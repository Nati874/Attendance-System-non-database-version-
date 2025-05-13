package GUI;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class JRTextField extends JTextField {

    private int borderRadius = 10;

    private Color borderColor = Color.GRAY;
    private int borderThickness = 2;
    private int borderTopThickness = -1;
    private int borderBottomThickness = -1;
    private int borderLeftThickness = -1;
    private int borderRightThickness = -1;

    private String placeholder = "";
    private Color placeholderColor = Color.GRAY;

    public JRTextField() {
        super();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public JRTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public JRTextField(String text) {
        super(text);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public JRTextField(String text, int columns) {
        super(text, columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    public int getBorderTopThickness() {
        return borderTopThickness >= 0 ? borderTopThickness : borderThickness;
    }

    public void setBorderTopThickness(int thickness) {
        this.borderTopThickness = thickness;
        repaint();
    }

    public int getBorderBottomThickness() {
        return borderBottomThickness >= 0 ? borderBottomThickness : borderThickness;
    }

    public void setBorderBottomThickness(int thickness) {
        this.borderBottomThickness = thickness;
        repaint();
    }

    public int getBorderLeftThickness() {
        return borderLeftThickness >= 0 ? borderLeftThickness : borderThickness;
    }

    public void setBorderLeftThickness(int thickness) {
        this.borderLeftThickness = thickness;
        repaint();
    }

    public int getBorderRightThickness() {
        return borderRightThickness >= 0 ? borderRightThickness : borderThickness;
    }

    public void setBorderRightThickness(int thickness) {
        this.borderRightThickness = thickness;
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

    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }

    @Override
    public Insets getInsets() {
        int top = getBorderTopThickness();
        int bottom = getBorderBottomThickness();
        int left = getBorderLeftThickness();
        int right = getBorderRightThickness();
        return new Insets(top + 4, left + 4, bottom + 4, right + 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(
                0, 0, width - 1, height - 1,
                borderRadius, borderRadius
        ));

        // Borders per side
        g2.setColor(getForeground());

        int top = getBorderTopThickness();
        int bottom = getBorderBottomThickness();
        int left = getBorderLeftThickness();
        int right = getBorderRightThickness();

        if (top > 0) {
            g2.fillRect(0, 0, width, top);
        }
        if (bottom > 0) {
            g2.fillRect(0, height - bottom, width, bottom);
        }
        if (left > 0) {
            g2.fillRect(0, 0, left, height);
        }
        if (right > 0) {
            g2.fillRect(width - right, 0, right, height);
        }

        g2.setBackground(borderColor);

        g2.dispose();
        super.paintComponent(g);

        // Draw placeholder if needed
        if (getText().isEmpty() && !isFocusOwner() && placeholder != null && !placeholder.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(placeholderColor);
            g2d.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            FontMetrics fm = g2d.getFontMetrics();
            int x = insets.left + 2;
            int y = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(placeholder, x, y);
            g2d.dispose();
        }
    }
}



