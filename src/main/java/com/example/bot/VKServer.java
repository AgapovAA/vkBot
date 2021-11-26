package com.example.bot;

import com.example.bot.entity.Kboard;
import com.example.bot.service.KboardService;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@RequiredArgsConstructor
public class VKServer {

    private final VKCore vkCore;
    private final KboardService kboardService;
    private static final Integer DEFAULT_KEYBOARD = 2;
    private static final Integer NUMBER_OF_ROWS = 5;
    private static final Integer NAVIGATE_KEYBOARD = 1;
    private static final Integer DEFAULT_DELAY = 300;
    private static final Integer RECONNECT_TIME = 10000;
    private List<Keyboard> keyboardList;
    private static Logger logger = Logger.getLogger(VKServer.class);

    void start() throws NullPointerException, ApiException, InterruptedException {
        logger.log(Level.INFO, "Running server...");
        Kboard kboard = kboardService.getKboardById(DEFAULT_KEYBOARD);
        Keyboard keyboard = kboardService.getKeyboard(kboard);

        if (keyboard.getButtons().size() > NUMBER_OF_ROWS) {
            Kboard navigateKeyboard = kboardService.getKboardById(NAVIGATE_KEYBOARD);
            List<KeyboardButton> navigateList = kboardService.getNavigateList(navigateKeyboard);
            keyboardList = kboardService.getKeyboardList(keyboard, navigateList, NUMBER_OF_ROWS);
        } else keyboardList = Collections.singletonList(keyboard);

        while (true) {
            Thread.sleep(DEFAULT_DELAY);
            try {
                Message message = vkCore.getMessage();
                if (message != null) {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messanger(message, vkCore, keyboardList));
                }
            } catch (ClientException e) {
                logger.log(Level.INFO, "Возникли проблемы");
                logger.log(Level.INFO, "Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);
            }
        }
    }
}
