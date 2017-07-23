package com.valkryst.VTerminal.AsciiStringTest;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class SetHiddenTest {
    private AsciiString string;

    @Before
    public void initializeString() {
        string = new AsciiString("ABCDEFGHJIKLMNOP");
        string.setBackgroundColor(Color.BLACK);
        string.setForegroundColor(Color.WHITE);
    }

    @Test
    public void toAllCharacters_setHidden() {
        string.setHidden(true);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertTrue(character.isHidden());
        }
    }

    @Test
    public void toAllCharacters_setVisible() {
        string.setHidden(true);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertTrue(character.isHidden());
        }

        string.setHidden(false);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertFalse(character.isHidden());
        }
    }
}
