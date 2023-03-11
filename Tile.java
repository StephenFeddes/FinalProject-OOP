import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class Tile extends JPanel {
    
    // Member variables
    private JButton tileButton;
    private ChessPiece piece;
    private Color tileColor;
    private int rowCoordinate;
    private int colCoordinate;

    // Getters
    public ChessPiece getPiece() { return piece; }
    public Color getTileColor() { return tileColor; }
    public int getRowCoordinate() { return rowCoordinate; }
    public int getColCoordinate() { return colCoordinate; }
    public JButton getTileButton() { return tileButton; }

    // Setters
    public void setRowCoordinate(int rowCoordinate) { this.rowCoordinate = rowCoordinate; } // row coordinate of tile in the board
    public void setColCoordinate(int colCoordinate) { this.colCoordinate = colCoordinate; } // column coordinate of tile in the board
    public void setPiece(ChessPiece piece) { this.piece = piece; }
    public void setTileColor(Color tileColor) { this.tileColor = tileColor; }

    public Tile(ChessPiece pieceIn, Color tileColorIn) {
        setLayout(null); // Allows button in panel to be any size it wants to be
        setPiece(pieceIn);
        setTileColor(tileColorIn);
        
        // Invisible button over the tile. Has the piece icon on it.
        tileButton = new JButton();
        
        // Makes button invisible.
        tileButton.setOpaque(false);
        tileButton.setContentAreaFilled(false);
        tileButton.setBorderPainted(false);
    
        // Gets the pieces image and displays it in the tile
        if (!piece.getType().equals("Empty")) {
            tileButton.setIcon(pieceIn.getImage());
        }

        add(tileButton);
    }

    // Draws the tile itself, not the piece it contains
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(tileColor);
        g.fillRect(0,0, getWidth(), getHeight());

        // Sets size of the invisible button so it covers the entire shape
        tileButton.setSize(getWidth(), getHeight());
    }
}