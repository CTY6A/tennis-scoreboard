package com.stubedavd;

import com.stubedavd.player.entity.Player;
import com.stubedavd.exception.AlreadyExistException;
import com.stubedavd.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayerRepositoryTest {

    private static PlayerRepository playerRepository;

    @BeforeAll
    public static void setUp() {

        playerRepository = new PlayerRepository();
    }

    @Test
    public void testAddAlreadyExistsPlayer() {

        Player player = new Player();
        player.setName("Test");

        playerRepository.save(player);

        try {

            playerRepository.save(player);
        } catch (AlreadyExistException e) {

            System.out.println(e.getMessage());
            assert true;
        }

    }
}
