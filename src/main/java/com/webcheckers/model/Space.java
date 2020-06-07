package com.webcheckers.model;

/**
 * represents a space on the Checkers board
 */
public class Space {
    /**
     * Defines the color of the space
     */
    public enum SPACECOLOR{
        LIGHT, // light clor
        DARK // dark color
    }

    //Attributes
    private int cellIdx; // declaring the cellIds as integer
    private SPACECOLOR spaceColor; // declaring the spaceColor as SPACECOLOR
    private Piece piece; // declaring the piece of type Piece

    /**
     * Constructor for the space without a piece on it.
     * @param cellIdx - the index of the cell
     * @param spaceColor - the color of the space
     */
    public Space(int cellIdx, SPACECOLOR spaceColor){
        // constructor calling
        this.cellIdx = cellIdx; // setting the value of cellIdx
        this.spaceColor = spaceColor; // setting the value of spaceColor
    }

    /**
     * Constructor for the space with a piece on it
     * @param cellIdx - the index of the cell
     * @param spaceColor - the color of the space
     */
    public Space(int cellIdx, SPACECOLOR spaceColor, Piece piece){
        // constructor calling

        this.cellIdx = cellIdx; // setting the value of cellIdx
        this.piece = piece; // setting the value of piece
        this.spaceColor = spaceColor; // setting the spacecolor
    }

    /**
     * If the piece is not occupied and the space color is dark,
     * the space is valid/open for a piece.
     * @return True if valid, false if invalid
     */
    public boolean isValid(){
        Boolean temp = piece == null && spaceColor == SPACECOLOR.DARK;
        return temp;
    }

    /**
     * @return the piece on the board if one exists
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @return the index of the space
     */
    public int getCellIdx() {

        return cellIdx;
}

    /**
     * @return the color of the space
     */
    public SPACECOLOR getSpaceColor() {

        return spaceColor;
    }
}
