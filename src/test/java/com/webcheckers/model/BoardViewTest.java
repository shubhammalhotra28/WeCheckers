package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class BoardViewTest {

    BoardView CuT; // Component Under test

    @BeforeEach
    public void Setup(){
        //instantiate the Cut
        CuT = new BoardView();
    }

    @Test
    public void getValidSpace(){
        Space space = CuT.getSpace(0, 1); // creating the variable of type Space
        assertEquals(space.getPiece().getColor(), Piece.Color.WHITE); // comparing
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.DARK); // comparing
        assertEquals(space.getCellIdx(), 1); // comparing
        assertEquals(space.getPiece().getType(), Piece.Type.SINGLE); // comparing

        space = CuT.getSpace(5, 4); // creating the variable of type Space
        assertEquals(space.getPiece().getColor(), Piece.Color.RED); // comparing
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.DARK); // comparing
        assertEquals(space.getCellIdx(), 4); // comparing
        assertEquals(space.getPiece().getType(), Piece.Type.SINGLE); // comparing

        space = CuT.getSpace(3, 3);  // creating the variable of type Space
        assertNull(space.getPiece()); // checking if NULL
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.LIGHT); // comparing
         assertEquals(space.getCellIdx(), 3); // comparing

    }

    @Test
    public void getInvalidSpace(){
        Space space  = CuT.getSpace(8, 19); // getting the space through the method
        assertNull(space); //passed parameter should be NULL
        space = CuT.getSpace(-5, 20); // getting the space through the method
        assertNull(space); //passed parameter should be NULL
        space = CuT.getSpace(7, -1); // getting the space through the method
        assertNull(space); //passed parameter should be NULL
    }

    @Test
    public void makeMoves(){
        Space spaceStart = CuT.getSpace(5, 4);  // setting the spaceStart with ref to CuT
        assertEquals(spaceStart.getPiece().getColor(), Piece.Color.RED); // check if the values are equal (parameters)
        Space spaceEnd = CuT.getSpace(4, 3);  // setting the spaceStart with ref to CuT
        assertNull(spaceEnd.getPiece()); // passed parameter should be NULl

        Position start = new Position(5, 4); // creating a new variable of typer Position
        Position end = new Position(4, 3); // creating a new variable of type Position
        Move move = new Move(start, end); // creating a new variable of type Move
        CuT.makeMove(move, Piece.Color.RED); // using CuT to reference makeMove while passing the parameters as move and color

        spaceStart = CuT.getSpace(5, 4);  // setting the spaceStart with ref to CuT
        assertNull(spaceStart.getPiece()); // passed parameter should be NULL
        spaceEnd = CuT.getSpace(4, 3); // setting the spaceEnd with ref to CuT
        assertEquals(spaceEnd.getPiece().getColor(), Piece.Color.RED); // checks if equal
        ///nextmove
        spaceStart = CuT.getSpace(4, 3); //// setting the spaceStart with ref to CuT
        spaceEnd = CuT.getSpace(3, 4); // setting the spaceEnd with ref to CuT
        assertEquals(spaceStart.getPiece().getColor(), Piece.Color.RED); // checks if equal
        assertNull(spaceEnd.getPiece()); // passed parameter should be NULL

        start = new Position(4, 3); // setting the variable start
        end = new Position(3, 4); // setting the variable end
        move = new Move(start, end); // setting the variable move
        CuT.makeMove(move, Piece.Color.RED); // calling the makeMove method with ref to CuT while passing the move and color as parameter

        spaceStart = CuT.getSpace(4, 3); // setting the variable spaceStart
        spaceEnd = CuT.getSpace(3, 4); // setting the variable spaceEnd
        assertNull(spaceStart.getPiece()); // setting the variable move
        assertEquals(spaceEnd.getPiece().getColor(), Piece.Color.RED); // calling the makeMove method with ref to CuT while passing the color and getting the color

        //make final move as a jump white over red
        spaceStart = CuT.getSpace(2, 5); // setting the variable spaceStart
        Space spaceMiddle = CuT.getSpace(3, 4);  // settting the variable spaceEnd
        spaceEnd = CuT.getSpace(4, 3); // setting the variable spaceEnd
        assertEquals(spaceStart.getPiece().getColor(), Piece.Color.WHITE); // checks if equal
        assertEquals(spaceMiddle.getPiece().getColor(), Piece.Color.RED); // checks if equal
        assertNull(spaceEnd.getPiece()); // passed parameter should be NULL

        start = new Position(2, 5); // setting the start
        end = new Position(4, 3); // setting the end
        move = new Move(start, end); // setting the move
        CuT.makeMove(move, Piece.Color.WHITE); // calling makeMove methode with ref to CuT

        spaceStart = CuT.getSpace(2, 5); // setting the spaceStart
        spaceMiddle = CuT.getSpace(3, 4); // setting the spaceMiddle
        spaceEnd = CuT.getSpace(4, 3); // setting the spaceEnd
        assertNull(spaceStart.getPiece()); // passed parameter should be NULL
        assertNull(spaceMiddle.getPiece()); // passed parameter should be NULL
        assertEquals(spaceEnd.getPiece().getColor(), Piece.Color.WHITE); // checks if equals
    }
}
