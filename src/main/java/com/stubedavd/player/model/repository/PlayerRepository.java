package com.stubedavd.player.model.repository;

import com.stubedavd.player.model.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Player save(Player player);
    Optional<Player> findByName(String name);
}
