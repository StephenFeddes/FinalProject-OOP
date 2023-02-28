import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.Color;

public class ChessView extends JFrame {

    private ChessPiece[][] board = new ChessPiece[8][8];
    private JPanel boardPanel = new JPanel();
    
    public ChessPiece[][] getBoard() { return board; }
    public void setBoard(ChessPiece[][] boardIn) { this.board = boardIn; }

    public ChessView() {
        // Inititalizations and constants
        board = ChessModel.initializeBoard();
        setBounds(0,0,518,535);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayBoard();
    }

    public void displayBoard() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLayout(new GridLayout(8, 8));
        
        Color tileColor;
        Tile initTile;
        for (int row=0; row < 8; row++) {
            for(int col=0; col < 8; col++) {

                if (row%2 == 0) {
                    if (col%2 == 0) {
                        tileColor = Color.ORANGE;
                    } else {
                        tileColor = Color.RED;
                    }
                } else {
                    if (col%2 == 0) {
                        tileColor = Color.RED;
                    } else {
                        tileColor = Color.ORANGE;
                    }
                }

                initTile = new Tile(board[row][col], tileColor);

                initTile.setRowCoordinate(row);
                initTile.setColCoordinate(col);

                boardPanel.add(initTile);
            }
        }
    }
}