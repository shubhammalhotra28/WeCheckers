package com.webcheckers.model;

//import sun.jvm.hotspot.runtime.posix.POSIXSignals;

/**
 * represents a location on the board in row,col coordinates
 */
public class Position {
    //attributes
    private int row; // declaring private variable of type int
    private int cell; // declaring private variable of type int

    public Position(int row, int cell){
        // constructor
        this.row = row; // setting the value of row
        this.cell = cell; // setting the value of cell
    }

    public int getRow() {
        // returning row

        return row;
    }

    public int getCell() {
        // returning the cell
        return cell;
    }

    /**
     * See if other Postion has the same row and cell integers as this Position
     * @param other the object to be checked for equality
     * @return if the two are equal based on their row and cell integers
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Position))
            // if not the instance of player
            return false; // then return false;
        Position otherP = (Position)other; // casting the object and setting it to the variable of type Position
        return this.row == otherP.getRow() && this.cell == otherP.getCell(); // returning the boolean value after checking the condition set
    }

    /**
     * @return a string representing this postition
     */
    @Override
    public String toString() {
        return "(" + Integer.toString(row) + "," + Integer.toString(cell) + ")";
    }
}
