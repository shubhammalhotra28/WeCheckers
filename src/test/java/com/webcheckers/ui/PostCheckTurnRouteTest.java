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
public class PostCheckTurnRouteTest {
    //attributes
    /**
     * The component-under-test (CuT).
     * This is a stateless component so we only need one.
     */
    private PostCheckTurnRoute CuT;

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
        CuT = new PostCheckTurnRoute(gson, gameCenter);
    }

    @Test
    public void gameIsOver() {
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        //when I see if the game is over it will report true
        when(game.gameOver()).thenReturn(true);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        //verify that the game object was retrieved
        verify(gameCenter, times(1)).getGame("Bob");
        //verify that the game was checked for game over
        verify(game, times(1)).gameOver();

        //verify that the program returned true in order to rerender the board
        assertTrue(gson.toJson(Message.info("true")).equals(messageReceived));
    }

    @Test
    public void isMyTurn() {
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        Player tempPlayer = new Player("Bob");
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(tempPlayer);

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        //when I see if the game is over it will report false
        when(game.gameOver()).thenReturn(false);

        //when I see if I am the active player report true
        when(game.amIActive(tempPlayer)).thenReturn(true);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        //verify that the game object was retrieved
        verify(gameCenter, times(1)).getGame("Bob");
        //verify that the game was checked for game over
        verify(game, times(1)).gameOver();
        //verify that the game was checked for active player
        verify(game, times(1)).amIActive(tempPlayer);

        //verify that the program returned true in order to rerender the board
        assertTrue(gson.toJson(Message.info("true")).equals(messageReceived));
    }

    @Test
    public void isNotMyTurn() {
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        Player tempPlayer = new Player("Bob");
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(tempPlayer);

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        //when I see if the game is over it will report false
        when(game.gameOver()).thenReturn(false);

        //when I see if I am the active player report false
        when(game.amIActive(tempPlayer)).thenReturn(false);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        //verify that the game object was retrieved
        verify(gameCenter, times(1)).getGame("Bob");
        //verify that the game was checked for game over
        verify(game, times(1)).gameOver();
        //verify that the game was checked for active player
        verify(game, times(1)).amIActive(tempPlayer);

        //verify that the program returned false in order to not rerender the board
        assertTrue(gson.toJson(Message.error("Not your turn :(")).equals(messageReceived));
    }
}
