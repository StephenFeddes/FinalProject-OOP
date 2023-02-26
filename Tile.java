import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.Color;

public class Tile extends JPanel {
    
    private JButton changeTileButton;
    private ChessPiece piece;
    private boolean isEmpty;
    private Color tileColor;

    public ChessPiece getPiece() { return piece; }
    public void setPiece(ChessPiece piece) { this.piece = piece; }
    public boolean getisEmpty() { return isEmpty; }
    public void setEmpty(boolean isEmpty) { this.isEmpty = isEmpty; }
    public Color getTileColor() { return tileColor; }
    public void setTileColor(Color tileColor) { this.tileColor = tileColor; }

    public Tile(ChessPiece pieceIn, Color tileColorIn) {
        setLayout(null); // Allows button in panel to be any size it wants to be
        setPiece(pieceIn);
        setTileColor(tileColorIn);
        /* Creates an invisible button. This allows the user to
        replace the shape with a face by clicking the shape. */
        changeTileButton = new JButton();
        
        // Makes button invisible.
        changeTileButton.setOpaque(false);
        changeTileButton.setContentAreaFilled(false);
        changeTileButton.setBorderPainted(false);

        if (!piece.getType().equals("Empty")) {
            changeTileButton.setIcon(pieceIn.getPieceImage());
        }

        add(changeTileButton);

        changeTileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(tileColor);
        g.fillRect(0,0, getWidth(), getHeight());

        // Sets size of the invisible button so it covers the entire shape
        changeTileButton.setSize(getWidth(), getHeight());
    }
}