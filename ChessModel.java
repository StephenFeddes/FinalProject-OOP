import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.event.SwingPropertyChangeSupport;

import java.awt.Color;

public class ChessModel {

    private int moveStep = 0; // Step number of a move. A move has two steps; a selected piece and the destination
    private int[][] steps = new int[2][2]; // Array containing the selected piece coordinates and destination coordinates
    private ChessPiece[][] board = new ChessPiece[8][8];
    private ArrayList<int[]> currentPossibleMovesList = new ArrayList<int[]>();
    public String currentTurn = "White";
    private String gameStatus = "White's turn        ";
    public boolean isPawnAtEnd;
    public ArrayList<Tile> lostWhitePieces = new ArrayList<Tile>();
    public ArrayList<Tile> lostBlackPieces = new ArrayList<Tile>();
    public ArrayList<Tile> getLostWhitePieces() { return lostWhitePieces; }
    public ArrayList<Tile> getLostBlackPieces() { return lostBlackPieces; }
    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public ArrayList<int[]> getCurrentPossibleMovesList() { return currentPossibleMovesList; }

    public void setBoard(ChessPiece[][] boardIn) { board = boardIn; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }

    public ChessModel() {
        board = ChessController.initializeBoard();
    }

    public void addMove(int[] selectionCoordinates) {

        ArrayList<int[]> testMovesList = new ArrayList<int[]>();
        ChessPiece selectedPiece = new Empty();

        steps[moveStep] = selectionCoordinates.clone(); // Shallow copy

        selectedPiece = board[steps[0][0]][steps[0][1]];

        testMovesList = testMoves(selectedPiece, steps[0], board);

        if (selectedPiece.getPieceType() == "King" && selectedPiece.isUnmoved) {

            testMovesList.addAll(getCastlingCoordinates(board, currentTurn));
        }
        
        boolean placementIsAcceptable = false;

        if (moveStep == 1) {

            for (int[] coordinates : testMovesList) {

                if (Arrays.equals(coordinates, steps[1])) {

                    placementIsAcceptable = true;
                }
            }

        } 

        if (placementIsAcceptable) {

            selectedPiece.isUnmoved = false;

            if (board[steps[1][0]][steps[1][1]].getPieceColor() != currentTurn && board[steps[1][0]][steps[1][1]].getPieceType() != "Empty") {

                if (currentTurn == "White") {
                    
                    lostBlackPieces.add(new Tile(board[steps[1][0]][steps[1][1]], new Color(165,185, 210)));

                } else {

                    lostWhitePieces.add(new Tile(board[steps[1][0]][steps[1][1]], new Color(165,185, 210)));
                }

            }

            if (board[steps[1][0]][steps[1][1]].getPieceColor() == currentTurn) {

                castleKing(selectedPiece, steps[1], board);

            } else {

                board[steps[1][0]][steps[1][1]] = selectedPiece;
                board[steps[0][0]][steps[0][1]] = new Empty();
            }

            boolean isWhitePawnAtEnd = selectedPiece.getPieceType() == "Pawn" && steps[1][0] == 0 && selectedPiece.getPieceColor() == "White";
            boolean isBlackPawnAtEnd = selectedPiece.getPieceType() == "Pawn" && steps[1][0] == 7 && selectedPiece.getPieceColor() == "Black";

            if (isWhitePawnAtEnd || isBlackPawnAtEnd) {
                isPawnAtEnd = true;
            }

            switch (currentTurn) {
                case "White":
                    currentTurn = "Black";
                    break;
    
                case "Black":
                    currentTurn = "White";
                    break;
            }

            checkStatus(board);

            currentPossibleMovesList.clear();

            moveStep = 0;

        } else if (selectedPiece.getPieceColor().equals(currentTurn) && moveStep==0) {

            currentPossibleMovesList = testMovesList;
            currentPossibleMovesList.add(steps[0]);

            moveStep = 1;

        } else if (board[steps[1][0]][steps[1][1]].getPieceColor().equals(currentTurn)) {

                currentPossibleMovesList.clear();

                steps[0] = steps[1];
                selectedPiece = board[steps[1][0]][steps[1][1]];
                testMovesList = testMoves(selectedPiece, steps[1], board);

                if (selectedPiece.getPieceType() == "King" && selectedPiece.isUnmoved) {
                    testMovesList.addAll(getCastlingCoordinates(board, currentTurn));
                }
                
                currentPossibleMovesList = testMovesList;
                currentPossibleMovesList.add(steps[1]);
                moveStep = 1;
            
        } else if (selectedPiece.getPieceColor().equals(currentTurn)) {

                currentPossibleMovesList = testMovesList;
                currentPossibleMovesList.add(steps[0]);
        }
    }

    private void castleKing(ChessPiece king, int[] rookCoordinates, ChessPiece[][] boardIn) {

        PieceFactory pieceFactory = new PieceFactory();

        if (rookCoordinates[1] == 7 && king.getPieceColor() == "Black") {

            boardIn[0][6] = king;
            boardIn[0][5] = pieceFactory.createPiece("Rook", "Black");
            boardIn[0][4] = new Empty();
            boardIn[0][7] = new Empty();

        } else if (rookCoordinates[1] == 0 && king.getPieceColor() == "Black") {

            boardIn[0][2] = king;
            boardIn[0][3] = pieceFactory.createPiece("Rook", "Black");
            boardIn[0][4] = new Empty();
            boardIn[0][0] = new Empty();

        } else if (rookCoordinates[1] == 7 && king.getPieceColor() == "White") {

            boardIn[7][6] = king;
            boardIn[7][5] = pieceFactory.createPiece("Rook", "White");
            boardIn[7][4] = new Empty();
            boardIn[7][7] = new Empty();

        } else if (rookCoordinates[1] == 0 && king.getPieceColor() == "White") {

            boardIn[7][2] = king;
            boardIn[7][3] = pieceFactory.createPiece("Rook", "White");
            boardIn[7][4] = new Empty();
            boardIn[7][0] = new Empty();
        }
    }

