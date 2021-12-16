package com.example.bot;

import com.example.bot.entity.Kboard;
import com.example.bot.entity.VKGroup;
import com.example.bot.service.KboardService;
import com.example.bot.service.VKGroupService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.Message;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class VKServer {

    private static final Integer NUMBER_OF_ROWS = 5;
    private static final Integer NAVIGATE_KEYBOARD = 1;
    private static final Integer DEFAULT_DELAY = 100;
    private static final Integer RECONNECT_TIME = 10000;
    private VKCore vkCore;
    private List<Keyboard> keyboardList;
    private Map<VKCore, List<Keyboard>> vkCoreMap = new HashMap();
    private final KboardService kboardService;
    private final VKGroupService vkGroupService;
    private static Logger logger = Logger.getLogger(VKServer.class);

    void start() throws NullPointerException {
        logger.log(Level.INFO, "Running server...");
        List<VKGroup> vkGroups = vkGroupService.getAllVKGroup();

        vkGroups.forEach(s -> {
            try {
                vkCore = new VKCore(s.getGroupId(), s.getAccessToken());
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            }
            Keyboard keyboard = kboardService.getKeyboard(s.getKboard());
            if (keyboard.getButtons().size() > NUMBER_OF_ROWS) {
                Kboard navigateKeyboard = kboardService.getKboardById(NAVIGATE_KEYBOARD);
                List<KeyboardButton> navigateList = kboardService.getNavigateList(navigateKeyboard);
                keyboardList = kboardService.getKeyboardList(keyboard, navigateList, NUMBER_OF_ROWS);
            } else keyboardList = Collections.singletonList(keyboard);
            vkCoreMap.put(vkCore, keyboardList);
        });

        while (true) {
            vkCoreMap.forEach((k, v) -> {
                try {
                    Thread.sleep(DEFAULT_DELAY);
                    Message message = k.getMessage();
                    if (message != null) {
                        ExecutorService exec = Executors.newCachedThreadPool();
                        exec.execute(new Messanger(message, k, v));
                    }
                } catch (ClientException | InterruptedException | ApiException e) {
                    e.printStackTrace();
                    logger.log(Level.INFO, "Возникли проблемы");
                    logger.log(Level.INFO, "Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                    try {
                        Thread.sleep(RECONNECT_TIME);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }
}
