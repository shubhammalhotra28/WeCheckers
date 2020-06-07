package com.webcheckers.appl;

import com.webcheckers.model.Game;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

@Tag("appl-tier")
public class GameCenterTest {

    @Test
    public void test() {
        //setup
        //HashMap<String, Game> gmap;
        Game game = mock(Game.class);
        String playerName = "Bob";

        //create the GameCenter
        GameCenter CuT = new GameCenter();

        //validate the game map
        HashMap<String, Game> gmap = CuT.getGameMap();
        assertNotNull(gmap);
        assertEquals(0,gmap.size());

        //add a game to the map
        CuT.addGame(playerName,game);

        //verify the game was added
        assertEquals(1,gmap.size());
        assertEquals(game,gmap.get(playerName));

        //test the getGame method
        assertEquals(game,CuT.getGame(playerName));

        //remove the game from the map
        CuT.removeGameBoard(playerName);

        //verify the game was removed
        assertEquals(0,gmap.size());
        assertNull(gmap.get(playerName));
    }
}
