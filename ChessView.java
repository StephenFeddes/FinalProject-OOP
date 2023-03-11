import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class ChessView extends JFrame {

    // Member variables
    private final int BOARD_SIZE = 8;
    private JPanel northPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel eastPanel = new JPanel();
    private JPanel westPanel = new JPanel(); 
    private JPanel boardPanel = new JPanel(); 
    private JPanel gamePanel = new JPanel(); 
    private JPanel gameStatusPanel = new JPanel();
    private JPanel optionsPanel = new JPanel(); 
    private JPanel resetButtonPanel = new JPanel();
    private ArrayList<Tile> tileList = new ArrayList<Tile>(); // List of tiles in the current board
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE]; // 2D array of the board's pieces
    private ArrayList<int[]> availableTilesList = new ArrayList<int[]>(); // List of tiles a player's selected piece can go or selected
    private String turnColor = "White";  // String indicating whose turn it currently is
    private int[] selectedTileCoordinates = new int[2]; // Coordinates of the last board tile the user clicked on
    private String gameStatus = "LewisChess         "; // Message showing check, checkmate, or current turn
    private ChessPiece convertedPiece; // If a pawn reaches end of board, it gets converted to the chosen type
    private Container contentPane = getContentPane(); // Content the JFrame will ultimately display
    private ArrayList<Tile> lostWhitePieces = new ArrayList<Tile>(); // List of white pieces lost
    private ArrayList<Tile> lostBlackPieces = new ArrayList<Tile>(); // List of black pieces lost
    private ChessPiece selectedPiece;
    public boolean isResetClicked = false; // If reset is clicked, the board is reset to the initial state
    public boolean isPawnAtEnd = false; // If a pawn reaches the end, new piece options are displayed
    ActionListener listenerForBoardClick; // The controller that will listen to the button click

    // Getters
    public ChessPiece getSelectedPiece() { return selectedPiece; }
    public ChessPiece getConvertedPiece() { return convertedPiece; }
    public String getTurnColor() { return turnColor; }
    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public int[] getSelectedTileCoordinates() { return selectedTileCoordinates; }

    // Setters
    public void setSelectedPiece(ChessPiece selectedPiece) {this.selectedPiece = selectedPiece; }
    public void setLostWhitePieces(ArrayList<Tile> lostWhitePieces) { this.lostWhitePieces = lostWhitePieces; }
    public void setLostBlackPieces(ArrayList<Tile> lostBlackPieces) { this.lostBlackPieces = lostBlackPieces; }
    public void setTurnColor(String turnColor) { this.turnColor = turnColor; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }
    public void setAvailableTilesList(ArrayList<int[]> currentAvailableTiles) { availableTilesList = currentAvailableTiles; }

    public ChessView() {
        
        board = ChessLib.initializeBoard(); // 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Makes the JFrame initially start in full-screen
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        displayBoard(board);
    }

    public void displayBoard(ChessPiece[][] boardIn) { 

        // Removes all the panels so that the new ones replace them
        remove(boardPanel);
        remove(northPanel);
        remove(contentPane);
        remove(gameStatusPanel);
        remove(optionsPanel);
        remove(westPanel);
        remove(eastPanel);
        remove(resetButtonPanel);
        remove(southPanel);
        remove(gamePanel);
        tileList.clear(); // Removes all tiles in the previous tile list so that new ones replace them

        // Initializes panels
        northPanel = new JPanel();
        southPanel = new JPanel();
        eastPanel = new JPanel();
        westPanel = new JPanel(); 
        boardPanel = new JPanel(); // Panel containing the board
        gamePanel = new JPanel(); // Panel containing the boardPanel, northPanel, etc.
        gameStatusPanel = new gameStatusPanel(); // Panel that is contained by the north panel. Provides game messages
        optionsPanel = createOptionsPanel(getTurnColor());

        // Sets up north panel display
        northPanel.setLayout(new BorderLayout());
        northPanel.add(gameStatusPanel, BorderLayout.CENTER);
        northPanel.add(new BlankPanel(new Color(125,0, 0), 110, 60), BorderLayout.EAST);
        northPanel.add(new BlankPanel(new Color(125,0, 0), 110, 60), BorderLayout.WEST);
        if (isPawnAtEnd) {
            // If a pawn reaches the end, the piece options panel is displayed in the north panel
            northPanel.add(optionsPanel, BorderLayout.CENTER);
        }

        // Sets up south panel display
        southPanel.setLayout(new BorderLayout());
        southPanel.add(new BlankPanel(new Color(165,185, 210), 50, 50), BorderLayout.NORTH);
        resetButtonPanel = new ResetButtonPanel();
        southPanel.add(resetButtonPanel, BorderLayout.CENTER);

        // Sets up east panel display
        eastPanel.setLayout(new GridLayout(8,2));
        eastPanel.setPreferredSize(new Dimension(110, 100));
        eastPanel.setBackground(new Color(165,185, 210));
        for (Tile lostWhitePiece : lostWhitePieces) {
            // Adds the latest lost white piece to the east panel
            eastPanel.add(lostWhitePiece);
        }

        // Sets up west panel display
        westPanel.setLayout(new GridLayout(8,2));
        westPanel.setPreferredSize(new Dimension(110, 100));
        westPanel.setBackground(new Color(165,185, 210));
        for (Tile lostBlackPiece : lostBlackPieces) {
            // Adds the latest lost black piece to the east panel
            westPanel.add(lostBlackPiece);
        }

        // Panel containing all the panels, such as the board, south, etc
        gamePanel.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit(). getScreenSize();
        gamePanel.setBounds((int)screenSize.getWidth()/4,0, 700, 650);
        gamePanel.add(northPanel, BorderLayout.NORTH);
        gamePanel.add(southPanel, BorderLayout.SOUTH);
        gamePanel.add(eastPanel, BorderLayout.EAST);
        gamePanel.add(westPanel, BorderLayout.WEST);
        gamePanel.add(boardPanel, BorderLayout.CENTER);

        // Content that the JFrame will display
        contentPane.setLayout(null);
        contentPane.add(gamePanel, BorderLayout.CENTER); // Adds the whole game panel to the center
        contentPane.setBackground(new Color(125,0,0));

        //
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBackground(new Color(125,0, 0));
        boardPanel.setBorder(BorderFactory.createLineBorder(new Color(125,0, 0), 2));
        

    
        Color tileColor;
        Tile newTile;
        int tileCoordinates[] = new int[2];
        // Creates each tile by coloring it and putting a piece on it
        // Adds the tiles to be displayed to the board panel
        for (int row=0; row < BOARD_SIZE; row++) {
            for(int col=0; col < BOARD_SIZE; col++) {

                tileCoordinates[0] = row;
                tileCoordinates[1] = col;
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

                // If tile is in availableTilesList, color it appropriately.
                if (ChessLib.isCoordinatesInArrayList(tileCoordinates, availableTilesList)) {

                    // Colors the selected piece's tile's grey
                    if (getSelectedPiece().equals(boardIn[row][col])) {

                        tileColor = new Color(150,150,125);

                    } else if (boardIn[row][col].getColor() != turnColor && boardIn[row][col].getType() != "Empty") {

                        // Colors the tile red if the selected piece can attack it
                        tileColor = new Color(200,125,125);

                    } else {

                        // Colors the tile green if it is empty and tjhe
                        tileColor = new Color(125,200,125);
                    }

                    // If a rook of the same color as the current turn is available, then it means castling can happen. That rook tile should be yellow
                    if (getSelectedPiece().getType() == "King" && boardIn[row][col].canCastle) {

                        // Colors the rook tile yellow if the selected king can castle with it
                        tileColor = new Color(200,200,50);
                    }
                }

                // Adds piece from the 2d piece array to the current tile and colors the tile appropriately
                newTile = new Tile(boardIn[row][col], tileColor);

                // Sets the coordinates for the tile
                newTile.setRowCoordinate(row);
                newTile.setColCoordinate(col);

                // Adds tile to the array list of tiles and to the board panel
                tileList.add(newTile);
                boardPanel.add(newTile);
        }

        // If a pawn reaches the end, the board becomes unresponsive until that pawn is converted
        if (!isPawnAtEnd) {
            for (Tile tile : tileList) {
                addMoveListener(tile);
            }
        }

    // Redisplays the JFrame
        SwingUtilities.updateComponentTreeUI(this);
    }
    }

    public void addMoveListener(Tile tileIn) {

        tileIn.getTileButton().addActionListener(listenerForBoardClick);

        tileIn.getTileButton().addActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedTileCoordinates[0] = tileIn.getRowCoordinate();
                selectedTileCoordinates[1] = tileIn.getColCoordinate();
            }
        });
    }


    // Adds actions listeners for the options panel
    public void addPawnConversionListener(Tile tileIn) {
        PieceFactory pieceFactory = new PieceFactory();

        // Controller listens for an option click
        tileIn.getTileButton().addActionListener(listenerForBoardClick);

        // Converts the pawn to the selected piece (knight, bishop, rook, queen)
        tileIn.getTileButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertedPiece = pieceFactory.createPiece(tileIn.getPiece().getType(), tileIn.getPiece().getColor());
            }
        });
    }


    // Adds listener for the board clicks
    public void addMoveListener(ActionListener listenerForBoardClick) {

        // sets the board click listener property to the inputted listener
        this.listenerForBoardClick = listenerForBoardClick;

        for (Tile tile : tileList) {

            // Adds listener to the tile
            tile.getTileButton().addActionListener(listenerForBoardClick);

            // Adds anonymous listener that sets the selectedTileCoordinates to the tile that was selected
            tile.getTileButton().addActionListener( new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedTileCoordinates[0] = tile.getRowCoordinate();
                    selectedTileCoordinates[1] = tile.getColCoordinate();
                }
            });
        }
    }

    // Resets the properties of the view
    public void resetViewProperties() {

        tileList = new ArrayList<Tile>();
        board = ChessLib.initializeBoard();
        availableTilesList = new ArrayList<int[]>();
    
        
        turnColor = "White";
        selectedTileCoordinates = new int[2];
        gameStatus = "LewisChess         ";
        convertedPiece = null;
        isPawnAtEnd = false;
        lostWhitePieces = new ArrayList<Tile>();
        lostBlackPieces = new ArrayList<Tile>();
        isResetClicked = false;
    }

    // Creates the options panel and returns it
    JPanel createOptionsPanel(String turnColor) {

        JPanel newOptionsPanel = new JPanel();

        newOptionsPanel.setPreferredSize(new Dimension(50, 60));
        newOptionsPanel.setLayout(new GridLayout());

        PieceFactory pieceFactory = new PieceFactory();

        /* If a piece moves, the turn is usually over. However, if a pawn reaches the end, the
        turn is not over because the pawn must be converted. So, the turn must be switched back. */
        switch(turnColor) {
            case "White":
                turnColor = "Black";
                break;
            case "Black":
                turnColor = "White";
                break;
        }

        // Adds listener to each option so that it can be selected.
        Tile rookTile = new Tile(pieceFactory.createPiece("Rook", turnColor), new Color(255,255, 255));
        addPawnConversionListener(rookTile);

        Tile knightTile = new Tile(pieceFactory.createPiece("Knight", turnColor), new Color(255,255, 255));
        addPawnConversionListener(knightTile);

        Tile bishopTile = new Tile(pieceFactory.createPiece("Bishop", turnColor), new Color(255,255, 255));
        addPawnConversionListener(bishopTile);

        Tile queenTile = new Tile(pieceFactory.createPiece("Queen", turnColor), new Color(255,255, 255));
        addPawnConversionListener(queenTile);

        // Adds the options to the options panel (rook, knight, bishop, queen)
        newOptionsPanel.add(rookTile);
        newOptionsPanel.add(knightTile);
        newOptionsPanel.add(bishopTile);
        newOptionsPanel.add(queenTile);

        return newOptionsPanel;
    }
    
    // Panel indicating the games status, such as whose turn it is, whose in check, etc
    class gameStatusPanel extends JPanel {

        gameStatusPanel() {
            setPreferredSize(new Dimension(100, 60));
            setBackground(new Color(165,185, 210));
        }

        // Displays the status
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            final int FONT_SIZE = 40;
    
            g.setColor(new Color(125,0,0));
            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
            g.drawString(getGameStatus(), panelWidth/2 - getGameStatus().length()/2 * FONT_SIZE/4, 3*panelHeight/4);
        }
    }

    // Blank panel whose color and size can be customized. Used to fill in some parts of a border layout.
    class BlankPanel extends JPanel {

        BlankPanel(Color colorIn, int width, int height) {

            setPreferredSize(new Dimension(width, height));
            setBackground(colorIn);

        }
    }

    class ResetButtonPanel extends JPanel {

        ResetButtonPanel() {

            setPreferredSize(new Dimension(100, 70));
            setBackground(new Color(125,0, 0));

            // Button design
            JButton resetButton = new JButton("Reset");
            resetButton.setPreferredSize(new Dimension(75, 45));
            resetButton.setBackground(Color.LIGHT_GRAY);
            resetButton.setBorder(BorderFactory.createEmptyBorder());

            // Adds listener so that the controller knows when the reset button is clicked
            resetButton.addActionListener(listenerForBoardClick);
            resetButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    // If reset is clicked, the boolean isResetClicked is set to true so that the controller hands it
                    isResetClicked = true;
                }
            });

            add(resetButton);
        }
    }
}