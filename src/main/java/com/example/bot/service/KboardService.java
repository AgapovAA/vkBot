package com.example.bot.service;

import com.example.bot.entity.Kboard;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;

import java.util.List;

public interface KboardService {

    Kboard getKboardById(Integer id);

    List<KeyboardButton> getNavigateList(Kboard navigateKb);

    Keyboard getKeyboard(Kboard kboard);

    List<Keyboard> getKeyboardList(Keyboard k, List<KeyboardButton> navigateList, int n);
}
