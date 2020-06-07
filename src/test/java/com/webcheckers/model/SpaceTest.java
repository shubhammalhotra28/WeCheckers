package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A unit test for the model Space component
 */
@Tag("Model-tier")
public class SpaceTest {
    // private final attributes
    final Piece testPiece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
    final int SpaceIdx = 0;
    final Space.SPACECOLOR color = Space.SPACECOLOR.DARK;

    //the constructor tests also test all getter methods

    @Test
    public void constructorTwoArg() {
        //initialize object
        Space CuT = new Space(SpaceIdx,color); // component Under Test

        //assertions
        assertEquals(SpaceIdx,CuT.getCellIdx(),"Space Index not equal to "+Integer.toString(SpaceIdx));
        assertEquals(color,CuT.getSpaceColor(),"Space Color not equal to "+color);
        assertNull(CuT.getPiece(),"Space piece value is not null");
    }

    @Test
    public void constructorThreeArg() {
        //initialize object
        Space CuT = new Space(SpaceIdx,color,testPiece);

        //assertions
        assertEquals(SpaceIdx,CuT.getCellIdx(),"Space Index not equal to "+Integer.toString(SpaceIdx));
        assertEquals(color,CuT.getSpaceColor(),"Space Color not equal to "+color);
        assertEquals(testPiece,CuT.getPiece(),"Space piece value is not equal to test piece");
    }

    @Test
    public void isValid() {
        //initialize object with dark color and a piece present
        Space CuT = new Space(SpaceIdx, color, testPiece);

        //assert invalid because there is a piece
        assertEquals(false,CuT.isValid(),"isValid returned true but should have returned false");

        //reinitialize object with no piece but the space is light
        CuT = new Space(SpaceIdx, Space.SPACECOLOR.LIGHT);

        //assert invalid because the space is not dark
        assertEquals(false,CuT.isValid(),"isValid returned true but should have returned false");

        //reinitialize object with no piece and dark
        CuT = new Space(SpaceIdx, color);

        //assert valid because the space is dark and there is no piece
        assertEquals(true,CuT.isValid(),"isValid returned false but should have returned true");
    }
}
