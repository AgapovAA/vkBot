package com.example.bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "vk_group")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VKGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "group_id", nullable = false)
    private int groupId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kboard_id")
    private Kboard kboard;
}
