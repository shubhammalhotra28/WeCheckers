package com.webcheckers.ui;
import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

public class PostSignOutRoute implements Route{
    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignOutRoute(final TemplateEngine templateEngine, PlayerLobby lobby, GameCenter gameCenter) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        Objects.requireNonNull(gameCenter, "Gamecenter must not be null");
        this.templateEngine = templateEngine;
        this.lobby = lobby;
        this.gameCenter = gameCenter;

        LOG.config("PostSignOutRoute is initialized.");
        // get the name of the player and pass it to GEtHomeRoute with creation of object
    }


    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.info("PostSignOut is invoked.");
        //get the http session
        final Session httpSession = request.session();
        Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY); //get the player
        //if the player is signed in, handle the logic to sign them out
        if (thisPlayer != null) {
            Game game = gameCenter.getGame(thisPlayer.getName());
            if (game != null){
                response.redirect(WebServer.GAME_URL);
                return null;
            }
            lobby.removePlayer(thisPlayer); //remove the player from the player list and player map
            httpSession.removeAttribute(PostSignInRoute.PLAYER_KEY); //remove the player key session attribute
        }

//        //regardless if the player was signed in or not, redirect them to the home screen
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
