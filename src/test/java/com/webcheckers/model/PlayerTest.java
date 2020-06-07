package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * A unit test for the model Player component
 */
@Tag("Model-tier")
public class PlayerTest {

    //private static final attributes
    private static final String NAME = "Joe";
    private static final String NAME2 = "Bob";

    //test getName
    @Test
    public void getName(){
        final Player CuT = new Player(NAME);
        assertEquals(NAME, CuT.getName(), "Name not equal to " + NAME);
    }

    //test equals
    @Test
    public void equals(){
        final Player CuT1 = new Player(NAME);
        final Player CuT2 = new Player(NAME);
        final Player CuT3 = new Player(NAME2);
        assertTrue(CuT1.equals(CuT1));
        assertTrue(CuT1.equals(CuT2));
        assertFalse(CuT1.equals(CuT3));
    }

    //test hashcode
    @Test
    public void test_hashCode(){
        final Player CuT = new Player(NAME);
        assertEquals(NAME.hashCode(), CuT.hashCode());
    }
}
