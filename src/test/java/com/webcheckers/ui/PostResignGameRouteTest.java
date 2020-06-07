package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Tag("UI-tier")
public class PostResignGameRouteTest {
    //attributes
    /**
     * The component-under-test (CuT).
     * This is a stateless component so we only need one.
     */
    private PostResignGameRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Game game;
    private Response response;

    //friendly objects
    private Gson gson = new Gson();


    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        //mock objects
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        gameCenter = mock(GameCenter.class);
        game = mock(Game.class);

        //define behavior
        when(request.session()).thenReturn(session);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new PostResignGameRoute(engine, gson, playerLobby, gameCenter);
    }

    @Test
    public void inEmptyState() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        //make sure game reports not in empty state
        when(game.emptyState()).thenReturn(true);

        //invoke test
        String messageReceived = (String) CuT.handle(request, response);

        //analyze results
        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Bob");

        //verify that the game method was called
        verify(game, times(1)).emptyState();

        //verify that the resign methods was called
        verify(gameCenter, times(1)).removeGameBoard("Bob");
        verify(game, times(1)).resign("Bob");

        //assert that the response is expected
        assertTrue(gson.toJson(Message.info("Resigned from game")).equals(messageReceived));
    }

    @Test
    public void notInEmptyState() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        //make sure game reports not in empty state
        when(game.emptyState()).thenReturn(false);

        //invoke test
        String messageReceived = (String) CuT.handle(request, response);

        //analyze results
        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Bob");

        //verify that the game method was called
        verify(game, times(1)).emptyState();

        //assert that the response is expected
        assertTrue(gson.toJson(Message.error("You may only resign in the empty turn state")).equals(messageReceived));
    }
}
