package com.example.bot.service;

import com.example.bot.entity.VKGroup;
import com.example.bot.repositoty.VKGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VKGroupServiceImpl implements VKGroupService {

    private final VKGroupRepository vkGroupRepository;

    @Override
    public List<VKGroup> getAllVKGroup() {
        return vkGroupRepository.findAll();
    }
}
