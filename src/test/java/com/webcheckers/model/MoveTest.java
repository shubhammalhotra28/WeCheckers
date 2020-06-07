package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class MoveTest {
    // attributes
    private Position start, end;
    private Position start2, end2;
    private Move CuT; // component under test

    @BeforeEach
    public void setup() {
        // setting it up
        start = mock(Position.class);
        end = mock(Position.class);
        start2 = mock(Position.class);
        end2 = mock(Position.class);

        when(start.getCell()).thenReturn(0);
        when(start.getRow()).thenReturn(2);

        when(end.getCell()).thenReturn(1);
        when(end.getRow()).thenReturn(3);

        when(start2.getCell()).thenReturn(7);
        when(start2.getRow()).thenReturn(7);

        when(end2.getCell()).thenReturn(7);
        when(end2.getRow()).thenReturn(7);

        CuT = new Move(start,end); // action by CuT -> performed
    }

    @Test
    public void dataAttributes() {
        //verify that the start and end data attributes are correct
        assertEquals(start,CuT.getStart());
        assertEquals(end,CuT.getEnd());
    }

    @Test
    public void equalsWithInvalidOther() {
        //invoke test
        //use Object so that it will not be instanceof Move
        boolean equals = CuT.equals(new Object());
        //verify result is false
        assertFalse(equals);
    }

    @Test
    public void equalsWithDifferentStarts() {
        //invoke test
        boolean equals = CuT.equals(new Move(start2,end));
        //verify result is false
        assertFalse(equals);
    }

    @Test
    public void equalsWithDifferentEnds() {
        //invoke test
        boolean equals = CuT.equals(new Move(start,end2));
        //verify result is false
        assertFalse(equals);
    }

    @Test
    public void equals() {
        //invoke test
        boolean equals = CuT.equals(new Move(start,end));
        //verify result is true
        assertTrue(equals);
    }

    @Test
    public void tostring() {
        String result = CuT.toString();
        assertEquals(result,"Start(Row: 2 Cell: 0) End(Row: 3 Cell: 1)");
    }

    @Test
    public void altStringRed() {
        String result = CuT.altString(Piece.Color.RED);
        assertEquals(result, "(5,0) to (4,1)");
    }

    @Test
    public void altStringWhite() {
        String result = CuT.altString(Piece.Color.WHITE);
        assertEquals(result,"(2,7) to (3,6)");
    }
}
