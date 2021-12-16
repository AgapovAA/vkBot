package com.example.bot.repositoty;

import com.example.bot.entity.VKGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VKGroupRepository extends JpaRepository<VKGroup, Integer> {
}
