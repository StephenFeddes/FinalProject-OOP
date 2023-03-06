import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;

public class ChessModel {

    private final int BOARD_SIZE = 8;
    private int movesIndex = 0;
    private int[][] moves = new int[2][2];
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<int[]> currentPossibleMovesList = new ArrayList<int[]>();
    private boolean displayIsReady = false;
    public String currentTurn = "White";
    private ArrayList<int[]> testMovesList = new ArrayList<int[]>();

    public ChessPiece[][] getBoard() { return board; }
    public boolean getDisplayIsReady() { return displayIsReady; }
    public ArrayList<int[]> getCurrentPossibleMovesList() { return currentPossibleMovesList; }

    public ChessModel() {
        board = ChessController.initializeBoard();
    }

    public void addMove(int[] selectionCoordinates) {

        ChessPiece selectedPiece = new Empty();

        moves[movesIndex] = selectionCoordinates.clone(); // Shallow copy

        selectedPiece = board[moves[0][0]][moves[0][1]];

        testMoves(selectedPiece);
        
        boolean placementIsAcceptable = false;

        if (movesIndex == 1) {

            currentPossibleMovesList.clear();

            for (int[] coordinates : testMovesList) {

                if (Arrays.equals(coordinates, moves[1])) {

                    placementIsAcceptable = true;
                }
            }

        } 

        if (placementIsAcceptable) {

            board[moves[1][0]][moves[1][1]] = selectedPiece;

            board[moves[0][0]][moves[0][1]] = new Empty();

            switch (currentTurn) {
                case "White":
                    currentTurn = "Black";
                    break;
    
                case "Black":
                    currentTurn = "White";
                    break;
            }

            movesIndex = 0;

        } else if (selectedPiece.getPieceColor().equals(currentTurn) && movesIndex==0) {

            currentPossibleMovesList = testMovesList;

            movesIndex = 1;

        } else if (board[moves[1][0]][moves[1][1]].getPieceColor().equals(currentTurn)) {

                currentPossibleMovesList.clear();

                moves[0] = moves[1];

                selectedPiece = board[moves[1][0]][moves[1][1]];
                
                testMoves(selectedPiece);
                
                currentPossibleMovesList = testMovesList;

                movesIndex = 1;
            
        } else if (selectedPiece.getPieceColor().equals(currentTurn)) {

                currentPossibleMovesList.clear();

                currentPossibleMovesList = testMovesList;
        }
    }

    public boolean isCheckmate(ChessPiece[][] boardIn, String currentTurn) {

        final int BOARD_SIZE = 8;
        for (int row=0; row < BOARD_SIZE; row++) {
            for (int col=0; col < BOARD_SIZE; col++) {

                testMoves(boardIn[row][col]);

                if (boardIn[row][col].getPieceColor() != currentTurn && !testMovesList.isEmpty()) {
                    testMovesList.clear();
                    return false;
                }
            }
        }

        testMovesList.clear();
        return true;
    }

    public void testMoves(ChessPiece piece) {

        testMovesList = piece.possibleMovesList(board, moves[0]);

        testMovesList.clear();

        for (int[] possibleMove : piece.possibleMovesList(board, moves[0])) {

            ChessPiece[][] testBoard = deepCopyBoard(board);

            testBoard[possibleMove[0]][possibleMove[1]] = piece;
            testBoard[moves[0][0]][moves[0][1]] = new Empty();
                
            if (!isKingInCheck(testBoard)) {
                
                testMovesList.add(possibleMove);
            }
        }

    }

    public ChessPiece[][] deepCopyBoard(ChessPiece[][] boardIn) {

        final int BOARD_SIZE = 8;
        ChessPiece[][] deepCopiedBoard = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        PieceFactory pieceFactory = new PieceFactory();

        for (int row=0; row<BOARD_SIZE; row++) {
            for (int col=0; col<BOARD_SIZE; col++) {

                ChessPiece currentPiece = boardIn[row][col];
                deepCopiedBoard[row][col] = pieceFactory.createPiece(currentPiece.getPieceType(), currentPiece.getPieceColor());
            }
        }

        return deepCopiedBoard;
    }

    public boolean isKingInCheck(ChessPiece[][] boardIn) {

        int BOARD_SIZE = 8;

        for (int row=0; row < BOARD_SIZE; row++ ) {
            for (int col=0; col < BOARD_SIZE; col++) {

                int[] pieceCoordinates = new int[2];
                ChessPiece piece = boardIn[row][col];
                pieceCoordinates[0] = row;
                pieceCoordinates[1] = col;


                if (ChessController.isCoordinatesInArrayList(getKingCoordinates(boardIn, currentTurn), piece.possibleMovesList(boardIn, pieceCoordinates))) {
                    return true;
                }
            }
        }

        return false;
    }

    public int[] getKingCoordinates(ChessPiece[][] boardIn, String pieceColor) {
    
        int[] kingCoordinates = new int[2];

        int BOARD_SIZE = 8;
        for (int row=0; row < BOARD_SIZE; row++) {
            for (int col=0; col < BOARD_SIZE; col++) {

                if (boardIn[row][col].getPieceType() == "King" && boardIn[row][col].getPieceColor() == pieceColor) {

                    kingCoordinates[0] = row;
                    kingCoordinates[1] = col;
                }
            }
        }

        return kingCoordinates;
    }
}

    