import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;

public class Tile extends JPanel {
    
    private JButton changeTileButton;
    private ChessPiece piece;
    private boolean isEmpty;
    private Color tileColor;

    public ChessPiece getPiece() { return piece; }
    public void setPiece(ChessPiece piece) { this.piece = piece; }
    public boolean getisEmpty() { return isEmpty; }
    public void setEmpty(boolean isEmpty) { this.isEmpty = isEmpty; }

    public Tile(ChessPiece pieceIn, Color tileColorIn) {
        setLayout(null); // Allows button in panel to be any size it wants to be
        
        /* Creates an invisible button. This allows the user to
        replace the shape with a face by clicking the shape. */
        changeTileButton = new JButton();
        
        // Makes button invisible.
        changeTileButton.setOpaque(false);
        changeTileButton.setContentAreaFilled(false);
        changeTileButton.setBorderPainted(false);

        add(changeTileButton);

        JLabel pieceLabel = new JLabel(pieceIn.getPieceImage());
        add(pieceLabel);

        // When clicked, the clickCount is incremented and the shape is painted again.
        // When the clickCount is odd, a face is shown in place of the shape.
        changeTileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               repaint();
            }
        });
    }

    public Tile() {
        setEmpty(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(tileColor);
        g.fillRect(0,0, getWidth(), getHeight());

        // Sets size of the invisible button so it covers the entire shape
        changeTileButton.setSize(getWidth(), getHeight());
    }
}