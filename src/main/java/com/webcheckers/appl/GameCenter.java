package com.webcheckers.appl;

import com.webcheckers.model.Game;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * GameCenter class stores a map of all games currently happening on the server
 */
public class GameCenter {
    //variable declarations
    private HashMap<String, Game> gameMap; // declaring the hashmap with key as string and value as Game
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    /**
     * A constructor for the game center
     */
    public GameCenter(){
        gameMap = new HashMap<>(); // initialising the hashMap
    }

    /**
     * add a game to the game map
     * @param name the name of the player to be used as the key
     * @param game the game that is being added
     */
    public void addGame(String name, Game game){
        gameMap.put(name, game); // adding the attributes the hashmap named game
        LOG.config(String.format("%s added to a game.", name));
    }

    /**
     * remove a name,game pair from the map
     * @param name the name of the player that is the key to be removed
     */
    public void removeGameBoard(String name){
        gameMap.remove(name, getGame(name));
        LOG.config(String.format("%s removed from a game.", name));
    }

    /**
     * get a game from the map that corresponds to this key
     * @param name the name of the player in the game
     * @return
     */
    public Game getGame(String name) {
        return gameMap.get(name);
    }

    /**
     * To be used only for unit testing
     */
    public HashMap<String, Game> getGameMap() {return gameMap;}
}
