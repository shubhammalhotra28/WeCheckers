package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Tag("UI-tier")
class GetGameRouteTest {


    private GetGameRoute CuT;
    // mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private GameCenter gameCenter;
    private Gson gson;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    void setUp() {

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        //friendly classes
        playerLobby = new PlayerLobby();
        gameCenter = new GameCenter();
        gson = new Gson();


        // create a unique CuT for each test
        CuT = new GetGameRoute(engine, gson, playerLobby, gameCenter);
    }


    /**
     * Test creating a game with an opponent who is not already in a gam
     */
    @Test
    public void createValidGame(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player thisPlayer = new Player("Bob");
        Player thatPlayer = new Player("Tom");
        playerLobby.addPlayer(thisPlayer);
        playerLobby.addPlayer(thatPlayer);

        //when I get the player key from the session, then the player is signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(thisPlayer);
        //when I get query for the other player, it exists
        when(request.queryParams(eq("name"))).thenReturn(thatPlayer.getName());

        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "New Game");
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.PLAY);
        testHelper.assertViewModelAttribute(GetGameRoute.CURR_USER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.MESSAGE_KEY, GetGameRoute.GAME_MESSAGE_VAL);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_KEY, thatPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR_KEY, Piece.Color.RED);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD_KEY, gameCenter.getGame(thisPlayer.getName()).getBoard());
        testHelper.assertViewModelAttribute(GetGameRoute.FLIPPED_KEY, false);

        //   * test view name
        testHelper.assertViewName(GetGameRoute.GAME_FTL);
    }

    /**
     * Test joining an already created game
     */
    @Test
    public void joinGame(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player thisPlayer = new Player("Bob");
        Player thatPlayer = new Player("Tom");
        playerLobby.addPlayer(thisPlayer);
        playerLobby.addPlayer(thatPlayer);

        Game game = mock(Game.class);
        when(game.getRedPlayerName()).thenReturn("Tom");
        when(game.getWhitePlayerName()).thenReturn("Bob");
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);

        gameCenter.addGame(thisPlayer.getName(), game);
        gameCenter.addGame(thatPlayer.getName(), game);

        //when I get the player key from the session, then the player is signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(thisPlayer);
        //when I get query for the other player, it exists
        when(request.queryParams(eq("name"))).thenReturn(null);

        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "New Game");
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.PLAY);
        testHelper.assertViewModelAttribute(GetGameRoute.CURR_USER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.MESSAGE_KEY, GetGameRoute.GAME_MESSAGE_VAL);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_KEY, thatPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR_KEY, Piece.Color.RED);
        testHelper.assertViewModelAttribute(GetGameRoute.BOARD_KEY, gameCenter.getGame(thisPlayer.getName()).getBoard());
        testHelper.assertViewModelAttribute(GetGameRoute.FLIPPED_KEY, true);

        //   * test view name
        testHelper.assertViewName(GetGameRoute.GAME_FTL);
    }

    /**
     * Test joining a game that is over.
     */
    @Test
    public void gameOver(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player thisPlayer = new Player("Bob");
        Player thatPlayer = new Player("Tom");
        playerLobby.addPlayer(thisPlayer);
        playerLobby.addPlayer(thatPlayer);

        Game game = mock(Game.class);
        when(game.getRedPlayerName()).thenReturn("Tom");
        when(game.getWhitePlayerName()).thenReturn("Bob");
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);
        when(game.gameOver()).thenReturn(true);
        when(game.getWinner()).thenReturn(thisPlayer.getName());
        BoardView boardView = mock(BoardView.class);
        when(game.getBoard()).thenReturn(boardView);

        gameCenter.addGame(thisPlayer.getName(), game);
        gameCenter.addGame(thatPlayer.getName(), game);

        //when I get the player key from the session, then the player is signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(thisPlayer);
        //when I get query for the other player, it exists
        when(request.queryParams(eq("name"))).thenReturn(null);

        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "New Game");
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_KEY, GetGameRoute.gameMode.PLAY);
        testHelper.assertViewModelAttribute(GetGameRoute.CURR_USER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.MESSAGE_KEY, GetGameRoute.GAME_MESSAGE_VAL);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_KEY, thatPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_KEY, thisPlayer);
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR_KEY, Piece.Color.RED);
//        testHelper.assertViewModelAttribute(GetGameRoute.BOARD_KEY, any(BoardView.class));
        testHelper.assertViewModelAttribute(GetGameRoute.FLIPPED_KEY, true);
        testHelper.assertViewModelAttribute(GetGameRoute.MODE_OPTIONS_KEY, "{\"isGameOver\":true,\"gameOverMessage\":\"null You won the game!\"}");


        //   * test view name
        testHelper.assertViewName(GetGameRoute.GAME_FTL);
    }

    /**modeOptionsAsJSON
     * Test clicking on a player who is already in a game
     */
    @Test
    public void invalidClick(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player thisPlayer = new Player("Bob");
        Player thatPlayer = new Player("Tom");
        playerLobby.addPlayer(thisPlayer);
        playerLobby.addPlayer(thatPlayer);

        //when I get the player key from the session, then the player is signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(thisPlayer);
        //when I get query for the other player, it exists
        when(request.queryParams(eq("name"))).thenReturn(thatPlayer.getName());

        Game game = mock(Game.class);
        gameCenter.addGame(thatPlayer.getName(), game);

        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.TITLE_KEY, "Home");
        testHelper.assertViewModelAttribute(GetHomeRoute.MSSG_KEY, GetGameRoute.ERROR_MESSAGE_VAL);
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER_KEY, thisPlayer);
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.HOME_FTL);
    }

    @Test
    public void helpWindow(){
        // testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_KEY, moc);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        Player thisPlayer = new Player("Bob");
        Player thatPlayer = new Player("Tom");
        playerLobby.addPlayer(thisPlayer);
        playerLobby.addPlayer(thatPlayer);

        Game game = mock(Game.class);
        when(game.getRedPlayerName()).thenReturn("Tom");
        when(game.getWhitePlayerName()).thenReturn("Bob");
        when(game.getActiveColor()).thenReturn(Piece.Color.RED);

        gameCenter.addGame(thisPlayer.getName(), game);
        gameCenter.addGame(thatPlayer.getName(), game);

        //when I get the player key from the session, then the player is signed in
        when(session.attribute(eq(PostSignInRoute.PLAYER_KEY))).thenReturn(thisPlayer);
        //when I get query for the other player, it exists
        when(request.queryParams(eq("name"))).thenReturn(null);

        //mock data for the help window
        when(game.gameOver()).thenReturn(false); //so that end game will not trigger
        when(game.amIActive(thisPlayer)).thenReturn(true); //so that help *should* be displayed
        //constants to verify that the help window is rendered
        final String testPieceStr = "This is num pieces";
        final List<String> testMovesList = new ArrayList<String>();
        when(game.generatePiecesMsg()).thenReturn(testPieceStr);
        when(game.generateMovesList()).thenReturn(testMovesList);

        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //check data for help window
        testHelper.assertViewModelAttribute("piecesMSG", testPieceStr);
        testHelper.assertViewModelAttribute("moves", testMovesList);
    }




    /**
     * Test that get game will error if you are not signed in
     */
    @Test
    public void improperSession() {
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
        String messageReceived = (String) CuT.handle(request, response);

        //verify that the right methods were or were not called
        verify(request, times(1)).session();
        verify(session, times(1)).attribute(PostSignInRoute.PLAYER_KEY);
        verify(response, times(1)).redirect(WebServer.HOME_URL);

        //assert that the response is expected
        assertNull(messageReceived);
    }
}