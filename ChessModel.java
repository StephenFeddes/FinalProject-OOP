
public class ChessModel {
    private int moveCount = 0;
    public int[][] moves = new int[2][2];
    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessPiece[][] getBoard() { return board; }

    public ChessModel() {
        board = initializeBoard();
    }

    public ChessPiece[][] addMove(int[] pieceCoordinates) {
        moves[moveCount] = pieceCoordinates;

        moveCount += 1;
        moveCount = moveCount % 2;

        if (moveCount == 2) {
            board[moves[1][0]][moves[1][1]] = board[moves[0][0]][moves[0][1]];

            board[moves[0][0]][moves[0][1]] = new Empty();

            return board;
        }

        return board;
    }

    public static ChessPiece[][] initializeBoard() {
        ChessPiece[][] initBoard = new ChessPiece[8][8];

        // Initial setup for the black pieces
        initBoard[0][0] = initBoard[0][7] = new Rook("Black");
        initBoard[0][1] = initBoard[0][6] = new Knight("Black");
        initBoard[0][2] = initBoard[0][5] = new Bishop("Black");
        initBoard[0][3] = new Queen("Black");
        initBoard[0][4] = new King("Black");
        for (int col=0; col < 8; col++) {
            initBoard[1][col] = new Pawn("Black");
        }

        // Initial empty spaces
        for (int row=2; row < 8; row++) {
            for (int col=0; col < 8; col++) {
                initBoard[row][col] = new Empty();
            }
        }

        // Initial setup for the white pieces
        initBoard[7][0] = initBoard[7][7] = new Rook("White");
        initBoard[7][1] = initBoard[7][6] = new Knight("White");
        initBoard[7][2] = initBoard[7][5] = new Bishop("White");
        initBoard[7][3] = new Queen("White");
        initBoard[7][4] = new King("White");
        for (int col=0; col < 8; col++) {
            initBoard[6][col] = new Pawn("White");
        }

        return initBoard;
    }
}