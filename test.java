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

public class test extends JFrame {

    private final int BOARD_SIZE = 8;
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<int[]> availableTilesList = new ArrayList<int[]>();
    private JPanel boardPanel = new JPanel();
    private JPanel gameMessagesPanel = new JPanel();
    private JPanel gameStatusPanel = new gameStatusPanel();
    private JPanel resetButtonPanel = new ResetButtonPanel();
    private JPanel westPanel = new JPanel();
    private JPanel eastPanel = new JPanel();
    private String turnColor = "White";
    private JPanel optionsPanel = new JPanel();
    private int[] tileCoordinates = new int[2];
    private String gameStatus = "LewisChess         ";
    private ChessPiece convertedPiece;
    ActionListener listenerForBoardClick;
    private Container contentPane = getContentPane();
    public boolean isPawnAtEnd = false;
    private ArrayList<Tile> lostWhitePieces = new ArrayList<Tile>();
    private ArrayList<Tile> lostBlackPieces = new ArrayList<Tile>();
    public boolean isResetClicked = false;

    public ChessPiece getConvertedPiece() { return convertedPiece; }
    public String getTurnColor() { return turnColor; }
    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public int[] getTileCoordinates() { return tileCoordinates; }

    public void setLostWhitePieces(ArrayList<Tile> lostWhitePieces) { this.lostWhitePieces = lostWhitePieces; }
    public void setLostBlackPieces(ArrayList<Tile> lostBlackPieces) { this.lostBlackPieces = lostBlackPieces; }
    public void setTurnColor(String turnColor) { this.turnColor = turnColor; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }
    public void setAvailableTilesList(ArrayList<int[]> currentAvailableTiles) { availableTilesList = currentAvailableTiles; }

    public test() {
        // Inititalizations and constants
        board = ChessController.initializeBoard();
        setBounds(250,0,900,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayBoard(board);
    }

    public void displayBoard(ChessPiece[][] boardIn) { 

        this.remove(boardPanel);
        this.remove(gameMessagesPanel);
        this.remove(contentPane);
        this.remove(gameStatusPanel);
        this.remove(optionsPanel);
        this.remove(westPanel);
        this.remove(eastPanel);
        this.remove(resetButtonPanel);
        tileList.clear();
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(300, 300));
        gameMessagesPanel = new JPanel();
        gameStatusPanel = new gameStatusPanel();
        optionsPanel = createOptionsPanel(getTurnColor());
        eastPanel = new JPanel();
        westPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        resetButtonPanel = new ResetButtonPanel();

        contentPane.setLayout(new BorderLayout());
        contentPane.add(boardPanel, BorderLayout.CENTER);
        contentPane.add(gameMessagesPanel, BorderLayout.NORTH);
        contentPane.add(eastPanel, BorderLayout.EAST);
        contentPane.add(westPanel, BorderLayout.WEST);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        eastPanel.setLayout(new GridLayout(8,2));
        westPanel.setLayout(new GridLayout(8,2));
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBackground(new Color(165,185, 210));
        gameMessagesPanel.setLayout(new BorderLayout());
        gameMessagesPanel.add(new BlankPanel(new Color(125,0,0), 200, 100), BorderLayout.EAST);
        gameMessagesPanel.add(new BlankPanel(new Color(125,0,0), 200, 100), BorderLayout.WEST);
        bottomPanel.add(resetButtonPanel, BorderLayout.CENTER);
        bottomPanel.add(new BlankPanel(new Color(125,0,0), 100, 100), BorderLayout.NORTH);


        eastPanel.setPreferredSize(new Dimension(200, 200));
        eastPanel.setBackground(new Color(165,185, 210));

        for (Tile lostWhitePiece : lostWhitePieces) {
            eastPanel.add(lostWhitePiece);
        }

        westPanel.setPreferredSize(new Dimension(200, 200));
        westPanel.setBackground(new Color(165,185, 210));

        for (Tile lostBlackPiece : lostBlackPieces) {
            westPanel.add(lostBlackPiece);
        }

        gameMessagesPanel.add(gameStatusPanel, BorderLayout.CENTER);

        if (isPawnAtEnd) {
            gameMessagesPanel.add(optionsPanel, BorderLayout.CENTER);
        }
        
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

                    if (boardIn[row][col].getPieceColor() == turnColor) {

                        tileColor = new Color(150,150,125);

                    } else if (boardIn[row][col].getPieceColor() != turnColor && boardIn[row][col].getPieceType() != "Empty") {

                        tileColor = new Color(200,125,125);

                    } else {

                        tileColor = new Color(125,200,125);
                    }

                    // If castling, then the rook tiles should be green
                    boolean isRookSelected = boardIn[tileCoordinates[0]][tileCoordinates[1]].getPieceType() == "Rook";
                
                    if (boardIn[row][col].getPieceType() == "Rook" && boardIn[row][col].getPieceColor() == turnColor && !isRookSelected) {

                        tileColor = new Color(200,200,50);
                    }
                }

