package com.webcheckers.model;


import java.awt.geom.PathIterator;
import java.util.Iterator;

/**
 * represents a row on the game board
 * implemented as an array of spaces
 */
public class Row implements Iterable<Space> {

    //attributes
    private Space[] spaces; // declaring the array of Space named spaces
    private int index; // declaring the index as an integer

    /**
     * The constructor for a row.
     * Starts with a light square first (leftmost) if the row is an even index.
     * Starts with a dark square first (leftmost) if the row is an odd index.
     *
     * @param index - the index (0-topmost & 7 bottom most) of the row
     */
    public Row(int index, Piece.Color topColor) {
        //constructor calling
        // initializing the values
        this.index = index; // setting the index

        this.spaces = new Space [8]; // setting the spaces

        //used to fill attributes
        Piece.Type startingType = Piece.Type.SINGLE; // setting the type as Single
        Piece.Color bottomColor;  // declaring the variable of type Piece.Color
        if (topColor == Piece.Color.WHITE){ // if top color equals WHITE
            bottomColor = Piece.Color.RED; //  update the bottom color to be RED
        }else{
            bottomColor = Piece.Color.WHITE; // else update the bottom Color to be white
        }

        //fill row with spaces and possibly pieces
        switch (index){
            case 0: //light-dark with top color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, topColor));
                break;
            case 1://dark-light with top color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, topColor),null);
                break;
            case 2: //light-dark with top color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null, new Piece(startingType, topColor));
                break;
            case 3: //dark-light with no pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, null,null);
                break;
            case 4: //light-dark with no pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK, null,null);
                break;
            case 5: //dark-light with bottom color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, bottomColor),null);
                break;
            case 6: //light-dark with bottom color pieces
                handleSpaces(Space.SPACECOLOR.LIGHT, Space.SPACECOLOR.DARK,null, new Piece(startingType, bottomColor));
                break;
            case 7: //dark-light with bottom color pieces
                handleSpaces(Space.SPACECOLOR.DARK, Space.SPACECOLOR.LIGHT, new Piece(startingType, bottomColor),null);
                break;
        }
    }

    /**
     * initialize the spaces array for this row based on the parameters given
     * @param firstSpaceColor the color of the first space in this row
     * @param secondSpaceColor the color of the second space in this row
     * @param firstPiece the first piece in this row
     * @param secondPiece the second piece in this row
     * spaces after the first two will alternate using either the
     *                    first color and piece or the second
     */
    private void handleSpaces(Space.SPACECOLOR firstSpaceColor, Space.SPACECOLOR secondSpaceColor, Piece firstPiece, Piece secondPiece) {
        for (int i = 0; i < 8; i++) { // loop
            if (i % 2 == 0) { // if even index
                spaces[i] = new Space(i, firstSpaceColor, firstPiece);
            } else { //if odd index
                spaces[i] = new Space(i, secondSpaceColor, secondPiece);
            }
        }
    }

    /**
     * replace a space in this row
     * used to update when a move occurs
     * @param cell the index to be replaced
     * @param space the space with which to replace it
     */
    public void replaceSpace(int cell, Space space){
        // spaces [cell] updated
        spaces[cell] = space;
    }

    /**
     * get a space at a given index
     * @param cell the index to get
     */
    public Space getSpace(int cell){
        // return the space[cell]
        return spaces[cell];
    }



    /**
     * @return the index of the row (0-topmost & 7 bottom most)
     */
    public int getIndex() {
        return index; // return the index
    }

    /**
     * @return return an iterator that iterates over each space in the row
     */
    @Override
    public Iterator<Space> iterator() {
        return new SpaceIterator(spaces); // return the Iterator of type Space
    }

    /**
     * @return return an iterator that iterates backwards over each space in the row
     */
    public Iterator<Space> reverseIterator(){return new ReverseSpaceIterator(spaces);}

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class SpaceIterator implements Iterator<Space> {
        //attributes needed for iteration
        private int index; // declaring the index as an integer variable
        private Space[] spaces; // declaring the Space array named spaces

        /**
         * A constructor for the iterator
         * @param spaces - the array of spaces in the row
         */
        public SpaceIterator(Space[] spaces) {
            // constructor calling
            // setting the values
            this.index = 0;  // initialising the index value as 0
            this.spaces = spaces; // setting the value of spaces
        }


        /**
         * Determines if the row has another space to iterate over
         * @return true if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return spaces.length >= index + 1; // checks if the space length is greater or equal to index or index+1-> returns boolean
        }

        /**
         * @return the next space in the iteration
         * increments the index
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index]; // setting the value of nextSpace
            index++; // incrementing the index
            return nextSpace; // returning the nextSpace of type Space
        }
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class ReverseSpaceIterator implements Iterator<Space> {
        //attributes needed for iteration
        private int index; // declaring the index as an integer variable
        private Space[] spaces; // declaring the Space array named spaces

        /**
         * A constructor for the iterator
         * @param spaces - the array of spaces in the row
         */
        public ReverseSpaceIterator(Space[] spaces) {
            // constructor calling
            // setting the values
            this.index = 7;  // initialising the index value as 0
            this.spaces = spaces; // setting the value of spaces
        }


        /**
         * Determines if the row has another space to iterate over
         * @return true if there is another space left.
         */
        @Override
        public boolean hasNext() {
            return index>=0; // checks if index is greater or equal to 0
        }

        /**
         * @return the next space in the iteration
         * increments the index
         */
        @Override
        public Space next() {
            Space nextSpace = spaces[index]; // setting the value of nextSpace
            index--; // decrementing the index
            return nextSpace; // returning the nextSpace of type Space
        }
    }
}

