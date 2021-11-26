package com.example.bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "kboard")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kboard_name", nullable = false)
    private String kboardName;

    @Column(name = "one_time", nullable = false)
    private Boolean oneTime;

    @Column(name = "inline", nullable = false)
    private Boolean inline;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "kboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Button> buttonList;
}