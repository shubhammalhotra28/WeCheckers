package com.webcheckers.model;

/**
 * represents a piece on the game board
 */
public class Piece {
    /**
     * Defines the types of piece
     */
    public enum Type {
        SINGLE,
        KING
    }

    /**
     * Defines the colors of a piece
     */
    public enum Color {
        RED,
        WHITE
    }

    //attributes
    private Type type;
    private Color color;

    /**
     * The constructor for a piece
     * @param type - the type of piece
     * @param color - the color of the piece
     */
    public Piece(Type type, Color color){
        // constructor calling
        // initialising the value of attributes
        this.type = type;
        this.color = color;

    }

    /**
     * Get the type of this piece
     * @return - the type of this piece
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the color of this piece
     * @return - the color of this piece
     */
    public Color getColor() {
        return color;
    }
}
