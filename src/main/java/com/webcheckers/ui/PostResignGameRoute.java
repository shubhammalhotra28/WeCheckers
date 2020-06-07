package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostResignGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final TemplateEngine templateEngine;
    private final Gson gson;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;


    public PostResignGameRoute(final TemplateEngine templateEngine, Gson gson, PlayerLobby lobby, GameCenter gameCenter) {
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

        LOG.config("PostResignGameRoute is initialized.");
    }

    public Object handle(Request request, Response response) {
        LOG.info("PostValidateMoveRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();

        //get the player that made the move from the session attributes
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);
        //get the game shared between the players
        Game game = gameCenter.getGame(thisPlayer.getName());

       if (!game.emptyState()) {
                //return message to reject resignation
                return gson.toJson(Message.error("You may only resign in the empty turn state"));
            } else {
           //remove the player from the game map and remove his opponent
           String playerName = thisPlayer.getName();
           gameCenter.removeGameBoard(playerName);
           game.resign(playerName);
           //return message to redirect back to home
           return gson.toJson(Message.info("Resigned from game"));
       }
    }
}
