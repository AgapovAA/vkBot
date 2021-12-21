package com.example.bot.service;

import com.example.bot.entity.Kboard;
import com.example.bot.exception.NotFoundException;
import com.example.bot.repositoty.KboardRepository;
import com.vk.api.sdk.objects.messages.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KboardServiceImpl implements KboardService {

    private final KboardRepository KboardRepository;

    @Override
    public Kboard getKboardById(Integer id) {
        Optional<Kboard> opt = KboardRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new NotFoundException("Kboard not found with id " + id);
        }
    }

    @Override
    public List<KeyboardButton> getNavigateList(Kboard navigateKb) {
        List<KeyboardButton> navigate = new ArrayList<>();
        navigateKb.getButtonList().forEach(s -> {
            while (s.getPlace() > navigate.size())
                navigate.add(new KeyboardButton());
            KeyboardButton keyboardButton = new KeyboardButton()
                    .setAction(new KeyboardButtonAction()
                            .setLabel(s.getLabel())
                            .setPayload(s.getPayload())
                            .setType(TemplateActionTypeNames.valueOf(s.getType())))
                    .setColor(KeyboardButtonColor.valueOf(s.getColor()));
            navigate.set(s.getPlace() - 1, keyboardButton);
        });
        return navigate;
    }

    @Override
    public Keyboard getKeyboard(Kboard kboard) {
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> all = new ArrayList<>();
        kboard.getButtonList().forEach(s -> {
            while (s.getRow() > all.size())
                all.add(new ArrayList<>());
            while (s.getPlace() > all.get(s.getRow() - 1).size())
                all.get(s.getRow() - 1).add(new KeyboardButton());
            KeyboardButton keyboardButton = new KeyboardButton()
                    .setAction(new KeyboardButtonAction()
                            .setLabel(s.getLabel())
                            .setType(TemplateActionTypeNames.valueOf(s.getType())))
                    .setColor(KeyboardButtonColor.valueOf(s.getColor()));
            all.get(s.getRow() - 1).set(s.getPlace() - 1, keyboardButton);
        });
        keyboard.setButtons(all).setInline(kboard.getInline()).setOneTime(kboard.getOneTime());
        return keyboard;
    }

    @Override
    public List<Keyboard> getKeyboardList(Keyboard k, List<KeyboardButton> navigateList, int n) {
        List<Keyboard> keyboardList = new ArrayList<>();
        int a = 0;
        Iterator<List<KeyboardButton>> iterator = k.getButtons().iterator();
        while (iterator.hasNext()) {
            Keyboard keyboard = new Keyboard();
            List<List<KeyboardButton>> all = new ArrayList<>();
            for (int i = a; i < a + n && iterator.hasNext(); i++) {
                all.add(iterator.next());
            }
            all.add(navigateList);
            keyboard.setButtons(all);
            keyboardList.add(keyboard);
            a += n;
        }
        return keyboardList;
    }
}