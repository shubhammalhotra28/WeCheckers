package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

/**
 * the UI controller to GET the signin page
 */
public class GetSignInRoute implements Route{
    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    static final String TITLE_KEY = "title";
    static final String MSSG_KEY = "message";
    static final String TITLE_VAL = "Sign In";
    static final Message MSSG_VAL = Message.info("Enter your name to sign in");

    static final String SIGNIN_FTL = "signin.ftl";


    private final TemplateEngine templateEngine;


    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;

        LOG.config("GetSignInRoute is initialized.");

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
        LOG.info("GetSignInRoute is invoked.");

//        get the current httpSession if there is one
        final Session httpSession = request.session();

        //if the player is signed in, show the list of signed in players and use home.ftl
        if (httpSession.attribute(PostSignInRoute.PLAYER_KEY) != null){
            response.redirect(WebServer.HOME_URL);
            // render the View
            return null;

        }else {
//        the player is not signed in, and must sign in.
            //initialize the VM
            Map<String, Object> vm = new HashMap<>();
            vm.put(TITLE_KEY, TITLE_VAL);
            // display a user message in the Home page
            vm.put(MSSG_KEY, MSSG_VAL);

            // render the View
            return templateEngine.render(new ModelAndView(vm , SIGNIN_FTL));
        }
    }
}
