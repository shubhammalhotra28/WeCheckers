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

public class PostSubmitTurnRoute implements Route{

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSubmitTurnRoute(final TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(gameCenter, "Game center must not be null");
        //
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.lobby = lobby;
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
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();


        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        Game game = null;

        if (thisPlayer != null){ //if you have signed in, attempt to get the shared game you are in
            game = gameCenter.getGame(thisPlayer.getName());
        }

        if (game != null) { //if i have signed in and there is a board for me
            if(game.getMoveSequence() != null){ //if there is a game for me
                if(game.allMovesMade()){
                    game.MakeMoves();
                    return gson.toJson(Message.info("The valid move(s) was submitted."));
                }
                return gson.toJson(Message.error("You have to finish your multi jump."));
            }else{
                game.MakeMoves();
                return gson.toJson(Message.info("The valid move(s) was submitted."));
            }
        }
        response.redirect(WebServer.HOME_URL);
//        halt(); if i include this then i cant test this part of the code <- throws an exception, TRY CATCH?
        return null;

    }

}

