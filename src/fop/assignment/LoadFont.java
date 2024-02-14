package fop.assignment;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class LoadFont {

    public static Font loadCustomFont(String ttfFilePath, String fontName, int style, int size) {
        try {
            // Load the custom font from the TTF file
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(ttfFilePath));

            // Register the custom font with the graphics environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            // Derive the font with the specified style and size
            return customFont.deriveFont(style, size);
        } catch (Exception e) {
            // Print the stack trace for any exceptions during font loading
            e.printStackTrace();
            
            // Return null in case of an error
            return null;
        }
    }
}