    private ArrayList<int[]> getCastlingCoordinates(ChessPiece[][] boardIn, String kingColor) {

        ArrayList<int[]> castlingCoordinatesList = new ArrayList<int[]>();
        PieceFactory pieceFactory = new PieceFactory();
        ChessPiece[][] testBoard = deepCopyBoard(boardIn);
        int kingRowNum = 0;

        switch (kingColor) {
            case "White":
                kingRowNum = 7;
                break;
            case "Black":
                kingRowNum = 0;
                break;
        }

        if (boardIn[kingRowNum][5].getPieceType() == "Empty" && boardIn[kingRowNum][6].getPieceType() == "Empty" && boardIn[kingRowNum][7].isUnmoved && boardIn[kingRowNum][7].getPieceType() == "Rook") {

            testBoard[kingRowNum][5] = pieceFactory.createPiece("King", kingColor);
                
            if (!isKingInCheck(testBoard)) {

                testBoard = deepCopyBoard(boardIn);
                testBoard[kingRowNum][6] = pieceFactory.createPiece("King", kingColor);
                testBoard[kingRowNum][5] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[kingRowNum][4] = new Empty();

                if (!isKingInCheck(testBoard)) {

                    int[] rightCastlingCoordinates = new int[2];
                    rightCastlingCoordinates[0] = kingRowNum;
                    rightCastlingCoordinates[1] = 7;

                    castlingCoordinatesList.add(rightCastlingCoordinates);
                }
            }
        }

        if (boardIn[kingRowNum][3].getPieceType() == "Empty" && boardIn[kingRowNum][2].getPieceType() == "Empty" && boardIn[kingRowNum][1].getPieceType() == "Empty") {

            testBoard[kingRowNum][3] = pieceFactory.createPiece("King", kingColor);
            testBoard[kingRowNum][4] = new Empty();
                
            if (!isKingInCheck(testBoard)) {

                testBoard = deepCopyBoard(boardIn);
                testBoard[kingRowNum][2] = pieceFactory.createPiece("King", kingColor);
                testBoard[kingRowNum][3] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[kingRowNum][4] = new Empty();
                testBoard[kingRowNum][0] = new Empty();

                if (!isKingInCheck(testBoard)) {

                    int[] leftCastlingCoordinates = new int[2];
                    leftCastlingCoordinates[0] = kingRowNum;
                    leftCastlingCoordinates[1] = 0;

                    castlingCoordinatesList.add(leftCastlingCoordinates);
                }
            }
        }

        return castlingCoordinatesList;
    }

    public void checkStatus(ChessPiece[][] boardIn) {

        final int BOARD_SIZE = 8;
            boolean cantMove = true;
            for (int row=0; row < BOARD_SIZE; row++) {
                for (int col=0; col < BOARD_SIZE; col++) {

                int[] pieceCoordinates = new int[2];
                pieceCoordinates[0] = row;
                pieceCoordinates[1] = col;

                    if (boardIn[row][col].getPieceColor() == currentTurn && !testMoves(boardIn[row][col], pieceCoordinates, boardIn).isEmpty()) {
                        cantMove = false;
                    }
                }
            }

            if (cantMove && isKingInCheck(boardIn)) {

                String winningColor = null;

                switch(currentTurn) {
                    case "White":
                        winningColor = "Black";
                        break;
                    case "Black":
                        winningColor = "White";
                        break;
                }

                setGameStatus(String.format("Checkmate: %s wins                  ", winningColor));

            } else if (cantMove) {

                setGameStatus("Stalemate           ");

            } else if (isKingInCheck(boardIn)) {

                setGameStatus(String.format("%s in check        ", currentTurn));

            } else {

                setGameStatus(String.format("%s's turn        ", currentTurn));
            }
    }

    private ArrayList<int[]> testMoves(ChessPiece piece, int[] pieceCoordinates, ChessPiece[][] boardIn) {

        ArrayList<int[]> movesList = new ArrayList<int[]>();

        for (int[] possibleMove : piece.possibleMovesList(boardIn, pieceCoordinates)) {

            ChessPiece[][] testBoard = deepCopyBoard(boardIn);

            testBoard[possibleMove[0]][possibleMove[1]] = piece;
            testBoard[pieceCoordinates[0]][pieceCoordinates[1]] = new Empty();
                
            if (!isKingInCheck(testBoard)) {
                
                movesList.add(possibleMove);
            }
        }

        return movesList;
    }

    private ChessPiece[][] deepCopyBoard(ChessPiece[][] boardIn) {

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

    private boolean isKingInCheck(ChessPiece[][] boardIn) {

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

    private int[] getKingCoordinates(ChessPiece[][] boardIn, String pieceColor) {
    
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

    public void resetModelProperties() {

        moveStep = 0;
        steps = new int[2][2];
        board = ChessController.initializeBoard();
        currentPossibleMovesList = new ArrayList<int[]>();
        currentTurn = "White";
        gameStatus = "White's turn        ";
        isPawnAtEnd = false;
        lostWhitePieces = new ArrayList<Tile>();
        lostBlackPieces = new ArrayList<Tile>();
    }
}