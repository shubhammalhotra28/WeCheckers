package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A unit test for the ui GetSignInRoute Route Component
 */
@Tag("UI-tier")
public class GetSignInRouteTest {
    //attributes

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     */
    private GetSignInRoute CuT;


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
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        //define behavior for when the signed in player accesses the players in the player lobby
        List<Player> players = new ArrayList<>();
        //add two already signed in players to the player lobby
        players.add(new Player("Tom"));
        players.add(new Player("Bob"));
        when(playerLobby.getPlayers()).thenReturn(players);


        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new GetSignInRoute(engine);

    }

    @Test
    public void signedIn(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));

        // Invoke the test
        String received = (String) CuT.handle(request, response);

        //verify that the user is redirected to the home route
        verify(response, times(1)).redirect(WebServer.HOME_URL);
        //verify that null is returned
        assertNull(received);
    }

    @Test
    public void notSignedIn(){
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
        CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_KEY, GetSignInRoute.TITLE_VAL);
        testHelper.assertViewModelAttribute(GetSignInRoute.MSSG_KEY, GetSignInRoute.MSSG_VAL);
        //   * test view name
        testHelper.assertViewName(GetSignInRoute.SIGNIN_FTL);

    }
}
