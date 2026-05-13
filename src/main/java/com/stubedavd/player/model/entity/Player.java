package com.stubedavd.player.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "players", indexes = @Index(name = "idx_player_name", columnList = "name"))

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Player {

    public Player(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 23)
    private String name;
}
