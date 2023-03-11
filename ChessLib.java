import java.util.ArrayList;

public class ChessLib {

    // Checks if the inputted coordinates are contained by the given ArrayList
    public static boolean isCoordinatesInArrayList(int[] coordinates, ArrayList<int[]> availableTilesList) {

        for (int[] availableTileCoordinates : availableTilesList) {

            if (availableTileCoordinates[0]==coordinates[0] && availableTileCoordinates[1]==coordinates[1]) {

                return true;
            }
        }

        return false;
    }

    // Deep copies the board
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
    public static ChessPiece[][] initializeBoard() {
        final int BOARD_SIZE = 8;
        ChessPiece[][] initBoard = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    
        // Initial setup for the black pieces
        initBoard[0][0] = initBoard[0][7] = new Rook("Black");
        initBoard[0][1] = initBoard[0][6] = new Knight("Black");
        initBoard[0][2] = initBoard[0][5] = new Bishop("Black");
        initBoard[0][3] = new Queen("Black");
        initBoard[0][4] = new King("Black");
        for (int col=0; col < BOARD_SIZE; col++) {
            initBoard[1][col] = new Pawn("Black");
        }
    
        // Initial empty spaces
        for (int row=2; row < BOARD_SIZE; row++) {
            for (int col=0; col < BOARD_SIZE; col++) {
                initBoard[row][col] = new Empty();
            }
        }
    
        // Initial setup for the white pieces
        initBoard[7][0] = initBoard[7][7] = new Rook("White");
        initBoard[7][1] = initBoard[7][6] = new Knight("White");
        initBoard[7][2] = initBoard[7][5] = new Bishop("White");
        initBoard[7][3] = new Queen("White");
        initBoard[7][4] = new King("White");
        for (int col=0; col < BOARD_SIZE; col++) {
            initBoard[6][col] = new Pawn("White");
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