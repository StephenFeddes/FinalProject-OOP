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
    private String currentTurn = "White";

    public ChessPiece[][] getBoard() { return board; }
    public boolean getDisplayIsReady() { return displayIsReady; }
    public ArrayList<int[]> getCurrentPossibleMovesList() { return currentPossibleMovesList; }

    public ChessModel() {
        board = initializeBoard();
    }

    public void addMove(int[] selectionCoordinates) {

        ChessPiece selectedPiece = new Empty();

        moves[movesIndex] = selectionCoordinates.clone(); // Shallow copy

        selectedPiece = board[moves[0][0]][moves[0][1]];
    
        boolean placementIsAcceptable = false;

        if (movesIndex == 1) {

            currentPossibleMovesList.clear();

            for (int[] coordinates : selectedPiece.possibleMovesList(board, moves[0])) {
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

            currentPossibleMovesList = selectedPiece.possibleMovesList(board, moves[0]);
            movesIndex = 1;

        } else if (board[moves[1][0]][moves[1][1]].getPieceColor().equals(currentTurn)) {

            currentPossibleMovesList.clear();

            moves[0] = moves[1];

            currentPossibleMovesList = board[moves[1][0]][moves[1][1]].possibleMovesList(board, moves[0]);

            movesIndex = 1;
        }
        
        else if (selectedPiece.getPieceColor().equals(currentTurn)) {

            currentPossibleMovesList.clear();

            currentPossibleMovesList = selectedPiece.possibleMovesList(board, moves[0]);
        }
    }


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