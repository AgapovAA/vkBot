package com.example.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class Messanger implements Runnable {

    private final Message message;
    private final VKCore vkCore;
    private final List<Keyboard> keyboardList;
    private static HashMap<Integer, Integer> iterators = new HashMap();

    @Override
    public void run() {
        Integer iterator;
        Random random = new Random();
        String msg = message.getText();
        int peerId = message.getFromId();

        if (iterators.containsKey(peerId)) {
            iterator = iterators.get(peerId);
        } else {
            iterator = 0;
            iterators.put(peerId, iterator);
        }

        try {
            if (msg.equals("вперёд") && iterator < (keyboardList.size() - 1)) {
                iterator++;
                iterators.put(peerId, iterator);
            }
            if (msg.equals("назад") && iterator > 0) {
                iterator--;
                iterators.put(peerId, iterator);
            }
            if (msg.equals("отмена")) {
                iterator = 0;
                iterators.put(peerId, iterator);
            }
            vkCore
                        .getVk()
                        .messages()
                        .send(vkCore.getActor())
                        .keyboard(keyboardList.get(iterator))
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