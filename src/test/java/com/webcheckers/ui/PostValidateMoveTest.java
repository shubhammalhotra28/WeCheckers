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

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Tag("UI-tier")
public class PostValidateMoveTest {

    private PostValidateMoveRoute CuT;
    // mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private GameCenter gameCenter;
    private Gson gson;
    private Game game;


    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    void setUp() {

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        game = mock(Game.class);
        gameCenter = mock(GameCenter.class);
        gson = new Gson();

        // create a unique CuT for each test
        CuT = new PostValidateMoveRoute(engine, gson, gameCenter);
    }

    /**
     * Test the case that there it is a valid second move
     */
    @Test
    public void testSingleMove(){
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));
        when(gameCenter.getGame("Bob")).thenReturn(game);
        when(game.getMoveSequence()).thenReturn(null);
//        when(game.allMovesMade()).thenReturn(true);
        System.out.println(game.getMoveSequence());

        Move tempMove = new Move(new Position(5, 4), new Position(4, 3));
        when(request.queryParams("actionData")).thenReturn(gson.toJson(tempMove));
        BoardView board = new BoardView();
        when(game.getBoard()).thenReturn(board);
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);


        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertEquals("{\"text\":\"The move is a valid forward move.\",\"type\":\"INFO\"}", messageReceived);
    }

    @Test
    public void testInvalidForwardMove(){
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));
        when(gameCenter.getGame("Bob")).thenReturn(game);
        when(game.getMoveSequence()).thenReturn(null);

        Move tempMove = new Move(new Position(5, 4), new Position(4, 4));
        when(request.queryParams("actionData")).thenReturn(gson.toJson(tempMove));
        BoardView board = new BoardView();
        when(game.getBoard()).thenReturn(board);
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);


        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertEquals(messageReceived, "{\"text\":\"The move is not a valid forward move.\",\"type\":\"ERROR\"}");
    }


    /**
     * Test a valid second jump move
     */
    @Test
    public void TestGoodLastJump(){
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));
        when(gameCenter.getGame("Bob")).thenReturn(game);

        Move tempMove = new Move(new Position(5, 4), new Position(4, 3));
        when(request.queryParams("actionData")).thenReturn(gson.toJson(tempMove));
        BoardView board = mock(BoardView.class);
        when(game.getBoard()).thenReturn(board);
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);
        when(game.getMoveSequence()).thenReturn(new ArrayList<Move>(Arrays.asList(tempMove)));
        when(game.getLastMoveToMake()).thenReturn(tempMove);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertEquals(messageReceived, "{\"text\":\"The move is the last continuation of a jump.\",\"type\":\"INFO\"}");
    }


    /**
     * Test a valid second jump move
     */
    @Test
    public void TestBadJump(){
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));
        when(gameCenter.getGame("Bob")).thenReturn(game);

        Move tempMove = new Move(new Position(5, 4), new Position(4, 3));
        Move otherMove = new Move(new Position(5, 4), new Position(4, 5));
        when(request.queryParams("actionData")).thenReturn(gson.toJson(tempMove));
        BoardView board = mock(BoardView.class);
        when(game.getBoard()).thenReturn(board);
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);
        when(game.getMoveSequence()).thenReturn(new ArrayList<Move>(Arrays.asList(otherMove)));

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertEquals(messageReceived, "{\"text\":\"You must continue the jump.\",\"type\":\"ERROR\"}");
    }


    /**
     * Test when you are not signed in as a player
     * This should be a redirect to the home page and return null.
     */
    @Test
    public void notSignedIn(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        //when I get the player key from the session, then the player is not signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(null);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertNull(messageReceived);
    }

    /**
     * Test when there is not a valid game
     * This should be a redirect to the home page and return null.
     */
    @Test
    public void noGame(){
        //when I get the player key from the session, then the player is already signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(new Player("Bob"));
        when(gameCenter.getGame("Bob")).thenReturn(null);

        // Invoke the test
        String messageReceived = (String) CuT.handle(request, response);

        //   * verify that a player object was checked once
        verify(session, times(1)).attribute(eq(PostSignInRoute.PLAYER_KEY));

        //assert that the response is expected
        assertNull(messageReceived);
    }

}
