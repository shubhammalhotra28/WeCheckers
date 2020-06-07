package com.webcheckers.model;

/**
 * class to represent a move to be made on the board
 */
public class Move {
    //private attributes
    private Position start;
    private Position end;

    public Move(Position start, Position end){
        // constructor
        this.start = start;
        this.end = end;
    }

    /**
     * getter method for start position
     */
    public Position getStart() {
        // return the start of type Position
        return start;
    }

    /**
     * getter method for end position
     */
    public Position getEnd() {
        // return the end of type Position
        return end;
    }

    /**
     * @return a string representing this move
     */
    public String toString(){
        // to string method
        String moveString = ""; // initialising an empty string
        moveString += "Start(Row: " + start.getRow() + " Cell: " + start.getCell() + ") "; // concatinating and updating the string
        moveString += "End(Row: " + end.getRow() + " Cell: " + end.getCell() + ")"; // concatinating and updating the string
        return moveString; // return the string
    }

    /**
     * an alternate method for representing this move as a string
     * used for telling the user the options of where they can move
     * @return a string representing this move
     */
    public String altString(Piece.Color color) {
        Position s,e;
        if (color == Piece.Color.RED) {
            //invert the row
            s = new Position(7-start.getRow(),start.getCell());
            e = new Position(7-end.getRow(),end.getCell());
        } else {
            //invert the cell
            s = new Position(start.getRow(),7-start.getCell());
            e = new Position(end.getRow(),7-end.getCell());
        }
        return s + " to " + e;
    }

    /**
     * See if other Move has the same start and end Positions as this Move
     * @param other the object to be checked for equality
     * @return if the two are equal based on their name
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Move))
            // if not the instance of player
            return false; // then return false
        Move otherM = (Move)other; // casting the object recieved to type -> Move and storing it
        return this.start.equals(otherM.getStart()) && this.end.equals(otherM.getEnd()); // returning a boolean value after comparison
    }
}
