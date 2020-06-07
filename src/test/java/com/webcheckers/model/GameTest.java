package com.webcheckers.model;

import com.webcheckers.ui.WebServer;
import com.webcheckers.util.ValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * A unit test for the model Piece component
 */


@Tag("Model-tier")
public class GameTest {
    Game CuT; // component Under Test


    private BoardView board; // declaring priavte boardview type board
    private Player redPlayer, whitePlayer; // declaring private Player
    private Move move; // declaring private attrinbute of type Move
    private ArrayList<Move> moveSequence;
    private ValidationHelper helper; // declaring the variable as private of type ValidationHelper

    final private String redPlayerName = "Bob"; // declaring and intialising the string
    final private String whitePlayerName = "Tom"; // declaring and intialising the string
    final private Piece.Color defaultActiveColor = Piece.Color.RED; // setting the default color to be RED

    @BeforeEach
    public void setup() {
        // setup
        // setting the attributes declared above
        board = mock(BoardView.class);
        redPlayer = mock(Player.class);
        whitePlayer = mock(Player.class);
        move = mock(Move.class);
        moveSequence = new ArrayList<>();
        moveSequence.add(move);
        helper = mock(ValidationHelper.class);

        when(redPlayer.getName()).thenReturn(redPlayerName);
        when(whitePlayer.getName()).thenReturn(whitePlayerName);
    }

    @Test
    public void constructor() {
        //invoke test
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);

        //verify all values have been initialized/assigned correctly
        assertEquals(board,CuT.getBoard()); // checks if equal
        assertEquals(redPlayerName,CuT.getRedPlayerName()); // checks if equal
        assertEquals(whitePlayerName,CuT.getWhitePlayerName()); // checks if equal
        assertEquals(defaultActiveColor,CuT.getActiveColor()); // checks if equal
        assertNull(CuT.getMoveSequence());  //passed parameter should be NULL
        assertNull(CuT.getWinner()); //passed parameter should be NULL
        assertNull(CuT.getGameEndMsg()); //passed parameter should be NULL
        assertFalse(CuT.gameOver());  // should return False
    }

    @Test
    public void amIActive(){
        //check first with the default active color that the
        //red player name is active and the white player name is inactive
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        //verify red is active player
        assertEquals(Piece.Color.RED,CuT.getActiveColor());
        assertTrue(CuT.amIActive(redPlayer));
        assertFalse(CuT.amIActive(whitePlayer));

        //change the active color from red to white
        CuT = new Game(board,redPlayerName,whitePlayerName,Piece.Color.WHITE, helper);
        //verify white is active player
        assertEquals(Piece.Color.WHITE,CuT.getActiveColor());
        //verify red is inactive and white is active
        assertFalse(CuT.amIActive(redPlayer));
        assertTrue(CuT.amIActive(whitePlayer));

    }

    @Test
    public void changeActiveColor() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        //verify red is active player
        assertEquals(Piece.Color.RED,CuT.getActiveColor());
        //change active color to white and verify change was correct
        CuT.changeActiveColor();
        assertEquals(Piece.Color.WHITE,CuT.getActiveColor());
        //change back to red and verify change was correct
        CuT.changeActiveColor();
        assertEquals(Piece.Color.RED,CuT.getActiveColor());
    }

    @Test
    public void moveToMake() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        CuT.setMoveSequence(moveSequence);
        assertEquals(moveSequence, CuT.getMoveSequence()); // checks if equal
    }



    @Test
    public void allMovesMade() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        CuT.setMoveSequence(moveSequence);
        CuT.incrementMovesMade();
        assertTrue(CuT.allMovesMade()); // return true
    }



    @Test
    public void getOpponentTo() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        //check that white player is the opponent of red player
        assertEquals(whitePlayerName,CuT.getOpponentTo(redPlayerName));
        //check that the red player is the opponent of white player
        assertEquals(redPlayerName,CuT.getOpponentTo(whitePlayerName));
    }

//    @Test
//    public void emptyState() {
//        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
//        //verify that when move is null game reports in the empty state
//        assertNull(CuT.getMoveToMake());
//        assertTrue(CuT.emptyState());
//        //verify that when move is define game reports not in the empty state
//        CuT.setMoveToMake(move);
//        assertNotNull(CuT.getMoveToMake());
//        assertFalse(CuT.emptyState());
//    }

    @Test
    public void redResigns() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        //make red resign
        CuT.resign(redPlayerName);
        //verify data after resignation occurs
        assertTrue(CuT.gameOver());
        assertEquals(redPlayerName + " resigned.",CuT.getGameEndMsg());
        assertEquals(whitePlayerName,CuT.getWinner());
    }

    @Test
    public void whiteResigns() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        //make white resign
        CuT.resign(whitePlayerName);
        //verify data after resignation occurs
        assertTrue(CuT.gameOver());
        assertEquals(whitePlayerName + " resigned.",CuT.getGameEndMsg());
        assertEquals(redPlayerName,CuT.getWinner());
    }

    @Test
    public void makeMoveInvalid() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        assertNull(CuT.getMoveSequence());
        //invoke test
        CuT.MakeMoves();
        //verify that nothing was changed on board
        verify(board, times(0)).makeMove(any(Move.class),any(Piece.Color.class));
    }

    @Test
    public void makeOneMove() {
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        CuT.setMoveSequence(moveSequence);
        assertNotNull(CuT.getMoveSequence());
        //invoke test
        CuT.MakeMoves();
        //verify that the move was sent to the board and that the board.makeMove was only called the one time
        verify(board, times(1)).makeMove(move,defaultActiveColor);
        verify(board, times(1)).makeMove(any(Move.class),any(Piece.Color.class));
        //verify that moveToMake was cleared upon making the move
        assertNull(CuT.getMoveSequence());
    }

