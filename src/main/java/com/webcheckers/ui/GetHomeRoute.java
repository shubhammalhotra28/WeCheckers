package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  //attribute keys for the vm
  static final String TITLE_KEY = "title";
  static final String MSSG_KEY = "message";
  static final String AVAILABLE_PLAYER_KEY = "availablePlayers";
  static final String NUMBER_PLAYERS_KEY = "playersSignedIn";
  static final String PLAYER_LIST_KEY = "players";
  static final String CURRENT_PLAYER_KEY = "currentUser";

  //attribute values for the vm
  static final String TITLE_VAL = "Welcome!";
  static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  static final String NUMBER_PLAYERS_MSSG = "There are %d player(s) currently signed in.";

  //ftl used
  static final String HOME_FTL = "home.ftl";

  //object attributes
  private final TemplateEngine templateEngine;
  private final PlayerLobby lobby;
  private final GameCenter gameCenter;

  //logger
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());


  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby lobby, GameCenter gameCenter) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.lobby = Objects.requireNonNull(lobby, "lobby is required");
    this.gameCenter = Objects.requireNonNull(gameCenter, "game center is required");
    //
    LOG.config("GetHomeRoute is initialized.");
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
    LOG.info("GetHomeRoute is invoked.");

    // retrieve the HTTP session
    final Session httpSession = request.session();
    Player thisPlayer = httpSession.attribute(PostSignInRoute.PLAYER_KEY);

    //create the hashmap
    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_KEY, TITLE_VAL);
    // display a user message in the Home page
    vm.put(MSSG_KEY, WELCOME_MSG);

    //if the player is signed in, show the list of signed in players
    if (thisPlayer != null){
      //add the current player to the hashmap
      vm.put(CURRENT_PLAYER_KEY, thisPlayer);

      //add each online player
      List<Player> players = lobby.getPlayers();
      //make a copy of the players list and remove the current player
      //so that the current player does not show up as an available player
      List<Player> copy = new ArrayList<Player>(players);
      copy.remove(thisPlayer);
      vm.put(PLAYER_LIST_KEY, copy);

      //check if this player is in a game
      if (gameCenter.getGame(thisPlayer.getName()) != null) {
        response.redirect(WebServer.GAME_URL);
      }

      // render the View
      return templateEngine.render(new ModelAndView(vm , HOME_FTL));
    }else{
      //if the player is not signed in, show the number of signed in players
      //add each online player
      List<Player> players = lobby.getPlayers();
      int playersSize = players.size();
      boolean availablePlayers = playersSize>0;
      vm.put(AVAILABLE_PLAYER_KEY, availablePlayers);
      vm.put(NUMBER_PLAYERS_KEY, String.format(NUMBER_PLAYERS_MSSG, playersSize));

      // render the View
      return templateEngine.render(new ModelAndView(vm , HOME_FTL));

    }
  }
}
