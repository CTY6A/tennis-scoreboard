package com.stubedavd.model.player.repository;

import com.stubedavd.model.player.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Player save(Player player);
    Optional<Player> findByName(String name);
}
