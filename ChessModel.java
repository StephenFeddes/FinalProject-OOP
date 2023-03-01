import java.util.ArrayList;

public class ChessModel {

    private final int BOARD_SIZE = 8;
    private int movesIndex = 0;
    private int[][] moves = new int[2][2];
    private ChessPiece[][] board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    private boolean displayIsReady = false;

    public ChessPiece[][] getBoard() { return board; }
    public boolean getDisplayIsReady() { return displayIsReady; }

    public ChessModel() {
        board = initializeBoard();
    }

    public void addMove(int[] selectionCoordinates) {

        moves[movesIndex] = selectionCoordinates.clone(); // Shallow copy

        if (movesIndex == 1) {

            board[moves[1][0]][moves[1][1]] = board[moves[0][0]][moves[0][1]];

            board[moves[0][0]][moves[0][1]] = new Empty();

            movesIndex = 0;
            displayIsReady = true;
        } else {
            movesIndex = 1;
            displayIsReady = false;
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