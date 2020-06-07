package com.webcheckers.model;

import com.webcheckers.util.ValidationHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes a game
 * stores board and other data for the game instance
 */
public class Game {

    //attributes
    private String redPlayerName; // declaring global private string redPlayerName
    private String whitePlayerName; // declaring global private string whitePlayerName
    private Piece.Color activeColor; // declaring glocbal variabl;e for active color
    private ArrayList<Move> moveSequence; // declaring global variable of type Move
    private int movesMade; // declaring a private boolean at global to check move
    private BoardView board; // declaring private variable board of type BoardView
    private boolean gameOver; // declaring global attribute of gameOver
    private String gameEndMsg; // declaring global private string gameEndMsg
    private String winner; // declaring global private string winner
    private ValidationHelper helper;

    /**
     * constructor
     */
    public Game(BoardView board, String redPlayerName, String whitePlayerName, Piece.Color activeColor, ValidationHelper helper){
        this.board = board; // setting the value of board
        this.redPlayerName = redPlayerName; // setting the value of redPlayerName
        this.whitePlayerName = whitePlayerName; // setting the value of whitePlayerName
        this.activeColor = activeColor; // setting the value of activeColor
        this.moveSequence = null; // setting the value of secondMoveToMake to NULL
        this.movesMade = 0;  // setting the value of secondMoved to be false
        this.gameOver = false; // setting the value of gameOver to be false
        this.gameEndMsg = null; // setting the value of gameEndMsg to NULL
        this.winner = null; // setting the value of winner to NULL
        this.helper = helper; // setting the value of helper variable for validation help
    }

    /**
     * getter method for red player name
     */
    public String getRedPlayerName() {
        // return red player name
        return redPlayerName;
    }

    /**
     * getter method for white player name
     */
    public String getWhitePlayerName() {
        // return white player name
        return whitePlayerName;
    }

    /**
     * getter method for active color
     */
    public Piece.Color getActiveColor() {
        // return the active piece color
        return activeColor;
    }

    /**
    method is checking if the active person player and the active color
    is same then returns true value to the boolean type function
    otherwise false

    */
    public boolean amIActive(Player player){

        String name = player.getName(); // getting the player name
        if(name.equals(redPlayerName) && activeColor == Piece.Color.RED){ // check if player is red and active color is also red
            return true; // return true
        }else if(name.equals(whitePlayerName) && activeColor == Piece.Color.WHITE){ // check if the player is white and active color is white
            return true; // return true
        }
        return false; // else return false
    }


    /**
     * getter method for second pending move to be made
     */
    public ArrayList<Move> getMoveSequence() {
        // return secodMoveToMake of type -> Move
        return moveSequence;
    }

    public void setMoveSequence(ArrayList<Move> moveSequence) {
        this.moveSequence = moveSequence;
    }


    /**
        Returning the boolean value
        of the secondMoved
     */
    public boolean allMovesMade() {
        if (moveSequence != null){
            return moveSequence.size() == movesMade;
        }else{
            return false;
        }
    }

    public Move getNextMoveToMake(){
        if (movesMade == moveSequence.size()){
            return null;
        }
        return moveSequence.get(movesMade);
    }

    public Move getLastMoveToMake(){
        return moveSequence.get(moveSequence.size()-1);
    }

    public void incrementMovesMade(){
        movesMade += 1;
    }



    /**
     * clear the move that is pending to be made
     */
    public void clearMoves(){
        if (movesMade > 0){
            if (movesMade == 1){
                moveSequence = null;
            }
            movesMade -= 1;
        }
    }

    /**
     * make the pending move(s)
     */
    public void MakeMoves(){
        if (moveSequence != null){
            for (Move move : moveSequence){
                System.out.println("Making the move: " + move);
                board.makeMove(move, activeColor); //make all the moves on the board
            }
        }

        moveSequence = null;
        movesMade = 0; //clear the number of moves made

        //change the active color and check for game ending condition
        checkEndGame();
        changeActiveColor();
    }

    /**
     * toggle the active color
     * if it's red change it to white
     * if it's white change it to red
     */
    public void changeActiveColor(){
        //change the active color piece
        if (activeColor == Piece.Color.RED){ // check -> if active color is Red
            activeColor = Piece.Color.WHITE; // update it to white
        }else{
            activeColor = Piece.Color.RED; //else ->  update it to red
        }
    }

