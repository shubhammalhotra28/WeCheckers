package com.webcheckers.model;

import com.webcheckers.appl.PlayerLobby;

import java.util.logging.Logger;

/**
 * represents a player that is signed into the server
 */
public class Player {

    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    //attributes
    private String name; // private string variable declaration

    // initialising with the variables with the constructor
    public Player(String name) {
      // setting the value of name
     this.name = name;
    }

    /**
     * @return a string value of the name
     */
    public String getName(){

        return this.name; // returning the name
    }


    /**
     * See if other has the same name as this player
     * @param other the object to be checked for equality
     * @return if the two are equal based on their name
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Player))
             // if not the instance of player
            return false; // return false;
        Player otherP = (Player)other; // casting the object into Player and seclaring the variable
        String otherName = otherP.getName(); // getting the name and storing it
        return otherName.equals(name);// check if the name equals the recieved name and returns boolean value
    }

    /**
     * Compute the hashcode of a player based on the unique name of each player
     * @return the hashcode of the player
     */
    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
