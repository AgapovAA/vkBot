package com.example.bot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;

import java.util.Random;

public class Messanger implements Runnable {

    private Message message;
    private VKCore vkCore;
    private Keyboard keyboard;

    public Messanger(Message message, VKCore vkCore, Keyboard keyboard) {
        this.message = message;
        this.vkCore = vkCore;
        this.keyboard = keyboard;
    }

    @Override
    public void run() {
        Random random = new Random();
        String msg = message.getText();
        int peerId = message.getPeerId();

        try {
            vkCore
                    .getVk()
                    .messages()
                    .send(vkCore.getActor())
                    .keyboard(keyboard)
                    .peerId(peerId)
                    .randomId(random.nextInt(100000))
                    .message(msg)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}