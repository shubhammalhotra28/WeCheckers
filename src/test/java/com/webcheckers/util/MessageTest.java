package com.webcheckers.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("util-tier")
public class MessageTest {

    Message infoCuT;
    Message errorCuT;

    @BeforeEach
    public void Setup(){
         infoCuT = Message.info("Test info.");
        errorCuT = Message.error("Test error.");
    }

    @Test
    public void testgetText(){
        assertEquals(infoCuT.getText(), "Test info.");
        assertEquals(errorCuT.getText(), "Test error.");
    }

    @Test
    public void testgetType(){
        assertEquals(infoCuT.getType(), Message.Type.INFO);
        assertEquals(errorCuT.getType(), Message.Type.ERROR);
    }

    @Test
    public void testIsSucceful(){
        assertTrue(infoCuT.isSuccessful());
        assertFalse(errorCuT.isSuccessful());
    }
}
