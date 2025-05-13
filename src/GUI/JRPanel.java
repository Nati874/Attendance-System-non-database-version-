package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class JRPanel extends JPanel {

    private int borderRadius = 0;
    private int borderTopLeftRadius = -1;
    private int borderTopRightRadius = -1;
    private int borderBottomLeftRadius = -1;
    private int borderBottomRightRadius = -1;

    private int borderThickness = 1;
    private Color borderColor = Color.BLACK;

    public JRPanel() {
        setOpaque(false);  // We handle the background ourselves
    }

    // Getters and setters
    public int getBorderRadius() {
        return borderRadius;

    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public int getBorderTopLeftRadius() {
        return borderTopLeftRadius >= 0 ? borderTopLeftRadius : borderRadius;
    }

    public void setBorderTopLeftRadius(int radius) {
        this.borderTopLeftRadius = radius;
        repaint();
    }

    public int getBorderTopRightRadius() {
        return borderTopRightRadius >= 0 ? borderTopRightRadius : borderRadius;
    }

    public void setBorderTopRightRadius(int radius) {
        this.borderTopRightRadius = radius;
        repaint();
    }

    public int getBorderBottomLeftRadius() {
        return borderBottomLeftRadius >= 0 ? borderBottomLeftRadius : borderRadius;
    }

    public void setBorderBottomLeftRadius(int radius) {
        this.borderBottomLeftRadius = radius;
        repaint();
    }

    public int getBorderBottomRightRadius() {
        return borderBottomRightRadius >= 0 ? borderBottomRightRadius : borderRadius;
    }

    public void setBorderBottomRightRadius(int radius) {
        this.borderBottomRightRadius = radius;
        repaint();
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int tl = getBorderTopLeftRadius();
        int tr = getBorderTopRightRadius();
        int br = getBorderBottomRightRadius();
        int bl = getBorderBottomLeftRadius();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int halfThickness = borderThickness / 2;

        Shape shape = createRoundedShape(halfThickness, halfThickness, width - borderThickness, height - borderThickness, tl, tr, br, bl);

        // Fill background
        g2.setColor(getBackground());
        g2.fill(shape);

        // Draw border
        if (borderThickness > 0) {
            g2.setStroke(new BasicStroke(borderThickness));
            g2.setColor(borderColor);
            g2.draw(shape);
        }

        g2.dispose();
    }

    private Shape createRoundedShape(int x, int y, int width, int height, int tl, int tr, int br, int bl) {
        Path2D path = new Path2D.Double();
        path.moveTo(x + tl, y);

        // Top edge
        path.lineTo(x + width - tr, y);
        if (tr > 0) {
            path.quadTo(x + width, y, x + width, y + tr);
        }

        // Right edge
        path.lineTo(x + width, y + height - br);
        if (br > 0) {
            path.quadTo(x + width, y + height, x + width - br, y + height);
        }

        // Bottom edge
        path.lineTo(x + bl, y + height);
        if (bl > 0) {
            path.quadTo(x, y + height, x, y + height - bl);
        }

        // Left edge
        path.lineTo(x, y + tl);
        if (tl > 0) {
            path.quadTo(x, y, x + tl, y);
        }

        path.closePath();
        return path;
    }

    // Demo app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JRPanel Path2D Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            JRPanel panel = new JRPanel();
            panel.setBackground(Color.CYAN);
            panel.setBorderRadius(20);
            panel.setBorderTopLeftRadius(50);
            panel.setBorderTopRightRadius(10);
            panel.setBorderBottomLeftRadius(30);
            panel.setBorderBottomRightRadius(5);
            panel.setBorderThickness(5);
            panel.setBorderColor(Color.MAGENTA);
            panel.setLayout(new BorderLayout());

            JLabel label = new JLabel("Hello JRPanel with Path2D!", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(label, BorderLayout.CENTER);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}