package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * A unit test for the ui GetHomeRoute Route Component
 */
@Tag("UI-tier")
public class PostBackupMoveRouteTest {

    //attributes
    /**
     * The component-under-test (CuT).
     * This is a stateless component so we only need one.
     */
    private PostBackupMoveRoute CuT;

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
        CuT = new PostBackupMoveRoute(gson, gameCenter);
    }

    /**
     * Test when you have signed in an there is a board
     */
    @Test
    public void validPlayerAndBoard(){
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

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        verify(gameCenter, times(1)).getGame("Bob");

        //verify that the game method was called
        verify(game, times(1)).clearMoves();

        //assert that the response is expected
        assertTrue(gson.toJson(Message.info("Last move undone.")).equals(messageReceived));
    }

    /**
     * Test when you are not signed in as a player
     * This should be a redirect to the home page and return null.
     */
    @Test
    public void invalidPlayer(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(null);

        //when I get the game from the game center
        when(gameCenter.getGame("Bob")).thenReturn(game);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        //verify that the game object was not retrieved
        verify(gameCenter, times(0)).getGame("Bob");

        //verify that the game method was NOT called
        verify(game, times(0)).clearMoves();

        //assert that the response is expected
        assertNull(messageReceived);
    }


    /**
     * Test when there is not a valid game
     * This should be a redirect to the home page and return null.
     */
    @Test
    public void invalidGame(){
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
        when(gameCenter.getGame("Bob")).thenReturn(null);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));
        //verify that the game object was not retrieved
        verify(gameCenter, times(1)).getGame("Bob");

        //verify that the game method was NOT called
        verify(game, times(0)).clearMoves();

        //assert that the response is expected
        assertNull(messageReceived);
    }

}
