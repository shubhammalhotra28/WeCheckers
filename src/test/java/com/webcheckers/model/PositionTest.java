package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
class PositionTest {

    // private attributes
    private Position validPosition;
    private Position invalidPosition;

    @BeforeEach
    void setUp() {
        // setting it up
        validPosition = new Position(2,0);
        invalidPosition = new Position(9,10);

    }

    @Test
    void getRow()
    {
        assertEquals(2, validPosition.getRow());
        assertEquals(9, invalidPosition.getRow());
    }

    @Test
    void getCell()
    {
        assertEquals(0, validPosition.getCell());
        assertEquals(10, invalidPosition.getCell());
    }


    @Test
    void testEquals() {
        assertNotEquals(validPosition, invalidPosition);
        Position validPosition2 = new Position(2, 0);
        Position invalidPosition2 = new Position(9,10);
        assertEquals(validPosition, validPosition2);
        assertEquals(invalidPosition, invalidPosition2);
    }

    @Test
    public void tostring() {
        String result = validPosition.toString();
        assertEquals(result, "(2,0)");
    }



}