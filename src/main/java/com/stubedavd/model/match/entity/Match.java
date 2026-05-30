package com.stubedavd.model.match.entity;

import com.stubedavd.model.player.entity.Player;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;


@Entity
@Table(name = "tennis_matches")
@Check(constraints = "player1_id <> player2_id AND winner_id IN (player1_id, player2_id)")

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Match {

    public Match(Player player1, Player player2, Player winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player1_id", nullable = false, updatable = false)
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player2_id", nullable = false, updatable = false)
    private Player player2;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winner_id", nullable = false, updatable = false)
    private Player winner;
}
