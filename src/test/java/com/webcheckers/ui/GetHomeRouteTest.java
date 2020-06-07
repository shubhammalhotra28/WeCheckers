package com.webcheckers.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A unit test for the ui GetHomeRoute Route Component
 */
@Tag("UI-tier")
public class GetHomeRouteTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     */
    private GetHomeRoute CuT;


    // object attributes
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
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
        gameCenter = mock(GameCenter.class);
        //define behavior for when the signed in player accesses the players in the player lobby
        List<Player> players = new ArrayList<>();
        //add two already signed in players to the player lobby
        players.add(new Player("Tom"));
        players.add(new Player("Bob"));
        when(playerLobby.getPlayers()).thenReturn(players);


        // create a unique CuT for each test
        CuT = new GetHomeRoute(engine, playerLobby,gameCenter);
    }

    /**
     * Test that CuT shows the Home view when the player is not signed In.
     * shows the number of players in the game but not their names.
     */
    @Test
    public void notSignedIn() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
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
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_KEY, GetHomeRoute.TITLE_VAL);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSSG_KEY, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYER_KEY, true); //true because 2 players are in the player lobby
        testHelper.assertViewModelAttribute(GetHomeRoute.NUMBER_PLAYERS_KEY, "There are 2 player(s) currently signed in.");
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.HOME_FTL);

    }

    /**
     * Test that CuT shows the Home view when the player is signed in
     * Shows the list of players in the game.
     */
    @Test
    public void SignedIn() {
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
        CuT.handle(request, response);
        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_KEY, GetHomeRoute.TITLE_VAL);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSSG_KEY, GetHomeRoute.WELCOME_MSG);
        //test the player list only contains names that aren't mine
        List<Player> players = new ArrayList<>();
        players.add(new Player("Tom"));
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, players);
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.HOME_FTL);
    }
}