    /**
     * check for any of the end game conditions
     * if any condition is true then update variables
     * to indicate that the game is over and give the reason
     */
    public void checkEndGame()  {
        //check if red has at least one piece on the board
        int redPieces = helper.piecesOfColor(board,Piece.Color.RED);
        if (redPieces == 0) {
            //red has no pieces remaining and has lost the game
            gameOver = true; // setting the gameOver as true
            gameEndMsg = redPlayerName + " ran out of pieces!"; // concatinating and updating the string
            winner = whitePlayerName; // setting the winner
            return; // return
        }
        //check if white has at least one piece on the board
        int whitePieces = helper.piecesOfColor(board,Piece.Color.WHITE);
        if (whitePieces == 0) {
            //white has no pieces remaining and has lost the game
            gameOver = true; // setting the gameOver value to be true
            gameEndMsg = whitePlayerName + " ran out of pieces!"; // concatenating and updating the string
            winner = redPlayerName; // setting up the winner
            return; // return
        }
        //check if the player who is about to take their turn has
        //at least one move available
        Piece.Color inactiveColor;
        if (activeColor == Piece.Color.RED) inactiveColor = Piece.Color.WHITE; // setting the inactive color to be White
        else inactiveColor = Piece.Color.RED; // else->setting the color to be Red
        ArrayList<Move> possibleMoves = new ArrayList<>(); // initialization of arrayList
        //next two vars not used per say but needed as parameters for method
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>(); // initialization of arrayList
        helper.scanBoard(board,inactiveColor,possibleMoves, possibleJumps);
        if (possibleMoves.size()==0 && possibleJumps.size() == 0) {
            //this player has been blocked and has lost the game
            gameOver=true; // set the value og gameOver to be true
            if (inactiveColor == Piece.Color.WHITE) { // if the inactive color is white
                gameEndMsg = whitePlayerName + " has been blocked!"; // concatenating and updating the string
                winner = redPlayerName; // setting up the winner
            } else {
                gameEndMsg = redPlayerName + " has been blocked!"; // concatenating and updating the string
                winner = whitePlayerName; // setting up the winner
            }
            return; // return
        }
    }

    /**
     * getter method for the board object
     * @return
     */
    public BoardView getBoard() {
        return board; // return board
    }

    /**
        getter method for the opponent name
     */
    public String getOpponentTo(String name) {
        if (name.equals(redPlayerName)) return whitePlayerName;
        else return redPlayerName;
    }

    /**
     * invoked when a player resigns from the game
     * update variable to indicate that the game is
     * over and give the reason
     * @param name the name of the player resigning
     */
    public void resign(String name) {
        gameOver = true; // set the gameOver to be true
        gameEndMsg = name + " resigned."; // update the end message with resigned as being concatenated
        winner = getOpponentTo(name); // setting up the winner
    }

    /**
     * is the game in the empty state
     * @return if the game is in the empty state (if there is no move to make)
     */
    public boolean emptyState() {
        return moveSequence==null; // return boolean value if moveToMake==null
    }

    /**
     * getter method for if the game is over
     */
    public boolean gameOver() {
        return gameOver; // return gameOver
    }

    /**
     * getter method for the winner of the game
     */
    public String getWinner() {
        return winner; // return the winner
    }

    /**
     * getter method for the endgame message
     */
    public String getGameEndMsg() {
        // return the eng game message
        return gameEndMsg;
    }

    /**
     * generate a message to tell the user how many pieces
     * the active color has on the board
     * @return the string message
     */
    public String generatePiecesMsg() {
        int pieces = helper.piecesOfColor(board,activeColor);
        return "You have " + Integer.toString(pieces) + " pieces remaining.";
    }

    /**
     * generate a list of strings describing the possible moves
     * that the active player may make
     * @return a list of strings each representing a move
     */
    public List<String> generateMovesList() {
        ArrayList<Move> possibleMoves = new ArrayList<>(); // initialization of arrayList
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<>(); // initialization of arrayList
        //scan the board
        helper.scanBoard(board,activeColor,possibleMoves,possibleJumps);
        List<String> movesList = new ArrayList<String>();
        if (possibleJumps.size() > 0) { //if there are single jumps to be made
            //add each single jump to the moves list
            for (ArrayList<Move> moveSequence : possibleJumps){
                String sequenceString = "";
                for (int i=0; i<moveSequence.size(); i++) {
                    sequenceString += "Jump " + (i+1) + " :  " + moveSequence.get(i).altString(activeColor);
                    if (i < moveSequence.size()-1) {
                        //add the divider if and only if there is an addional jump after this one
                        sequenceString += " | ";
                    }
                }
                movesList.add(sequenceString);}

        } else { //if there are only regular moves available
            //add each move to the moves list
            if (activeColor == Piece.Color.WHITE) {
                for (Move move : possibleMoves) {
                    movesList.add(move.altString(activeColor));}
            } else {
                //if the active color is red then the rows had to be inverted
                //due to board flip - this means that by default, moves is in
                //reverse row major order: so iterate backward to fix it
                for (int i = possibleMoves.size()-1; i >= 0; i--) {
                    Move thisMove = possibleMoves.get(i);
                    movesList.add(thisMove.altString(activeColor));
                }
            }
        }

        return movesList;
    }
}
