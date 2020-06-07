package com.webcheckers.util;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@Tag("util-tier")
public class ValidationHelperTest {

    ValidationHelper CuT;
    BoardView boardView;

    @BeforeEach
    public void Setup(){
        boardView = new BoardView();
        CuT = new ValidationHelper();
    }

    @Test
    public void testPiecesOfColor(){
        int numPieces = CuT.piecesOfColor(boardView, Piece.Color.RED);
        assertEquals(12, numPieces);
        numPieces = CuT.piecesOfColor(boardView, Piece.Color.WHITE);
        assertEquals(12, numPieces);
    }

    @Test
    public void testScanBoard(){

        ArrayList<Move> possibleMoves = new ArrayList<>();
        ArrayList<ArrayList<Move>> possibleJumps = new ArrayList<ArrayList<Move>>(new ArrayList<>());

        CuT.scanBoard(boardView, Piece.Color.RED, possibleMoves, possibleJumps);
        assertTrue(possibleMoves.size() > 0);
        assertFalse(possibleJumps.size() > 0);

        Position start = new Position(5, 4);
        Position end = new Position(4, 3);
        Move move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.RED);

        start = new Position(4, 3);
        end = new Position(3, 4);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.RED);

        possibleMoves = new ArrayList<>();
        possibleJumps = new ArrayList<ArrayList<Move>>(new ArrayList<>());

        CuT.scanBoard(boardView, Piece.Color.WHITE, possibleMoves, possibleJumps);
        assertTrue(possibleMoves.size() > 0);
        assertTrue(possibleJumps.size() == 2);

        boardView = new BoardView();

        start = new Position(5, 2);
        end = new Position(4, 3);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.RED);

        start = new Position(4, 3);
        end = new Position(3, 4);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.RED);

        start = new Position(2, 5);
        end = new Position(4, 3);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.WHITE);

        start = new Position(4, 3);
        end = new Position(5, 2);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.WHITE);

        start = new Position(2, 3);
        end = new Position(3, 4);
        move = new Move(start, end);
        boardView.makeMove(move, Piece.Color.WHITE);


        possibleMoves = new ArrayList<>();
        possibleJumps = new ArrayList<ArrayList<Move>>(new ArrayList<>());

        CuT.scanBoard(boardView, Piece.Color.RED, possibleMoves, possibleJumps);

        assertTrue(possibleMoves.size() > 0);
        assertTrue(possibleJumps.size() == 2);
    }
}
