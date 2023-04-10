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
    public ResetButtonPanel resetButtonPanel = new ResetButtonPanel();
    private JPanel gameMenuPanel = new JPanel();
    private ArrayList<Tile> tileList = new ArrayList<Tile>(); // List of tiles in the current board
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE]; // 2D array of the board's pieces
    private ArrayList<int[]> availableTilesList = new ArrayList<int[]>(); // List of tiles a player's selected piece can go or selected
    private String turnColor = "White";  // String indicating whose turn it currently is
    private int[] selectedTileCoordinates = new int[2]; // Coordinates of the last board tile the user clicked on
    private String gameStatus; // Message showing check, checkmate, or current turn
    private ChessPiece convertedPiece; // If a pawn reaches end of board, it gets converted to the chosen type
    private Container contentPane = getContentPane(); // Content the JFrame will ultimately display
    private ChessPiece selectedPiece;
    private int whiteSecondsLeft = 1800; // Standard amount of seconds for a player
    private int blackSecondsLeft = 1800;
    private int initialSecondsLeft = 1800;
    private String firstPlayer = "White";
    public MenuButton beginButton = new MenuButton("");
    public ArrayList<Tile> lostWhitePieces = new ArrayList<Tile>(); // List of white pieces lost
    public ArrayList<Tile> lostBlackPieces = new ArrayList<Tile>(); // List of black pieces lost
    public ClockPanel whiteClockPanel = new ClockPanel("White");
    public ClockPanel blackClockPanel = new ClockPanel("Black");
    public boolean isResetClicked = false; // If reset is clicked, the board is reset to the initial state
    public boolean isPawnAtEnd = false; // If a pawn reaches the end, new piece options are displayed
    public boolean isBoardFlipping = false;
    public boolean gameOn = false;
    public boolean isFirstTurn = true;
    public boolean isTimerOn = true;
    public boolean isTimerOver = false;
    public ActionListener controllerListener; // The controller that will listen to the button click

    // Getters
    public int getInitialSecondsLeft() { return initialSecondsLeft; }
    public int getWhiteSecondsLeft() { return whiteSecondsLeft; }
    public int getBlackSecondsLeft() { return blackSecondsLeft; }
    public ChessPiece getSelectedPiece() { return selectedPiece; }
    public ChessPiece getConvertedPiece() { return convertedPiece; }
    public String getTurnColor() { return turnColor; }
    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public int[] getSelectedTileCoordinates() { return selectedTileCoordinates; }

    // Setters
    public void setInitialSecondsLeft(int initialSecondsLeft) { this.initialSecondsLeft = initialSecondsLeft; }
    public void setWhiteSecondsLeft(int whiteSecondsLeft) { this.whiteSecondsLeft = whiteSecondsLeft; }
    public void setBlackSecondsLeft(int blackSecondsLeft) { this.blackSecondsLeft = blackSecondsLeft; }
    public void setBoard(ChessPiece[][] boardIn) { board = boardIn; }
    public void setSelectedPiece(ChessPiece selectedPiece) {this.selectedPiece = selectedPiece; }
    public void setLostWhitePieces(ArrayList<Tile> lostWhitePieces) { this.lostWhitePieces = lostWhitePieces; }
    public void setLostBlackPieces(ArrayList<Tile> lostBlackPieces) { this.lostBlackPieces = lostBlackPieces; }
    public void setTurnColor(String turnColor) { this.turnColor = turnColor; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }
    public void setAvailableTilesList(ArrayList<int[]> currentAvailableTiles) { availableTilesList = currentAvailableTiles; }

    public ChessView() {
        
        //board = ChessLib.initializeBoard(); // 

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
        remove(eastPanel);
        remove(westPanel);
        remove(contentPane);
        remove(gameStatusPanel);
        remove(optionsPanel);
        remove(optionsPanel);
        remove(resetButtonPanel);
        remove(gameMenuPanel);
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
        gameStatusPanel = new GameStatusPanel(); // Panel that is contained by the north panel. Provides game messages
        optionsPanel = createOptionsPanel(getTurnColor());
        gameMenuPanel = new GameMenuPanel();

        // Sets up north panel display
        northPanel.setLayout(new BorderLayout());
        northPanel.add(gameStatusPanel, BorderLayout.CENTER);
        northPanel.add(new BlankPanel(new Color(125,0, 0), 110, 60), BorderLayout.EAST);
        northPanel.add(new BlankPanel(new Color(125,0, 0), 110, 60), BorderLayout.WEST);
        if (isPawnAtEnd) {
            // If a pawn reaches the end, the piece options panel is displayed in the north panel
            northPanel.add(optionsPanel, BorderLayout.CENTER);
        }
        if (!gameOn) {
            northPanel.add(gameMenuPanel);
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
            // Adds the latest lost black piece to the west panel
            westPanel.add(lostBlackPiece);
        }
            

        // Panel containing the board
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBackground(new Color(125,0, 0));
        boardPanel.setBorder(BorderFactory.createLineBorder(new Color(125,0, 0), 2));

        // Panel containing all the panels, such as the board, south panel, etc
        gamePanel.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit(). getScreenSize();
        gamePanel.setBounds((int)screenSize.getWidth()/4 - 8, 0,  700, 650);
        gamePanel.add(northPanel, BorderLayout.NORTH);
        gamePanel.add(southPanel, BorderLayout.SOUTH);
        gamePanel.add(eastPanel, BorderLayout.EAST);
        gamePanel.add(westPanel, BorderLayout.WEST);
        if (gameOn) {
            gamePanel.add(boardPanel, BorderLayout.CENTER);
        } else {
            gamePanel.add(new JPanel() { protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                final int FONT_SIZE = 40;

                setBackground(new Color(255, 255, 255));
        
                g.setColor(new Color(125,0,0));
                g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
                g.drawString("LewisChess         ", panelWidth/2 - "LewisChess         ".length()/2 * FONT_SIZE/4, panelHeight/2);
            }}, BorderLayout.CENTER);
        }

        // Content that the JFrame will display
        contentPane.setLayout(null);
        contentPane.add(gamePanel); // Adds the whole game panel to the center
        contentPane.add(blackClockPanel);
        contentPane.add(whiteClockPanel);
        contentPane.setBackground(new Color(125,0,0));

        Color tileColor;
        Tile newTile;
        int tileCoordinates[] = new int[2];
        // Creates each tile by coloring it and putting a piece on it
        // Adds the tiles to be displayed to the board panel
        for (int row=0; row < BOARD_SIZE; row++) {
            if (!gameOn) {
                break;
            }
            for(int col=0; col < BOARD_SIZE; col++) {

                tileCoordinates[0] = row;
                tileCoordinates[1] = col;
                
                // Colors the tile either beige or green
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

                        // Colors the tile light green if it is empty, which means the selected piece can move there
                        tileColor = new Color(125,200,125);
                    }

                    // If the selected piece is a king, color the rooks it can castle with yellow
                    if (getSelectedPiece().getType().equals("King") && boardIn[row][col].canCastle) {

                        tileColor = new Color(200,200,50);
                    }
                }

                // Tile contains the appropriate piece from the 2D chess piece board array
                newTile = new Tile(boardIn[row][col], tileColor); // Creates an updated tile to be displayed by the view

                // Sets the coordinates for the tile
                newTile.setRowCoordinate(row);
                newTile.setColCoordinate(col);

                /* Adds tile to an ArrayList of tiles and to the board panel. The board panel displays it.
                The Arrraylist contains the tiles that will be listened to by the controller. */
                if (gameOn) {
                    tileList.add(newTile);
                    boardPanel.add(newTile);
                }

                /* If a pawn reaches the end, the board becomes unresponsive until that pawn is converted. 
                If its checkmate, it becomes permanently unresponsive until the game is reset */
                if (!isPawnAtEnd && !getGameStatus().contains("Checkmate") && !isBoardFlipping && !isTimerOver) {
          
                    listenToTile(newTile);
            }
        }
    }
        // Redisplays the JFrame
        SwingUtilities.updateComponentTreeUI(this);
    }

    // Takes an input tile for the controller to listen to
    public void listenToTile(Tile tileIn) {

        tileIn.getTileButton().addActionListener(controllerListener);

        tileIn.getTileButton().addActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedTileCoordinates[0] = tileIn.getRowCoordinate();
                selectedTileCoordinates[1] = tileIn.getColCoordinate();
            }
        });
    }

    // Adds listener (the controller) for the board clicks
    public void addTileListener(ActionListener listenerForTileClick) {

        // Sets the board click listener property to the inputted listener
        this.controllerListener = listenerForTileClick;

        for (Tile tile : tileList) {

            // Adds listener to the tile
            tile.getTileButton().addActionListener(listenerForTileClick);

            // Adds anonymous listener that sets the selectedTileCoordinates to the tile that was selected
            tile.getTileButton().addActionListener( new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectedTileCoordinates[0] = tile.getRowCoordinate();
                    selectedTileCoordinates[1] = tile.getColCoordinate();
                }
            });
        }
    }

    public void addBeginButtonListener(ActionListener listenerForBeginButton) {

        this.controllerListener = listenerForBeginButton;
        beginButton.addActionListener(listenerForBeginButton);
    }

    // Adds actions listeners for the options panel
    public void addPawnConversionListener(Tile tileIn) {
        PieceFactory pieceFactory = new PieceFactory();

        // Controller listens for an option click
        tileIn.getTileButton().addActionListener(controllerListener);

        // Converts the pawn to the selected piece (knight, bishop, rook, queen)
        tileIn.getTileButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                ChessLib.playAudio("ChessData/buttonClicked.wav");
        
                convertedPiece = pieceFactory.createPiece(tileIn.getPiece().getType(), tileIn.getPiece().getColor());
            }
        });
    }

    // Resets the properties of the view
    public void resetViewProperties() {

        tileList = new ArrayList<Tile>();
        availableTilesList = new ArrayList<int[]>();
        selectedTileCoordinates = new int[2];
        gameStatus = null;
        convertedPiece = null;
        isPawnAtEnd = false;
        lostWhitePieces = new ArrayList<Tile>();
        lostBlackPieces = new ArrayList<Tile>();
        //isResetClicked = false;
        isBoardFlipping = false;
        gameOn = false;
        isFirstTurn = true;
        isTimerOver = false;
    }

    // Creates the options panel and returns it
    JPanel createOptionsPanel(String turnColor) {

        JPanel newOptionsPanel = new JPanel();

        newOptionsPanel.setPreferredSize(new Dimension(50, 60));
        newOptionsPanel.setLayout(new GridLayout());

        PieceFactory pieceFactory = new PieceFactory();

        /* If a piece moves, the turn is usually over. However, if a pawn reaches the end, the
        turn is not over because the pawn must be converted. So, the turn must be switched back. */
    
        String pieceColor = ChessLib.flipTurnColor(getTurnColor());

        // Adds listener to each option so that it can be selected.
        Tile rookTile = new Tile(pieceFactory.createPiece("Rook", pieceColor), new Color(255,255, 255));
        addPawnConversionListener(rookTile);

        Tile knightTile = new Tile(pieceFactory.createPiece("Knight", pieceColor), new Color(255,255, 255));
        addPawnConversionListener(knightTile);

        Tile bishopTile = new Tile(pieceFactory.createPiece("Bishop", pieceColor), new Color(255,255, 255));
        addPawnConversionListener(bishopTile);

        Tile queenTile = new Tile(pieceFactory.createPiece("Queen", pieceColor), new Color(255,255, 255));
        addPawnConversionListener(queenTile);

        // Adds the options to the options panel (rook, knight, bishop, queen)
        newOptionsPanel.add(rookTile);
        newOptionsPanel.add(knightTile);
        newOptionsPanel.add(bishopTile);
        newOptionsPanel.add(queenTile);

        return newOptionsPanel;
    }

    // Panel used for displaying the game's clocks
    class ClockPanel extends JPanel {

        int x;
        int seconds;
        String playerColor;

        public ClockPanel(String playerColor) {

            Dimension screenSize = Toolkit.getDefaultToolkit(). getScreenSize();
            this.playerColor = playerColor;

            if (playerColor == "White") {
                x = 0;
                seconds = getWhiteSecondsLeft();
            } else {
                x = (int)screenSize.getWidth() - (int)screenSize.getWidth()/4 + 8;
                seconds = getBlackSecondsLeft();
            }

            setBounds(x, (int)screenSize.getHeight()/4, (int)screenSize.getWidth()/4-8, (int)screenSize.getHeight()/2);
            setBackground(new Color(125, 0, 0));
        }

        public ClockPanel() {}

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            int panelHeight = getHeight();
            final int FONT_SIZE = 30;

            String minutesPart = "" + seconds / 60;
            String secondsPart = "" + seconds % 60;
            if (seconds % 60 < 10) {
                secondsPart = "0" + secondsPart;
            }
            if (seconds / 60 < 10) {
                minutesPart = "0" + minutesPart;
            }
            String time = minutesPart + ":" + secondsPart;

            Dimension screenSize = Toolkit.getDefaultToolkit(). getScreenSize();
            if (isTimerOn) {
                g.setColor(new Color(255 ,255,255));
            } else {
                g.setColor(new Color(125, 0, 0));
            }
            g.fillOval(0, 0, (int)screenSize.getWidth()/4 - 8, (int)screenSize.getWidth()/4 - 8);
            g.setColor(new Color(125,0,0));
            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
            g.drawString(playerColor + "'s remaining time: ", 12, (int)screenSize.getHeight()/6);
            g.drawString(time, (int)screenSize.getWidth()/8 - 40, panelHeight/2);
        }

        public void updateSeconds(int seconds) {
            this.seconds = seconds;
            repaint();
        }
    }

    // Panel containing the options a player can choose before beginning a game
    class GameMenuPanel extends JPanel {
        
        public GameMenuPanel() {
            setPreferredSize(new Dimension(100, 60));
            setLayout(new GridLayout());

            MenuButton increaseTimeButton = new MenuButton("<html>" + "Increment" + "<br>" + "<center>" + "Time" + "</center>" + "</html");
            increaseTimeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    ChessLib.playAudio("ChessData/buttonClicked.wav");

                    initialSecondsLeft += 300; // Increments 5 minutes
                    // Amount of seconds left cannot exceed 100 minutes
                    if (initialSecondsLeft > 5700) {
                        initialSecondsLeft = 5700;
                    }
                    blackSecondsLeft = initialSecondsLeft; 
                    whiteSecondsLeft = initialSecondsLeft;

                    blackClockPanel.updateSeconds(getBlackSecondsLeft());
                    whiteClockPanel.updateSeconds(getWhiteSecondsLeft());
                }
            });
            add(increaseTimeButton);

            MenuButton decreaseTimeButton = new MenuButton("<html>" + "Decrement" + "<br>" + "<center>" + "Time" + "</center>" + "</html");
            decreaseTimeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    ChessLib.playAudio("ChessData/buttonClicked.wav");

                    initialSecondsLeft -= 300; // Decrements 5 minutes
                    if (initialSecondsLeft <= 0) {
                        initialSecondsLeft = 300;
                    }
                    blackSecondsLeft = initialSecondsLeft; 
                    whiteSecondsLeft = initialSecondsLeft;

                    blackClockPanel.updateSeconds(getBlackSecondsLeft());
                    whiteClockPanel.updateSeconds(getWhiteSecondsLeft());
                }
            });
            add(decreaseTimeButton);

            MenuButton timerButton;
            if (isTimerOn) {
                timerButton = new MenuButton("Timer: On");
            } else {
                timerButton = new MenuButton("Timer: Off");
            }
            timerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    ChessLib.playAudio("ChessData/buttonClicked.wav");

                    if (isTimerOn) {
                        isTimerOn = false;
                        timerButton.setText("Timer: Off");
                        blackClockPanel.repaint();
                        whiteClockPanel.repaint();
                    } else {
                        isTimerOn = true;
                        timerButton.setText("Timer: On");
                        blackClockPanel.repaint();
                        whiteClockPanel.repaint();
                    }
                }
            });
            add(timerButton);

            MenuButton chooseFirstColorButton = new MenuButton("<html>" + "Set turn:" + "<br>" + "<center>" + firstPlayer + "</center>" + "</html");
            chooseFirstColorButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    ChessLib.playAudio("ChessData/buttonClicked.wav");

                    setTurnColor(ChessLib.flipTurnColor(getTurnColor()));
                    firstPlayer = getTurnColor();
                    chooseFirstColorButton.setText("<html>" + "Set turn:" + "<br>" + "<center>" + getTurnColor() + "</center>" + "</html");
                }
            });
            add(chooseFirstColorButton);

            beginButton = new MenuButton("Begin");
            beginButton.addActionListener(controllerListener);
            beginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    ChessLib.playAudio("ChessData/buttonClicked.wav");

                    gameOn = true;
                    if (getTurnColor().contains("White")) {
                        setGameStatus("White's turn        ");
                    } else {
                        setGameStatus("Black's turn        ");
                    }
                   
                    setBoard(ChessLib.initializeBoard(getTurnColor()));
                    isResetClicked = false;
                    displayBoard(getBoard());
                }
            });
            add(beginButton);
        }
    }

    // Panel indicating the games status, such as whose turn it is, whose in check, etc
    class GameStatusPanel extends JPanel {

        GameStatusPanel() {
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

    // Generic menu button. Other buttons inherit from it so that they look similar.
    class MenuButton extends JButton {

        public MenuButton(String buttonText) {
            super(buttonText);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createBevelBorder(5));
        }
    }

    // Panel containing the reset button
    class ResetButtonPanel extends JPanel {

        ResetButtonPanel() {

            setPreferredSize(new Dimension(100, 70));
            setBackground(new Color(125,0, 0));

            // Button design
            MenuButton resetButton = new MenuButton("Reset");
            resetButton.setPreferredSize(new Dimension(75, 45));
            resetButton.setBackground(Color.LIGHT_GRAY);
            resetButton.setBorder(BorderFactory.createEmptyBorder());

            // Cannot select the reset button as the board is flipping
            if (!isBoardFlipping) {
                // Adds listener so that the controller knows when the reset button is clicked
                resetButton.addActionListener(controllerListener);
                resetButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        ChessLib.playAudio("ChessData/buttonClicked.wav");

                        // If reset is clicked, the boolean isResetClicked is set to true so that the controller hands it
                        if (gameOn) {
                            isResetClicked = true;
                        }
                    }
                });
            }
            add(resetButton);
        }
    }
}
