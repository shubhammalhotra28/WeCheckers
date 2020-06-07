package com.webcheckers.ui;
import java.util.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import spark.*;

import com.webcheckers.util.Message;

import static spark.Spark.halt;

public class PostCheckTurnRoute implements Route{

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     */
    public PostCheckTurnRoute( Gson gson, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        //
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostCheckTurnRoute is initialized.");
    }



    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the validate move page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.info("PostCheckTurnRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        //get the game shared between the players
        Game game = gameCenter.getGame(thisPlayer.getName());

        if (thisPlayer != null && game != null) { //if i have signed in and there is a board for me
            //if the game is over, automatically render the endgame screen regardless of active player
            if (game.gameOver()) {
                return gson.toJson(Message.info("true"));
            }
            //figure out if I am active
            if (game.amIActive(thisPlayer)){
                return gson.toJson(Message.info("true"));
            }else{
                return gson.toJson(Message.error("Not your turn :("));
            }
        }
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;

    }
}

