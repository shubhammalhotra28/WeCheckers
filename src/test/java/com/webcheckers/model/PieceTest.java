package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A unit test for the model Piece component
 */


@Tag("Model-tier")
class PieceTest {
    // initiating the object
    final Piece CuT = new Piece(Piece.Type.SINGLE, Piece.Color.RED);

    // test for getType()
    @Test
    public void getType(){

        assertEquals(Piece.Type.SINGLE,CuT.getType()); // checks if equal

    }

    // test for getType()
    @Test
    public void getType_1()
    {
        assertNotEquals(Piece.Type.KING,CuT.getType());
    }

    // test for getColor()
    @Test
    public void getColor()
    {
        assertEquals(Piece.Color.RED,CuT.getColor());
    }

    // test for getColor()
    @Test
    public void getColor_1(){

        assertNotEquals(Piece.Color.WHITE,CuT.getColor());
    }




}