                newTile = new Tile(boardIn[row][col], tileColor);
                newTile.setRowCoordinate(row);
                newTile.setColCoordinate(col);

                tileList.add(newTile);
                boardPanel.add(newTile);
        }

        if (!isPawnAtEnd) {
            for (Tile tile : tileList) {
                addMoveListener(tile);
            }
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }
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

    public void addPawnAtEndListener(Tile tileIn) {
        PieceFactory pieceFactory = new PieceFactory();

        tileIn.getTileButton().addActionListener(listenerForBoardClick);

        tileIn.getTileButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertedPiece = pieceFactory.createPiece(tileIn.getPiece().getPieceType(), tileIn.getPiece().getPieceColor());
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

    JPanel createOptionsPanel(String turnColor) {

        JPanel newOptionsPanel = new JPanel();

        newOptionsPanel.setPreferredSize(new Dimension(100,100));
        newOptionsPanel.setLayout(new GridLayout());

        PieceFactory pieceFactory = new PieceFactory();

        switch(turnColor) {
            case "White":
                turnColor = "Black";
                break;
            case "Black":
                turnColor = "White";
                break;
        }

        Tile rookTile = new Tile(pieceFactory.createPiece("Rook", turnColor), new Color(255,255, 255));
        addPawnAtEndListener(rookTile);

        Tile knightTile = new Tile(pieceFactory.createPiece("Knight", turnColor), new Color(255,255, 255));
        addPawnAtEndListener(knightTile);

        Tile bishopTile = new Tile(pieceFactory.createPiece("Bishop", turnColor), new Color(255,255, 255));
        addPawnAtEndListener(bishopTile);

        Tile queenTile = new Tile(pieceFactory.createPiece("Queen", turnColor), new Color(255,255, 255));
        addPawnAtEndListener(queenTile);

        newOptionsPanel.add(rookTile);
        newOptionsPanel.add(knightTile);
        newOptionsPanel.add(bishopTile);
        newOptionsPanel.add(queenTile);

        return newOptionsPanel;
    }
    
    class gameStatusPanel extends JPanel {

        gameStatusPanel() {
            setPreferredSize(new Dimension(100,100));
            setBackground(new Color(165,185, 210));
        }

        protected void paintComponent(Graphics g) {
            setPreferredSize(new Dimension(100,100));
            super.paintComponent(g);
    
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            final int FONT_SIZE = 50;
    
            g.setColor(new Color(125,0,0));
            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
            g.drawString(getGameStatus(), panelWidth/2 - getGameStatus().length()/2 * FONT_SIZE/4, panelHeight/2);
        }
    }

    class BlankPanel extends JPanel {

        BlankPanel(Color colorIn, int width, int height) {

            setPreferredSize(new Dimension(width, height));
            setBackground(new Color(125,0,0));

        }
    }

    class ResetButtonPanel extends JPanel {

        int panelWidth = (int) getBounds().getWidth();

        ResetButtonPanel() {

            //System.out.println(ChessView.WIDTH)

            //setPreferredSize(new Dimension(100,100));
            setBackground(new Color(165,185, 210));

            JButton resetButton = new JButton("Reset");
            //resetButton.setBounds(ChessView.WIDTH, 0, 50, 50);

            resetButton.setPreferredSize(new Dimension(100, 50));
            
            resetButton.addActionListener(listenerForBoardClick);
            resetButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    isResetClicked = true;
                }
            });

            add(resetButton);
        }

        protected void paintComponent(Graphics g) {
            panelWidth = getWidth();
        }
    }

    public void resetViewProperties() {

        final int BOARD_SIZE = 8;
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        ChessPiece[][] board = ChessController.initializeBoard();
        ArrayList<int[]> availableTilesList = new ArrayList<int[]>();
        this.remove(boardPanel);
        this.remove(gameMessagesPanel);
        this.remove(contentPane);
        this.remove(gameStatusPanel);
        this.remove(optionsPanel);
        this.remove(westPanel);
        this.remove(eastPanel);
        this.remove(resetButtonPanel);
        
        turnColor = "White";
        tileCoordinates = new int[2];
        gameStatus = "LewisChess         ";
        convertedPiece = null;
        isPawnAtEnd = false;
        lostWhitePieces = new ArrayList<Tile>();
        lostBlackPieces = new ArrayList<Tile>();
        isResetClicked = false;
    }
}


