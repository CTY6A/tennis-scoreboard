package com.stubedavd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "matches")

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player1")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "player2")
    private Player player2;

    @ManyToOne
    @JoinColumn(name = "winner")
    private Player winner;
}
