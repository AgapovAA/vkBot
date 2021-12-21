package com.example.bot.repositoty;

import com.example.bot.entity.UsersKeyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersKeyboardRepository extends JpaRepository<UsersKeyboard, Integer> {

    UsersKeyboard findByPeerIdAndGroupId(int peerId, int groupId);
}
