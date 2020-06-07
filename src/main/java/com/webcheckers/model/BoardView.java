package com.webcheckers.model;
import java.util.Iterator;


/**
 * stores all of the rows and cells on the board
 * implemented as an array of rows
 */
public class BoardView implements Iterable<Row>{
    //attributes
    private Row[] rows; // initialising the array of Row



    /**
     * The constructor for the board view class
     */
    public BoardView(){
        Piece.Color topColor = Piece.Color.WHITE; // setting the topcolor to be white
        rows = new Row [8]; // initialising the array of row with 8 size
        for(int i=0; i<8; i++){
            // loop
            rows[i] = new Row(i, topColor); // updating
        }
    }

    /**
     * update the board with a move
     * if there is a jump, remove the jumped piece
     * @param move the move that is being made
     * @param activeColor
     */
    public void makeMove(Move move, Piece.Color activeColor){
        Position start = move.getStart(); // return start of move and update
        Position end = move.getEnd(); // return end of move and update
        Space startSpace = getSpace(start.getRow(), start.getCell());
        Piece startPiece = startSpace.getPiece();

        //set the start space to an empty square
        rows[start.getRow()].replaceSpace(start.getCell(), new Space(start.getCell(), Space.SPACECOLOR.DARK, null));

        //if the end piece is a single piece and is becomming a king
        if (startPiece.getType() == Piece.Type.SINGLE && (end.getRow() == 0 || end.getRow() == 7)){
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else if (startPiece.getType() == Piece.Type.KING){
            //if a piece is already a king piece
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.KING, activeColor)));
        }else{
            //the piece stays a single
            rows[end.getRow()].replaceSpace(end.getCell(), new Space(end.getCell(), Space.SPACECOLOR.DARK, new Piece(Piece.Type.SINGLE, activeColor)));
        }

        //add the logic to remove the piece in the middle
        if (Math.abs(start.getRow() - end.getRow()) == 2){ // if the diff is  2 _> absolute value
            int centerRow = (start.getRow() + end.getRow())/2; // calculating the center row
            int centerCol = (start.getCell() + end.getCell())/2; // calculating the center column
            rows[centerRow].replaceSpace(centerCol, new Space(centerCol, Space.SPACECOLOR.DARK, null)); // updating
        }
    }

    /**
     * get a space at a given row, col coordinates
     */
    public Space getSpace(int row, int col){
        // method -> returning Space type
        if (0 <= row && row <= 7 && 0<=col &&col<=7){
            return rows[row].getSpace(col); // return the space
        }
        return null; // return null
    }


    /**
     * @return return an iterator that iterates over each row on the board
     */
    @Override
    public Iterator<Row> iterator(){
        return new RowIterator(rows); // returning the row iterator
    }

    /**
     * @return return an iterator that iterates backwards over each row on the board
     */
    public Iterator<Row> reverseIterator(){
        return new ReverseRowIterator(rows); // returning the row iterator
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class RowIterator implements Iterator<Row>{
        // class implementing the interface -> Iterator

        // declaring the variables
        private  int index;  // declaring an integer for index
        private Row[] rows; // declaring the array of row

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public RowIterator(Row[] rows){
            // constructor calling
            // initialising the values
            this.index = 0;  // setting the value of index as 0
            this.rows = rows; // setting the value of rows array
        }

        /**
         * Determines if the board has another row to iterate over
         * @return true if there is another row left.
         */
        @Override
        public boolean hasNext(){
            return rows.length >= index +1; // checks if the length is greater or equal to index value or index+1 value
        }

        /**
         * @return the next row in the iteration
         * increments the index
         */
        @Override
        public Row next(){
            Row nextRow = rows[index]; // declaring and initialising the value for variable nextRow
            index++; // incrementing the index
            return nextRow; // returning the variable
        }
    }

    /**
     * A class to define how to iterate over an array of spaces.
     */
    public class ReverseRowIterator implements Iterator<Row>{
        // class implementing the interface -> Iterator

        // declaring the variables
        private  int index;  // declaring an integer for index
        private Row[] rows; // declaring the array of row

        /**
         * A constructor for the iterator
         * @param rows - the array of rows on the board
         */
        public ReverseRowIterator(Row[] rows){
            // constructor calling
            // initialising the values
            this.index = 7;  // setting the value of index as 0
            this.rows = rows; // setting the value of rows array
        }

        /**
         * Determines if the board has another row to iterate over
         * @return true if there is another row left.
         */
        @Override
        public boolean hasNext(){
            return this.index >= 0; // checks if index is greater or equal to 0 and returns boolean
        }

        /**
         * @return the next row in the iteration
         * increments the index
         */
        @Override
        public Row next(){
            Row nextRow = rows[index]; // declaring and initialising the value for variable nextRow
            index--; // decrementing the index
            return nextRow; // returning the variable nextRow
        }
    }

}
