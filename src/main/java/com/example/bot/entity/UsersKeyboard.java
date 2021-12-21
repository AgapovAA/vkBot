package com.example.bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users_keyboard")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersKeyboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "peer_id", nullable = false)
    private int peerId;

    @Column(name = "group_id", nullable = false)
    private int groupId;

    @Column(name = "keyboard_number", nullable = false)
    private int keyboardNumber;

    public UsersKeyboard(int peerId, int groupId, int keyboardNumber) {
        this.peerId = peerId;
        this.groupId = groupId;
        this.keyboardNumber = keyboardNumber;
    }
}
