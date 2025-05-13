package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class JRButton extends JButton {

    private int borderRadius = 0; // Default to a standard rectangular button
    private Color borderColor = UIManager.getColor("Button.borderColor"); // Default to L&F border color or black
    private float borderThickness = 1f; // Default border thickness

    private Color originalBackgroundColor;
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    // Constructors
    public JRButton() {
        super();
        init();
    }

    public JRButton(String text) {
        super(text);
        init();
    }

    public JRButton(Action a) {
        super(a);
        init();
    }

    public JRButton(Icon icon) {
        super(icon);
        init();
    }

    public JRButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    private void init() {
        // Make the button's default background transparent and remove default border painting.
        // We will handle all painting ourselves.
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false); // Optionally disable focus painting if it interferes
        setOpaque(false); // Ensure transparency for rounded corners

        // Store original background for hover/pressed effects
        originalBackgroundColor = getBackground();
        if (originalBackgroundColor == null) {
            originalBackgroundColor = UIManager.getColor("Button.background");
        }
        if (borderColor == null) {
            borderColor = Color.BLACK; // Fallback if L&F doesn't provide one
        }

        // Set default hover and pressed colors (can be customized further)
        if (originalBackgroundColor != null) {
            hoverBackgroundColor = originalBackgroundColor.brighter();
            pressedBackgroundColor = originalBackgroundColor.darker();
        } else {
            hoverBackgroundColor = Color.LIGHT_GRAY;
            pressedBackgroundColor = Color.GRAY;
        }


        // Mouse listener for hover and press effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = false;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    isPressed = false;
                    repaint();
                }
            }
        });
    }

    // --- Custom Property Getters and Setters ---

    public int getBorderRadius() {
        return borderRadius;
    }

    /**
     * Sets the border radius for the corners.
     * A value of 0 means sharp corners (standard rectangle).
     * Larger values create more rounded corners.
     * To make it a circle, set this to roughly half the component's height/width.
     *
     * @param borderRadius The radius for the corner arcs.
     */
    public void setBorderRadius(int borderRadius) {
        if (borderRadius < 0) {
            this.borderRadius = 0;
        } else {
            this.borderRadius = borderRadius;
        }
        repaint(); // Redraw the button when the radius changes
    }

    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the color of the button's border.
     *
     * @param borderColor The color for the border.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); // Redraw the button when the border color changes
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    /**
     * Sets the thickness of the button's border.
     *
     * @param borderThickness The thickness for the border (e.g., 1f, 2f).
     */
    public void setBorderThickness(float borderThickness) {
        if (borderThickness < 0) {
            this.borderThickness = 0f;
        } else {
            this.borderThickness = borderThickness;
        }
        repaint(); // Redraw the button when the border thickness changes
    }

    // --- Custom Getters/Setters for Hover and Pressed Colors ---
    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.originalBackgroundColor = bg;
        // Update hover and pressed colors based on the new background if they haven't been explicitly set
        if (this.hoverBackgroundColor == null || this.hoverBackgroundColor.equals(bg.brighter())) {
            this.hoverBackgroundColor = (bg != null) ? bg.brighter() : Color.LIGHT_GRAY;
        }
        if (this.pressedBackgroundColor == null || this.pressedBackgroundColor.equals(bg.darker())) {
            this.pressedBackgroundColor = (bg != null) ? bg.darker() : Color.GRAY;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int actualBorderRadius = Math.min(borderRadius, Math.min(width, height)); // Cap radius

        // 1. Paint Background
        if (isEnabled()) {
            if (isPressed && pressedBackgroundColor != null) {
                g2.setColor(pressedBackgroundColor);
            } else if (isHovered && hoverBackgroundColor != null) {
                g2.setColor(hoverBackgroundColor);
            } else if (originalBackgroundColor != null) {
                 g2.setColor(originalBackgroundColor);
            } else {
                // Fallback if no background color is explicitly set (should use L&F default)
                g2.setColor(UIManager.getColor("Button.background"));
            }
        } else {
            // Disabled state background
            g2.setColor(UIManager.getColor("Button.disabledBackground") != null ?
                        UIManager.getColor("Button.disabledBackground") :
                        Color.LIGHT_GRAY.darker()); // A sensible default
        }

        // Fill the rounded rectangle background
        // Adjust for border thickness so border is drawn around this fill
        g2.fill(new RoundRectangle2D.Float(
                borderThickness / 2f,
                borderThickness / 2f,
                width - borderThickness,
                height - borderThickness,
                actualBorderRadius, actualBorderRadius)
        );


        // 2. Paint Border (if thickness > 0)
        if (borderThickness > 0 && borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            // Draw the border slightly inset so it appears centered on the edge
            // The coordinates and dimensions ensure the stroke is drawn around the fill area
            g2.draw(new RoundRectangle2D.Float(
                    borderThickness / 2f,
                    borderThickness / 2f,
                    width - borderThickness,
                    height - borderThickness,
                    actualBorderRadius, actualBorderRadius)
            );
        }
        g2.dispose();

        // 3. Let JButton paint its text and icon ON TOP OF our custom background
        // We call super.paintComponent() AFTER our custom background and border painting
        // because setContentAreaFilled(false) and setBorderPainted(false)
        // will prevent it from drawing its own background/border.
        // It will then correctly paint the text/icon.
        super.paintComponent(g);
    }

    /**
     * Overridden to provide a hit detection area that matches the rounded shape.
     * This ensures clicks outside the visible rounded area are not registered.
     */
    @Override
    public boolean contains(int x, int y) {
        if (borderRadius == 0) { // If no border radius, behave like a normal rectangular button
            return super.contains(x, y);
        }
        int actualBorderRadius = Math.min(borderRadius, Math.min(getWidth(), getHeight()));
        // Create a shape matching the button's visual appearance
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), actualBorderRadius, actualBorderRadius);
        return shape.contains(x, y);
    }

    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("JRButton Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            frame.getContentPane().setBackground(Color.DARK_GRAY);

            JRButton roundedButton1 = new JRButton("Round Me!");
            roundedButton1.setPreferredSize(new Dimension(150, 50));
            roundedButton1.setFont(new Font("Arial", Font.BOLD, 14));
            roundedButton1.setForeground(Color.WHITE);
            roundedButton1.setBackground(new Color(70, 130, 180)); // Steel Blue
            roundedButton1.setBorderRadius(40); // Make it quite round
            roundedButton1.setBorderColor(Color.CYAN);
            roundedButton1.setBorderThickness(3f);
            roundedButton1.setHoverBackgroundColor(new Color(100, 160, 210));
            roundedButton1.setPressedBackgroundColor(new Color(50, 110, 160));


            JRButton circleButton = new JRButton("OK");
            circleButton.setPreferredSize(new Dimension(80, 80));
            circleButton.setBorderRadius(80); // Make it a circle
            circleButton.setBackground(new Color(255, 99, 71)); // Tomato
            circleButton.setForeground(Color.WHITE);
            circleButton.setBorderColor(new Color(255, 60, 40));
            circleButton.setBorderThickness(4f);
            circleButton.setFont(new Font("Verdana", Font.BOLD, 18));

            JRButton standardLikeButton = new JRButton("Standard-ish");
            standardLikeButton.setPreferredSize(new Dimension(150, 40));
            // Default borderRadius is 0
            standardLikeButton.setBackground(new Color(0x5cb85c)); // Bootstrap Success Green
            standardLikeButton.setForeground(Color.WHITE);
            standardLikeButton.setBorderColor(new Color(0x4cae4c));
            standardLikeButton.setBorderThickness(1f);
            standardLikeButton.setBorderRadius(5); // Slightly rounded like modern buttons

            JRButton disabledButton = new JRButton("Disabled");
            disabledButton.setPreferredSize(new Dimension(150, 50));
            disabledButton.setBorderRadius(30);
            disabledButton.setBorderColor(Color.GRAY);
            disabledButton.setBorderThickness(2f);
            disabledButton.setBackground(Color.LIGHT_GRAY); // Set an explicit background
            disabledButton.setEnabled(false);


            frame.add(roundedButton1);
            frame.add(circleButton);
            frame.add(standardLikeButton);
            frame.add(disabledButton);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }*/
}