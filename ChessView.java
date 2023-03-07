import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Graphics;
import java.awt.Color;

public class ChessView extends JFrame {

    private final int BOARD_SIZE = 8;
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<int[]> availableTilesList = new ArrayList<int[]>();
    private JPanel boardPanel = new JPanel();
    private JPanel gameMessagesPanel = new JPanel();
    private JPanel gameStatusPanel = new gameStatusPanel();
    private int[] tileCoordinates = new int[2];
    private String gameStatus = "LewisChess         ";

    ActionListener listenerForBoardClick;
    private Container contentPane = getContentPane();

    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public int[] getTileCoordinates() { return tileCoordinates; }

    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }
    public void setAvailableTilesList(ArrayList<int[]> currentAvailableTiles) { availableTilesList = currentAvailableTiles; }

    public void displayBoard(ChessPiece[][] boardIn) { 

        this.remove(boardPanel);
        this.remove(gameMessagesPanel);
        this.remove(contentPane);
        this.remove(gameStatusPanel);
        tileList.clear();
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(300, 300));
        gameMessagesPanel = new JPanel();
        gameStatusPanel = new gameStatusPanel();
        
        contentPane.setLayout(new BorderLayout());
        contentPane.add(boardPanel, BorderLayout.CENTER);
        contentPane.add(gameMessagesPanel, BorderLayout.NORTH);
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        gameMessagesPanel.setLayout(new BorderLayout());
        
        Color tileColor;
        Tile newTile;
        int currentTileCoordinates[] = new int[2];
        for (int row=0; row < BOARD_SIZE; row++) {
            for(int col=0; col < BOARD_SIZE; col++) {

                currentTileCoordinates[0] = row;
                currentTileCoordinates[1] = col;
                if (row%2 == 0) {
                    if (col%2 == 0) {
                        tileColor = new Color(200,200,150);
                    } else {
                        tileColor = new Color(100,150,100);
                    }
                } else {
                    if (col%2 == 0) {
                        tileColor = new Color(100,150,100);
                    } else {
                        tileColor = new Color(200,200,150);
                    }
                }

                if (ChessController.isCoordinatesInArrayList(currentTileCoordinates, availableTilesList)) {

                    tileColor = new Color(125,200,125);

                    if (boardIn[row][col].getPieceType() != "Empty") {
                        tileColor = new Color(200,125,125);
                    }
                }

                newTile = new Tile(boardIn[row][col], tileColor);
                newTile.setRowCoordinate(row);
                newTile.setColCoordinate(col);

                tileList.add(newTile);
                boardPanel.add(newTile);

                gameMessagesPanel.add(gameStatusPanel);
        }

        for (Tile tile : tileList) {
            addMoveListener(tile);
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }
    }

    public ChessView() {
        // Inititalizations and constants
        board = ChessController.initializeBoard();
        setBounds(0,0,518,535);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayBoard(board);
    }

    public void addMoveListener(Tile tileIn) {

        tileIn.getTileButton().addActionListener(listenerForBoardClick);

        tileIn.getTileButton().addActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tileCoordinates[0] = tileIn.getRowCoordinate();
                tileCoordinates[1] = tileIn.getColCoordinate();
            }
        });
    }


    public void addMoveListener(ActionListener listenerForBoardClick) {

        this.listenerForBoardClick = listenerForBoardClick;

        for (Tile tile : tileList) {

            tile.getTileButton().addActionListener(listenerForBoardClick);

            tile.getTileButton().addActionListener( new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    tileCoordinates[0] = tile.getRowCoordinate();
                    tileCoordinates[1] = tile.getColCoordinate();
                }
            });
        }
    }

    class pieceOptionsPanel extends JPanel {
        
        pieceOptionsPanel() {
            setLayout(new BorderLayout());
            
        }
    }

    class gameStatusPanel extends JPanel {

        gameStatusPanel() {
            setPreferredSize(new Dimension(100,100));
            setBackground(new Color(100,100,75));
        }

        protected void paintComponent(Graphics g) {
            setPreferredSize(new Dimension(100,100));
            super.paintComponent(g);
    
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            final int FONT_SIZE = 50;
    
            g.setColor(new Color(200,200,150));
            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
            g.drawString(getGameStatus(), panelWidth/2 - getGameStatus().length()/2 * FONT_SIZE/4, panelHeight/2);
        }
    }
}