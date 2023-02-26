import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;

public class BoardFrame extends JFrame {

    private ChessPiece[][] board = new ChessPiece[8][8];
    private final int numColumns = 8;
    private final int numRows = 8;

    public BoardFrame() {
        // Inititalizations and constants
        initializeBoard();
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        setBounds(0,0,518,535);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        contentPane.add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLayout(new GridLayout(numRows, numColumns));
        
        Color tileColor;
        for (int row=0; row < numRows; row++) {
            for(int col=0; col < numColumns; col++) {

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

                tileList.add(new Tile(board[row][col], tileColor));
                boardPanel.add(new Tile(board[row][col], tileColor));
            }
        }
    }

    public void initializeBoard() {
        // Initial setup for the black pieces
        board[0][0] = board[0][7] = new Rook("Black");
        board[0][1] = board[0][6] = new Knight("Black");
        board[0][2] = board[0][5] = new Bishop("Black");
        board[0][3] = new Queen("Black");
        board[0][4] = new King("Black");
        for (int col=0; col < numColumns; col++) {
            board[1][col] = new Pawn("Black");
        }

        // Initial empty spaces
        for (int row=2; row < numRows; row++) {
            for (int col=0; col < numColumns; col++) {
                board[row][col] = new Empty();
            }
        }

        // Initial setup for the white pieces
        board[7][0] = board[7][7] = new Rook("White");
        board[7][1] = board[7][6] = new Knight("White");
        board[7][2] = board[7][5] = new Bishop("White");
        board[7][3] = new Queen("White");
        board[7][4] = new King("White");
        for (int col=0; col < numColumns; col++) {
            board[6][col] = new Pawn("White");
        }
    }
}