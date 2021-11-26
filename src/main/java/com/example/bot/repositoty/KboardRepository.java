package com.example.bot.repositoty;

import com.example.bot.entity.Kboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KboardRepository extends JpaRepository<Kboard, Integer> {
}
