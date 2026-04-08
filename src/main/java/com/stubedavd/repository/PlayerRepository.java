package com.stubedavd.repository;

import com.stubedavd.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player> {

    Optional<Player> findByName(String name);
}
