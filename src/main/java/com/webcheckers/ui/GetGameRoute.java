package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import com.webcheckers.model.*;
import com.webcheckers.util.ValidationHelper;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.*;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * a route to render the game page
 * invoked by GET /game
 */
public class GetGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    //attribute keys for the vm
    static final String TITLE_KEY = "title";
    static final String VIEW_MODE_KEY = "viewMode";
    static final String CURR_USER_KEY = "currentUser";
    static final String MESSAGE_KEY = "message";
    static final String RED_PLAYER_KEY = "redPlayer";
    static final String WHITE_PLAYER_KEY = "whitePlayer";
    static final String ACTIVE_COLOR_KEY = "activeColor";
    static final String BOARD_KEY = "board";
    static final String FLIPPED_KEY = "flipped";
    static final String MODE_OPTIONS_KEY = "modeOptionsAsJSON";
    static final Message GAME_MESSAGE_VAL = Message.info("Game On!");
    static final Message ERROR_MESSAGE_VAL = Message.info("The user you selected is already in a game. Try another!");
    static final String GAME_FTL = "game.ftl";

    protected enum gameMode {
        PLAY
    }


    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetGameRoute(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby lobby, final GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(lobby, "gameCenter must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.templateEngine = templateEngine;
        this.lobby = lobby;
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        Player thatPlayer;
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);

        if (thisPlayer != null) { //if i have signed in
            String theirName = request.queryParams("name");
            thatPlayer = lobby.getPlayer(theirName);

            //initialize the vm
            Map<String, Object> vm = new HashMap<>();
            vm.put(TITLE_KEY, "New Game");
            vm.put(VIEW_MODE_KEY, gameMode.PLAY);
            vm.put(CURR_USER_KEY, thisPlayer);
            vm.put(MESSAGE_KEY, GAME_MESSAGE_VAL);

            //if i show up here and there is not a game under my name but there is for the person I clicked on
            if (gameCenter.getGame(thisPlayer.getName()) == null && gameCenter.getGame(theirName) != null){//if my opponent already is in a game
                return error(request, response, thisPlayer);
            }else if(gameCenter.getGame(thisPlayer.getName()) == null){
                //i am the first one to show up to the game
                //create a game where I am the red player
                //create the shared game
                Game game = new Game(new BoardView(), thisPlayer.getName(), thatPlayer.getName(), Piece.Color.RED, new ValidationHelper());
                //add the game board for this player and that player
                gameCenter.addGame(thisPlayer.getName(), game);
                gameCenter.addGame(thatPlayer.getName(), game);
                //add the elements to the vm

                vm.put(RED_PLAYER_KEY, lobby.getPlayer(game.getRedPlayerName()));
                vm.put(WHITE_PLAYER_KEY, lobby.getPlayer(game.getWhitePlayerName()));
                vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());
                vm.put(BOARD_KEY, game.getBoard());
                //this red player clicked first, so their board is not flipped
                vm.put(FLIPPED_KEY, false);
                //possibly remove the opponent name functionality, replace with the game center and the board class
                return templateEngine.render(new ModelAndView(vm, "game.ftl"));
            }else if(gameCenter.getGame(thisPlayer.getName()) != null){
                //i show up here with an opponent
                //get the game that I am in from the game center
                Game game = gameCenter.getGame(thisPlayer.getName());


                //add the elements to the vm
                vm.put(RED_PLAYER_KEY, lobby.getPlayer(game.getRedPlayerName()));
                vm.put(WHITE_PLAYER_KEY, lobby.getPlayer(game.getWhitePlayerName()));
                vm.put(ACTIVE_COLOR_KEY, game.getActiveColor());
                vm.put(BOARD_KEY, game.getBoard());
                //if my name is the same as the red player, do not flip my board
                if (game.getRedPlayerName().equals(thisPlayer.getName())){
                    vm.put(FLIPPED_KEY, false); //this player is the red player, their board is not flipped
                }else{
                    //this player is the white player, so their board is flipped
                    vm.put(FLIPPED_KEY, true);
                }


                //check if the game is over for any reason
                if (game.gameOver()) {
                    final Map<String, Object> modeOptions = new HashMap<>(2);
                    modeOptions.put("isGameOver", true);
                    String gameEndMsg = game.getGameEndMsg();
                    String winner = game.getWinner();
                    if (winner.equals(thisPlayer.getName())) {
                        gameEndMsg += " You won the game!";
                    } else {
                        gameEndMsg += " " + winner + " won the game.";
                    }
                    modeOptions.put("gameOverMessage", gameEndMsg);
                    vm.put(MODE_OPTIONS_KEY, gson.toJson(modeOptions));
                    gameCenter.removeGameBoard(thisPlayer.getName());
                }

                //add help section if this is the active player
                //in else block because there is no point in giving help if game is over
                else if (game.amIActive(thisPlayer)) {
                    vm.put("piecesMSG",game.generatePiecesMsg());
                    vm.put("moves",game.generateMovesList());
                }

                return templateEngine.render(new ModelAndView(vm, GAME_FTL));
            }else{
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
        }
            response.redirect(WebServer.HOME_URL);
            //halt();
            return null;
    }



    /**
     * Handles the case if the username is taken or uses invalid syntax
     * Redirects the user to the sign in page to sign in again.
     */
    private String error(Request request, Response response, Player thisPlayer) {
        LOG.info("GetGameRoute-Error is invoked.");


        //add each online player
        List<Player> players = lobby.getPlayers();
        //make a copy of the players list, remove the current player
        //so that the current player does not show up as an available player
        List<Player> copy = new ArrayList<Player>(players);
        copy.remove(thisPlayer);

        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_KEY, "Home");

        // display a user message in the Home page
        vm.put(GetHomeRoute.MSSG_KEY, ERROR_MESSAGE_VAL);
        vm.put(GetHomeRoute.PLAYER_LIST_KEY, copy);

        //add the current player
        vm.put(GetHomeRoute.CURRENT_PLAYER_KEY, thisPlayer);

        if (gameCenter.getGame(thisPlayer.getName()) != null) {
            response.redirect(WebServer.GAME_URL);
        }

        // render the View
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.HOME_FTL));
    }
}

