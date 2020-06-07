package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Tag("UI-tier")
public class PostSignOutRouteTest {
    //attributes
    /**
     * The component-under-test (CuT).
     * This is a stateless component so we only need one.
     */
    private PostSignOutRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Game game;
    private Response response;

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
        game = mock(Game.class);
        gameCenter = mock(GameCenter.class);

        //define behavior
        when(request.session()).thenReturn(session);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new PostSignOutRoute(engine, playerLobby, gameCenter);
    }

    /**
     * Test the case where the user is signed in
     */
    @Test
    public void validSignOut(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player tempPlayer = new Player("Bob");
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(tempPlayer);

        // Invoke the test
        String received = (String) CuT.handle(request, response);
        //verify that the right methods were called
        verify(request, times(1)).session();
        verify(session, times(1)).attribute(PostSignInRoute.PLAYER_KEY);
        verify(playerLobby, times(1)).removePlayer(tempPlayer);
        verify(session, times(1)).removeAttribute(PostSignInRoute.PLAYER_KEY);
        verify(response, times(1)).redirect(WebServer.HOME_URL);

        //assert that the response is expected
        assertNull(received);
    }



    /**
     * Test the case where the user is not signed in
     */
    @Test
    public void invalidSignOut(){
// testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(null);

        // Invoke the test
        String received = (String) CuT.handle(request, response);
        //verify that the right methods were called
        verify(request, times(1)).session();
        verify(session, times(1)).attribute(PostSignInRoute.PLAYER_KEY);
        verify(playerLobby, times(0)).removePlayer(any());
        verify(session, times(0)).removeAttribute(PostSignInRoute.PLAYER_KEY);
        verify(response, times(1)).redirect(WebServer.HOME_URL);


        //assert that the response is expected
        assertNull(received);
    }
}

