package fop.assignment;

import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPasswordField;

public class RoundJPasswordField extends JPasswordField {

    // Shape to define the rounded rectangle
    private Shape shape;

    // Constructor that sets the size and makes the component opaque
    public RoundJPasswordField(int size) {
        super(size);
        setOpaque(false);
    }

    // Custom painting method to draw a rounded rectangle as the background
    @Override
    protected void paintComponent(Graphics g) {
        // Set the color to the background color
        g.setColor(getBackground());
        // Draw a filled rounded rectangle
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        // Call the superclass paintComponent method to render the text
        super.paintComponent(g);
    }

    // Override the contains method to define the shape of the component
    @Override
    public boolean contains(int x, int y) {
        // If the shape is null or its bounds are different from the current bounds
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            // Create a new rounded rectangle shape based on the component's bounds
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
        // Check if the specified point (x, y) is within the shape
        return shape.contains(x, y);
    }
}

