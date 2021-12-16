package com.example.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Messanger implements Runnable {

    private Message message;
    private VKCore vkCore;
    private List<Keyboard> keyboardList;
    private static Map<Integer, Integer> iterators = new HashMap();

    public Messanger(Message message, VKCore vkCore, List<Keyboard> keyboardList) {
        this.message = message;
        this.vkCore = vkCore;
        this.keyboardList = keyboardList;
    }

    @Override
    public void run() {
        Integer iter;
        Random random = new Random();
        String msg = message.getText();
        int peerId = message.getPeerId();

        if (iterators.containsKey(peerId)) {
            iter = iterators.get(peerId);
        } else {
            iter = 0;
            iterators.put(peerId, iter);
        }

        try {
            if (msg.equals("вперёд") && iter < (keyboardList.size() - 1)) {
                iter++;
                iterators.put(peerId, iter);
            }
            if (msg.equals("назад") && iter > 0) {
                iter--;
                iterators.put(peerId, iter);
            }
            if (msg.equals("отмена")) {
                iter = 0;
                iterators.put(peerId, iter);
            }
            vkCore
                        .getVk()
                        .messages()
                        .send(vkCore.getActor())
                        .keyboard(keyboardList.get(iter))
                        .peerId(peerId)
                        .randomId(random.nextInt(100000))
                        .message(msg)
                        .execute();
        } catch (ApiException |
                ClientException e) {
            e.printStackTrace();
        }
    }
}