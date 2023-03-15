import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.File;
import java.awt.Color;

public class ChessLib {

    public static void playAudio(String audioFilePath) {

        try {
        File file = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        } catch(Exception e) {}
    }

    // Flips the turn color
    public static String flipTurnColor(String turnColor) {
        switch (turnColor) {
            case "White":
                return "Black";

            case "Black":
                return "White";
            default:
                return "N/A";
        }
    }

    // Checks if the inputted coordinates are contained by the given ArrayList
    public static boolean isCoordinatesInArrayList(int[] coordinates, ArrayList<int[]> availableTilesList) {

        for (int[] availableTileCoordinates : availableTilesList) {

            if (availableTileCoordinates[0]==coordinates[0] && availableTileCoordinates[1]==coordinates[1]) {

                return true;
            }
        }

        return false;
    }

    // Returns a deep copy of the inputted board
    public static ChessPiece[][] deepCopyBoard(ChessPiece[][] boardIn) {

        final int BOARD_SIZE = 8;
        ChessPiece[][] deepCopiedBoard = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        PieceFactory pieceFactory = new PieceFactory();

        for (int row=0; row<BOARD_SIZE; row++) {
            for (int col=0; col<BOARD_SIZE; col++) {

                ChessPiece currentPiece = boardIn[row][col];
                deepCopiedBoard[row][col] = pieceFactory.createPiece(currentPiece.getType(), currentPiece.getColor());
            }
        }

        return deepCopiedBoard;
    }

    // Creates the initial chess board and returns it
    public static ChessPiece[][] initializeBoard(String initialTurnColor) {
        final int BOARD_SIZE = 8;
        ChessPiece[][] initBoard = new ChessPiece[BOARD_SIZE][BOARD_SIZE];

        String nextTurnColor = flipTurnColor(initialTurnColor);
    
        // Initial setup for the black pieces
        initBoard[0][0] = new Rook(nextTurnColor);
        initBoard[0][7] = new Rook(nextTurnColor);
        initBoard[0][1] = new Knight(nextTurnColor);
        initBoard[0][6] = new Knight(nextTurnColor);
        initBoard[0][2] = new Bishop(nextTurnColor);
        initBoard[0][5] = new Bishop(nextTurnColor);
        initBoard[0][3] = new Queen(nextTurnColor);
        initBoard[0][4] = new King(nextTurnColor);
        for (int col=0; col < BOARD_SIZE; col++) {
            initBoard[1][col] = new Pawn(nextTurnColor);
        }
    
        // Initial empty spaces
        for (int row=2; row < BOARD_SIZE; row++) {
            for (int col=0; col < BOARD_SIZE; col++) {
                initBoard[row][col] = new Empty();
            }
        }
    
        // Initial setup for the white pieces
        initBoard[7][0] = new Rook(initialTurnColor);
        initBoard[7][7] = new Rook(initialTurnColor);
        initBoard[7][1] = new Knight(initialTurnColor);
        initBoard[7][6] = new Knight(initialTurnColor);
        initBoard[7][2] = new Bishop(initialTurnColor);
        initBoard[7][5] = new Bishop(initialTurnColor);
        initBoard[7][3] = new Queen(initialTurnColor);
        initBoard[7][4] = new King(initialTurnColor);
        for (int col=0; col < BOARD_SIZE; col++) {
            initBoard[6][col] = new Pawn(initialTurnColor);
        }
    
        return initBoard;
    }
}

class PieceFactory {
    public ChessPiece createPiece(String pieceType, String pieceColor)
    {
        if (pieceType == null || pieceType.isEmpty())
            return null;
        switch (pieceType) {
        case "Empty":
            return new Empty();
        case "Pawn":
            return new Pawn(pieceColor);
        case "Knight":
            return new Knight(pieceColor);
        case "Bishop":
            return new Bishop(pieceColor);
        case "Rook":
            return new Rook(pieceColor);
        case "Queen":
            return new Queen(pieceColor);
        case "King":
            return new King(pieceColor);
        default:
            throw new IllegalArgumentException("Unknown "+pieceType);
        }
    }
}

abstract class ChessPiece {

    // Properties
    public String type;
    private String color;
    private ImageIcon pieceImage;
    public boolean isUnmoved = true;
    public boolean canCastle = false;
    
    // Getters
    public ImageIcon getImage() { return pieceImage; };
    public String getType() { return type; }
    public String getColor() { return color; }

    // Setters
    public void setImage(ImageIcon pieceImage) { this.pieceImage = pieceImage; }
    public void setType(String pieceType) { this.type = pieceType; }
    public void setColor(String pieceColor) { this.color = pieceColor; }

    public ChessPiece(String pieceColor) {
        setColor(pieceColor);
    }

    public ChessPiece() {}

    abstract ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates);

    /* Gets coordinates based on the input and adds them to the list of possible coordinates a piece could go. 
    If isOneCoordinate is false, all the tile coordinates are consecutively added in the inputted direction
    until a tile is reached that cannot be added. If is true, only one coordinate is added at most, period. */
    public void findCoordinates(ChessPiece[][] boardIn, int[] pieceCoordinatesIn, ArrayList <int[]> possibleCoordinatesListIn, int colDirection, int rowDirection, boolean isOneCoordinate) {
        
        try {
        
            int[] possibleCoordinates = pieceCoordinatesIn.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+colDirection;
            possibleCoordinates[1] = possibleCoordinates[1]+rowDirection;
            ChessPiece currentPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];

            // Keeps checking tiles in the direction of colDirection, rowDirection until it reaches a non-empty tile
            while (currentPiece.getColor() != getColor()) {
    
                possibleCoordinatesListIn.add(possibleCoordinates.clone());

                if (isOneCoordinate) {
                    break;
                }

                possibleCoordinates[0] = possibleCoordinates[0]+colDirection;;
                possibleCoordinates[1] = possibleCoordinates[1]+rowDirection;

                // If the current piece added is a piece from the opposing side, stop adding in this direction
                if (currentPiece.getColor() != getColor() && currentPiece.getType() != "Empty") {
                    break;
                }

                // If next piece is the same color as the selected piece, stop the loop. Do not include that piece's coordinates.
                currentPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
    }
}