//    @Test
//    public void makeTwoMoves() {
//        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
//        Move secondMove = mock(Move.class);
//        CuT.setMoveToMake(move);
//        CuT.setSecondMoveToMake(secondMove);
//        CuT.setSecondMovedTrue();
//        assertNotNull(CuT.getMoveToMake());
//        assertNotNull(CuT.getSecondMoveToMake());
//        assertTrue(CuT.isSecondMoved());
//        //invoke test
//        CuT.MakeMoves();
//        //verify that both moves were sent to the board - board.makeMove should have thus been called twice
//        verify(board, times(1)).makeMove(move,defaultActiveColor);
//        verify(board, times(1)).makeMove(secondMove,defaultActiveColor);
//        verify(board, times(2)).makeMove(any(Move.class),any(Piece.Color.class));
//        //verify that both move variables were cleared and that secondMove boolean is set to false
//        assertNull(CuT.getMoveToMake());
//        assertNull(CuT.getSecondMoveToMake());
//        assertFalse(CuT.isSecondMoved());
//    }

    @Test
    public void redOutOfPieces() {
        //setup
        //red has zero pieces remaining
        when(helper.piecesOfColor(board,Piece.Color.RED)).thenReturn(0);
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);

        //invoke test
        CuT.checkEndGame();

        //assert data for red to lose game
        assertTrue(CuT.gameOver());
        assertEquals(redPlayerName + " ran out of pieces!",CuT.getGameEndMsg());
        assertEquals(whitePlayerName,CuT.getWinner());
    }

    @Test
    public void whiteOutOfPieces() {
        //setup
        //red has pieces remaining, but white has zero pieces remaining
        when(helper.piecesOfColor(board,Piece.Color.RED)).thenReturn(1);
        when(helper.piecesOfColor(board,Piece.Color.WHITE)).thenReturn(0);
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);

        //invoke test
        CuT.checkEndGame();

        //assert data for white to lose game
        assertTrue(CuT.gameOver());
        assertEquals(whitePlayerName + " ran out of pieces!",CuT.getGameEndMsg());
        assertEquals(redPlayerName,CuT.getWinner());
    }

    @Test
    public void blocked() {
        //setup
        //red and white both have pieces remaining
        when(helper.piecesOfColor(board,Piece.Color.RED)).thenReturn(1);
        when(helper.piecesOfColor(board,Piece.Color.WHITE)).thenReturn(1);
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);

        CuT.checkEndGame();
        assertTrue(CuT.gameOver());
    }

    @Test
    public void TestClearMoves(){
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        CuT.setMoveSequence(new ArrayList<>());
        CuT.incrementMovesMade();
        CuT.clearMoves();
        assertNull(CuT.getMoveSequence());
    }
//    @Test
//    void getActiveColor() {
//        assertEquals(Piece.Color.RED, CuT.getActiveColor());
//        assertNotEquals(Piece.Color.WHITE, CuT.getActiveColor());
//        CuT.changeActiveColor();
//        assertEquals(Piece.Color.WHITE, CuT.getActiveColor());
//        assertNotEquals(Piece.Color.RED, CuT.getActiveColor());
//    }

    @Test
    public void testNextandLastMoves(){
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor, helper);
        ArrayList<Move> moves = new ArrayList<>();
        Move move1=new Move(new Position(5, 5), new Position(4, 4));
        Move move2 =new Move(new Position(5, 5), new Position(4, 6));
        moves.add(move1);
        moves.add(move2);
        CuT.setMoveSequence(moves);
        assertEquals(CuT.getNextMoveToMake(), move1);
        assertEquals(CuT.getLastMoveToMake(), move2);
        CuT.incrementMovesMade();
        assertEquals(CuT.getNextMoveToMake(), move2);
        CuT.incrementMovesMade();
        assertNull(CuT.getNextMoveToMake());
    }
    @Test
    public void piecesMessage() {
        when(helper.piecesOfColor(board, defaultActiveColor)).thenReturn(2);
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor,helper);

        String result = CuT.generatePiecesMsg();
        assertEquals(result, "You have 2 pieces remaining.");
    }

    @Test
    public void generateMoveList(){
        helper = new ValidationHelper();
        board = new BoardView();
        CuT = new Game(board,redPlayerName,whitePlayerName,defaultActiveColor,helper);
        List<String> moveStrings = CuT.generateMovesList();
        //intitially there should only be normal forward moves
        assertEquals(7, moveStrings.size());
        CuT.MakeMoves();
        assertEquals(7, CuT.generateMovesList().size());
    }

}
