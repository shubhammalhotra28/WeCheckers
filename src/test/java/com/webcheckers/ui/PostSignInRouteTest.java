package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Tag("UI-tier")
public class PostSignInRouteTest {
    //attributes
    /**
     * The component-under-test (CuT).
     * This is a stateless component so we only need one.
     */
    private PostSignInRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
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

        //define behavior
        when(request.session()).thenReturn(session);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new PostSignInRoute(engine, playerLobby);
    }

    @Test
    public void successful() {
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player name from the session, then the player has entered "Bob"
        when(request.queryParams(PostSignInRoute.NAME_PARAM)).thenReturn("Bob");

        //player is added successfully
        when(playerLobby.addPlayer(new Player("Bob"))).thenReturn(true);

        //invoke test
        CuT.handle(request, response);

        //verify that the user is redirected to the home route
        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }

    @Test
    public void unsuccessful() {
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //when I get the player name from the session, then the player has entered "Bob"
        when(request.queryParams(PostSignInRoute.NAME_PARAM)).thenReturn("Bob");

        //player is not added successfully
        when(playerLobby.addPlayer(new Player("Bob"))).thenReturn(false);

        //invoke test
        CuT.handle(request, response);

        //verify that the user is not redirected to the home route
        verify(response, times(0)).redirect(WebServer.HOME_URL);

        //verify correct attributes
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute("title", "Sign In");
        testHelper.assertViewModelAttribute("message", PostSignInRoute.ERROR_MSG);
        //   * test view name
        testHelper.assertViewName(GetSignInRoute.SIGNIN_FTL);
    }
}
