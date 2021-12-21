package com.example.bot.service;

import com.example.bot.entity.UsersKeyboard;

public interface UsersKeyboardService {

    UsersKeyboard getByPeerIdAndGroupId(Integer peerId, Integer groupId);

    UsersKeyboard updateUsersKeyboard(UsersKeyboard usersKeyboard);
}
