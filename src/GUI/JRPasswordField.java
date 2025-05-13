package GUI;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class JRPasswordField extends JPasswordField {

    private int borderRadius = 15;
    private Color borderColor = Color.GRAY;
    private float borderThickness = 1f;
    private String placeholder = "";
    private Color placeholderColor = Color.LIGHT_GRAY;

    private final int textPadding; // Internal padding between border and text

    public JRPasswordField() {
        super();
        this.textPadding = 5; // Default padding
        setBorder(BorderFactory.createEmptyBorder());
        init();
    }

    public JRPasswordField(int columns) {
        super(columns);
        this.textPadding = 5;
        setBorder(BorderFactory.createEmptyBorder());
        init();
    }

    public JRPasswordField(String text) {
        super(text);
        this.textPadding = 5;
        setBorder(BorderFactory.createEmptyBorder());
        init();
    }

    public JRPasswordField(String text, int columns) {
        super(text, columns);
        this.textPadding = 5;
        setBorder(BorderFactory.createEmptyBorder());
        init();
    }


    private void init() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        updateInsets(); // Set initial insets for text

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                repaint();
            }
        });
    }

    private void updateInsets() {
        int ins = (int) Math.ceil(borderThickness) + textPadding;
        super.setBorder(BorderFactory.createEmptyBorder(ins, ins, ins, ins));
    }

    // --- Custom Property Getters and Setters ---

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        if (borderRadius < 0) {
            this.borderRadius = 0;
        } else {
            this.borderRadius = borderRadius;
        }
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(float borderThickness) {
        if (borderThickness < 0) {
            this.borderThickness = 0f;
        } else {
            this.borderThickness = borderThickness;
        }
        updateInsets();
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Paint custom rounded background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));

        // 2. Paint custom border
        if (borderThickness > 0 && borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.draw(new RoundRectangle2D.Float(
                    borderThickness / 2f,
                    borderThickness / 2f,
                    getWidth() - borderThickness,
                    getHeight() - borderThickness,
                    borderRadius, borderRadius));
        }

        // 3. Let JPasswordField paint its content (echo chars, caret, selection)
        super.paintComponent(g);

        // 4. Paint placeholder text if the field is empty and not focused
        // For JPasswordField, check getPassword().length
        if (getPassword().length == 0 && !hasFocus() && placeholder != null && !placeholder.isEmpty()) {
            g2.setColor(placeholderColor);
            FontMetrics fm = getFontMetrics(getFont());

            Insets currentInsets = getInsets();
            int availableHeight = getHeight() - currentInsets.top - currentInsets.bottom;
            int y = currentInsets.top + (availableHeight - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(placeholder, currentInsets.left, y);
        }

        g2.dispose();
    }

/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("JRPasswordField Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            frame.getContentPane().setBackground(Color.decode("#f0f0f0"));

            JRPasswordField passField1 = new JRPasswordField(20);
            passField1.setFont(new Font("Arial", Font.PLAIN, 16));
            passField1.setPlaceholder("Enter password");
            passField1.setBorderRadius(20);
            passField1.setBorderColor(Color.RED);
            passField1.setBorderThickness(2f);
            passField1.setBackground(Color.WHITE);
            passField1.setPreferredSize(new Dimension(250, 40));
            passField1.setEchoChar('*');


            JRPasswordField passField2 = new JRPasswordField();
            passField2.setPlaceholder("Confirm password");
            passField2.setBorderRadius(10);
            passField2.setBorderColor(new Color(255, 102, 0)); // Orange
            passField2.setBorderThickness(1.5f);
            passField2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            passField2.setBackground(new Color(255, 240, 230));
            passField2.setPlaceholderColor(Color.DARK_GRAY);
            passField2.setPreferredSize(new Dimension(200, 35));
            passField2.setEchoChar((char) 0x2022); // Bullet character


            frame.add(new JLabel("Password:"));
            frame.add(passField1);
            frame.add(new JLabel("Confirm:"));
            frame.add(passField2);

            frame.pack();
            frame.setSize(new Dimension(600, 200));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }*/
}
