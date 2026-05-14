package com.stubedavd;

import com.stubedavd.player.model.entity.Player;
import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.player.model.repository.PlayerRepository;
import com.stubedavd.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayerRepositoryTest {

    private static PlayerRepository playerRepository;

    @BeforeAll
    public static void setUp() {

        playerRepository = new PlayerRepository(HibernateUtil.getSessionFactory());
    }

    @Test
    public void testAddAlreadyExistsPlayer() {

        Player player = new Player("Test");

        playerRepository.save(player);

        try {

            playerRepository.save(player);
        } catch (EntityAlreadyExistException e) {

            System.out.println(e.getMessage());
            assert true;
        }

    }
}
