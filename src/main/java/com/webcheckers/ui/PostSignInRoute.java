package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import javax.print.attribute.standard.MediaSize;

import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;



public class PostSignInRoute implements Route{

    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    // Constants for rendering after sign in request
    static final String NAME_PARAM = "userName";
    static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers. Select an available player below!");
    static final String PLAYER_KEY = "myPlayer";

    public static final Message ERROR_MSG = Message.info("Username is either already in use or invalid. Try another! (only alphanumeric characters)");





    // Attributes
    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;


    /**
     * The constructor for the {@code POST /guess} route handler.
     *
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *    when the {@code gameCenter} or {@code templateEngine} parameter is null
     */
    PostSignInRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(lobby, "lobby must not be null");
        //
        this.templateEngine = templateEngine;
        this.lobby = lobby;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException
     *    when an invalid result is returned after making a guess
     */
    @Override
    public String handle(Request request, Response response) {
        //render the page
        LOG.info("PostSignInRoute is invoked.");

        // retrieve the HTTP session
        final Session httpSession = request.session();


        //retrieve the username parameter
        final String username = request.queryParams(NAME_PARAM);
        final Player thisPlayer = new Player(username);
        httpSession.attribute(PLAYER_KEY, thisPlayer);

        //attempt to add the player to the lobby
        //'added' will tell whether it was successful or not
        boolean added = lobby.addPlayer(thisPlayer);

        if (added){
            //handle success

            /**
             * The logic below can be simplified by redirecting the user to the home page
             * The redirect essentially just calls get home route and uses the logic from that page'
             * The goal of the redirect is to prevent limit the same code existing in multiple routes
             *
             * The error handling is still useful though.
             */

            response.redirect(WebServer.HOME_URL);
            return null; //the though is that the program will never get here because of the redirect
        }else{
            //handle an error - go back to sign in page
            return error();
        }



    }

    /**
     * Handles the case if the username is taken or uses invalid syntax
     * Redirects the user to the sign in page to sign in again.
     */
    private String error(){
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Sign In");
        vm.put("message", ERROR_MSG);
        // render the View
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }
}
