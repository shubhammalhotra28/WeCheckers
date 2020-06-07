package com.webcheckers.appl;


import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Tag("appl-tier")

public class PlayerLobbyTest {

    @Test
    public void test(){
        //setup
        final String playerName = "Bob";
        final String invalidPlayerName = "@Bob";
        Player player = mock(Player.class);
        Player invalidPlayer = mock(Player.class);
        when(player.getName()).thenReturn(playerName);
        when(invalidPlayer.getName()).thenReturn(invalidPlayerName);

        //initialization
        PlayerLobby CuT = new PlayerLobby();

        //check initial list of players
        List players = CuT.getPlayers();
        assertNotNull(players);
        assertEquals(0,players.size());

        //check for adding invalid named player
        CuT.addPlayer(invalidPlayer);
        assertEquals(0,players.size());

        //check for adding a player
        CuT.addPlayer(player);
        assertEquals(1,players.size());
        assertEquals(player, CuT.getPlayer(player.getName()));

        //check that player can only be added once
        CuT.addPlayer(player);
        assertEquals(1,players.size());

        //check that the player can be removed
        CuT.removePlayer(player);
        assertEquals(0,players.size());


    }

}
