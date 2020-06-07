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

public class PostBackupMoveRoute implements Route{

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final Gson gson;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     */
    public PostBackupMoveRoute( Gson gson, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(gameCenter, "GameCenter must not be null");
        //
        this.gson = gson;
        this.gameCenter = gameCenter;

        LOG.config("PostValidateMoveRoute is initialized.");
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
        LOG.info("PostBackupMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        Game game = null;

        if (thisPlayer != null){ //if you have signed in, attempt to get the shared game you are in
            game = gameCenter.getGame(thisPlayer.getName());
        }

        if (game != null) { //if there is a game for me
            game.clearMoves();
            return gson.toJson(Message.info("Last move undone."));
        }
        response.redirect(WebServer.HOME_URL);
//        halt(); if i include this then i cant test this part of the code <- throws an exception, TRY CATCH?
        return null;
    }
}