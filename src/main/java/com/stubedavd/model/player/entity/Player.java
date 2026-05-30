package com.stubedavd.model.player.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "players", indexes = @Index(name = "idx_player_name", columnList = "name"))

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Player {
    public static final int NAME_MAX_LENGTH = 23;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = NAME_MAX_LENGTH)
    private String name;

    public Player(String name) {
        this.name = name;
    }
}