class Bishop extends ChessPiece {

    public Bishop(String bishopColor) {
        super.setColor(bishopColor);
        setType("Bishop");

        switch(bishopColor) {
            case "Black":
                setImage(new ImageIcon("ChessData/blackBishop.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessData/whiteBishop.png"));
                break;
        }
    }
    
    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();

        // Builds the list of coordinates the bishop could go
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, 1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, -1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, -1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, 1, false);

        return possibleCoordinatesList;
    }
}

// The "Empty" piece is used for checking if a tile is empty or not
class Empty extends ChessPiece {

    public Empty() {
        setType("Empty");
        
        setColor("Null");
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {
        
        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}

class King extends ChessPiece {

    public King(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("King");

        switch(rookColor) {
            case "Black":
                setImage(new ImageIcon("ChessData/blackKing.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessData/whiteKing.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        // Builds the list of coordinates the king could go
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 0, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 0, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 0, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 0, -1, true);

        return possibleNextLocations;
        }
}

class Knight extends ChessPiece {

    public Knight(String knightColor) {
        super(knightColor);
        setColor(knightColor);
        setType("Knight");

        switch(knightColor) {
            case "Black":
                setImage(new ImageIcon("ChessData/blackKnight.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessData/whiteKnight.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        // Builds the list of coordinates the knight could go
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, -2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, -2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 2, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 2, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -2, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -2, 1, true);
        
        return possibleNextLocations;
    }
}

class Pawn extends ChessPiece {

    public Pawn(String pawnColor) {
        super(pawnColor);
        setColor(pawnColor);
        setType("Pawn");

        switch(pawnColor) {
            case "Black":
                setImage(new ImageIcon("ChessData/blackPawn.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessData/whitePawn.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] boardIn, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();
        int[] possibleCoordinates = new int[2];

        // On every turn, the player, regardless of their color, can only move their pawn upwards on the board
        final int moveDirection = -1;

        // Gets the moves the pawn can make that are in front of it
        try {
            ChessPiece pieceInFront1 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]]; 

            // Checks if the pawn can move at least one tile forward. A pawn can only move forward if the tiles in front are empty
            if (pieceInFront1.getType() == "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleCoordinatesList.add(possibleCoordinates.clone());

                // Checks to see if the pawn can move two tiles forward. If a pawn has already moved, it cannot move forward twice
                ChessPiece pieceInFront2 = boardIn[pieceCoordinates[0] + 2*moveDirection][pieceCoordinates[1]];
                if (pieceInFront2.getType() == "Empty" && isUnmoved) {
                    
                    possibleCoordinates[1] = pieceCoordinates[1];
                    possibleCoordinates[0] = pieceCoordinates[0] + 2*moveDirection;
                    possibleCoordinatesList.add(possibleCoordinates.clone());
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        /* Checks one diagonally adjacent tile in front of the pawn to see if it
        contains an enemy piece. If it does, add that tile's coordinates to the list */
        try {
            ChessPiece pieceInDiagonal1 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] + moveDirection];
            if (pieceInDiagonal1.getColor() != getColor() && pieceInDiagonal1.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] + moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }

        } catch(ArrayIndexOutOfBoundsException e) {}

        // Checks the other diagonally adjacent tile in front of the pawn
        try {

            ChessPiece pieceInDiagonal2 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] - moveDirection];
            if (pieceInDiagonal2.getColor() != getColor() && pieceInDiagonal2.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] - moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        return possibleCoordinatesList;
        } 
    }

    class Queen extends ChessPiece {

        public  Queen(String queenColor) {
            super(queenColor);
            setColor(queenColor);
            setType("Queen");
    
            switch(queenColor) {
                case "Black":
                    setImage(new ImageIcon("ChessData/blackQueen.png"));
                    break;
    
                case "White":
                    setImage(new ImageIcon("ChessData/whiteQueen.png"));
                    break;
            }
        }
    
        public Queen() {}
    
        public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {
    
            // Delegation pattern. Queen contains all the moves a bishop and rook can make.
            Rook rook = new Rook(getColor());
            Bishop bishop = new Bishop(getColor());
            ArrayList<int[]> possibleCoordinatesList = rook.possibleMovesList(chessBoard, pieceCoordinates);
            possibleCoordinatesList.addAll(bishop.possibleMovesList(chessBoard, pieceCoordinates));
    
            return possibleCoordinatesList;
        }
    }

    class Rook extends ChessPiece {

        public Rook(String rookColor) {
            super(rookColor);
            setColor(rookColor);
            setType("Rook");
    
            switch(rookColor) {
                case "Black":
                    setImage(new ImageIcon("ChessData/blackRook.png"));
                    break;
    
                case "White":
                    setImage(new ImageIcon("ChessData/whiteRook.png"));
                    break;
            }
        }
    
        public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {
    
            ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();
    
            // Builds the list of coordinates the rook could go
            findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, 0, false);
            findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, 0, false);
            findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 0, 1, false);
            findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 0, -1, false);
            
            return possibleCoordinatesList;
        }
    }

class Tile extends JPanel {
    
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