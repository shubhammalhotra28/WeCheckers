package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@Tag("Model-tier")
public class RowTest {

    // creating a private CuT
    Row Cut;

    @Test
    public void testGetSpaceThenReplaceSpace(){

        /** setting the value of Cut and then using the JUNIT
         * to check if there is any case where the code is actually failing
         */

        Cut = new Row(0, Piece.Color.WHITE);
        Space space = Cut.getSpace(1);
        assertEquals(space.getPiece().getColor(), Piece.Color.WHITE);
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.DARK);
        assertEquals(space.getCellIdx(), 1);
        assertEquals(space.getPiece().getType(), Piece.Type.SINGLE);

        Cut.replaceSpace(1, new Space(1, Space.SPACECOLOR.DARK, null));
        space = Cut.getSpace(1);
        assertNull(space.getPiece());

        space = Cut.getSpace(4);
        assertNull(space.getPiece());
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.LIGHT);
        assertEquals(space.getCellIdx(), 4);

        Cut = new Row(3, Piece.Color.WHITE);
        space = Cut.getSpace(0);
        assertNull(space.getPiece());
        assertEquals(space.getCellIdx(), 0);
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.DARK);

        Cut.replaceSpace(0, new Space(1, Space.SPACECOLOR.DARK, new Piece(Piece.Type.SINGLE, Piece.Color.RED)));
        space = Cut.getSpace(0);
        assertEquals(space.getPiece().getColor(), Piece.Color.RED);

        space = Cut.getSpace(1);
        assertNull(space.getPiece());
        assertEquals(space.getCellIdx(), 1);
        assertEquals(space.getSpaceColor(), Space.SPACECOLOR.LIGHT);

    }


    @Test
    public void testGetIndex(){
        Cut = new Row(0, Piece.Color.WHITE);
        assertEquals(Cut.getIndex(), 0); // checks if equal

        Cut = new Row(6, Piece.Color.WHITE);
        assertEquals(Cut.getIndex(), 6); // checks if equal
    }
}
