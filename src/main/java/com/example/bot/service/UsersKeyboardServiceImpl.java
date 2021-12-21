package com.example.bot.service;

import com.example.bot.entity.UsersKeyboard;
import com.example.bot.exception.NotFoundException;
import com.example.bot.repositoty.UsersKeyboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersKeyboardServiceImpl implements UsersKeyboardService {

    private final UsersKeyboardRepository usersKeyboardRepository;
    private static final Integer DEFAULT_KEYBOARD_NUMBER = 0;

    @Override
    public UsersKeyboard getByPeerIdAndGroupId(Integer peerId, Integer groupId) {
        UsersKeyboard usersKeyboard = usersKeyboardRepository.findByPeerIdAndGroupId(peerId, groupId);
        if (usersKeyboard != null) {
            return usersKeyboard;
        } else {
            return createDefaultUsersKeyboard(peerId, groupId);
        }
    }

    @Override
    public UsersKeyboard updateUsersKeyboard(UsersKeyboard u) {
        return usersKeyboardRepository.findById(u.getId())
                .map(usersKeyboard -> {
                    usersKeyboard.setKeyboardNumber(u.getKeyboardNumber());
                    return usersKeyboardRepository.save(usersKeyboard);
                }).orElseThrow(() -> new NotFoundException("UsersKeyboard not found with id " + u.getId()));
    }

    private UsersKeyboard createDefaultUsersKeyboard(Integer peerId, Integer groupId) {
        return usersKeyboardRepository.save(new UsersKeyboard(peerId, groupId, DEFAULT_KEYBOARD_NUMBER));
    }
}
