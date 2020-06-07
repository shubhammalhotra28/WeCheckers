package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * stores a list and map of all players currently signed into the server
 */
public class PlayerLobby {
    // variable declarations
    private List<Player> players; // declaring the arrayList of type Players
    private HashMap<String, Player> playersMap; // declaring the hashmap with key of strings and values as Player
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());


    /**
     * A constructor for the player lobby.
     */
    public PlayerLobby(){
        // constructor calling
        players = new ArrayList<>(); // initialising the arrayList
        playersMap= new HashMap<>(); // initialising the hashMap
    }

    /**
     * Attempts to add a player to the Lobby
     * Will add the player if and only if a player of that name
     * is not already in the lobby
     *
     * @param player The player object to be added
     * @return
     *      true if the player was successfully added to the array
     *      false if the player was not added to the array -
     *          likely due to a player already being signed in with the same name
     *          or due to the player's name being invalid
     */
    public boolean addPlayer(Player player){
        //determine if it's legal to add this player
        boolean alreadyLoggedIn = isSignedIn(player);
        boolean validName = isValidName(player);
        if (alreadyLoggedIn || !validName) return false; //player not added to list
        //add player to list and map
        players.add(player);
        playersMap.put(player.getName(), player);
        LOG.config(String.format("Player \'%s\' added to the lobby",player.getName()));
        return true;
    }

    /**
     * remove a player from the player array and from the map
     * @param player the player to be removed
     */
    public void removePlayer(Player player){
        String name = player.getName();
        //find the player being removed in the list
        for(int i=0; i < players.size(); i++){
            if (players.get(i).getName().equals(name)){
                //remove the player
                players.remove(i);
                break;
            }
        }
        playersMap.remove(name);
        LOG.config(String.format("Player \'%s\' removed from lobby",player.getName()));
    }

    /**
     * getter method for the players list so that it can be displayed on the home page
     * @return players
     */
    public List getPlayers() {
        return players;
    }

    /**
     * get the player corresponding to a given name
     * @param name the name of the player
     * @return the Player object that corresponds to the name key
     */
    public Player getPlayer(String name){
        return playersMap.get(name);
    }

    /**
     * Helper method to determine if a player is already
     * in the players list
     * @param player the player to be checked
     * @return
     *      true if the player is already in the list
     *      false if there is no player with the same name in the list
     */
    protected boolean isSignedIn(Player player) {
        for (Player test: players) {
            if (player.equals(test)) return true;
        }
        return false;
    }

    /**
     * Determines if the player has a name that is syntactically valid (alphanumeric and spaces only)
     * @param player - the player's name to validate
     * @return true if the name is valid
     */
    private boolean isValidName(Player player){
        String name = player.getName();
        return name.matches("[\\w ]*[\\w]+[\\w ]*");
    }
}
