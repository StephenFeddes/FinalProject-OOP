import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.event.SwingPropertyChangeSupport;

import java.awt.Color;

public class ChessModel {

    private int moveStep = 0; // Step number of a move. A move has two steps; a selected piece and the destination
    private int[][] steps = new int[2][2]; // Array containing the selected piece coordinates and destination coordinates
    private ChessPiece[][] board = new ChessPiece[8][8];
    private ArrayList<int[]> currentPossibleDestinationsList = new ArrayList<int[]>();
    private String turnColor = "White";
    private String gameStatus = "White's turn        ";
    private ChessPiece selectedPiece;
    private ArrayList<Tile> lostWhitePieces = new ArrayList<Tile>();
    private ArrayList<Tile> lostBlackPieces = new ArrayList<Tile>();
    public boolean isPawnAtEnd;

    // Getters
    public ArrayList<Tile> getLostWhitePieces() { return lostWhitePieces; }
    public ArrayList<Tile> getLostBlackPieces() { return lostBlackPieces; }
    public String getGameStatus() { return gameStatus; }
    public ChessPiece[][] getBoard() { return board; }
    public ArrayList<int[]> getCurrentPossibleDestinationsList() { return currentPossibleDestinationsList; }
    public String getTurnColor() { return turnColor; }
    public ChessPiece getSelectedPiece() { return selectedPiece; }

    // Setters
    public void setSelectedPiece(ChessPiece selectedPiece) { this.selectedPiece = selectedPiece; }
    public void setTurnColor(String turnColor) { this.turnColor = turnColor; }
    public void setBoard(ChessPiece[][] boardIn) { board = boardIn; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }

    public ChessModel() {
        board = ChessLib.initializeBoard(); // Board is initialized when the model is constructed
    }

    public void addMove(int[] selectionCoordinates) {

        // List of moves the selected piece can make without putting their own king in check
        ArrayList<int[]> testMovesList = new ArrayList<int[]>();

        // Array containing the selected piece's coordinates the selected destination's coordinates
        steps[moveStep] = selectionCoordinates.clone();

        setSelectedPiece(board[steps[0][0]][steps[0][1]]);

        /* With the board, selected piece, and the pieces coordinates, testMoves tests
        all the moves the piece can make and filters out ones that result in check */
        testMovesList = testMoves(getSelectedPiece(), steps[0], board);

        // If king is selected, add the castling coordinates to the list of available moves if it is legal
        if (getSelectedPiece().getType() == "King" && getSelectedPiece().isUnmoved) {

            testMovesList.addAll(getCastlingCoordinates(board, getTurnColor()));
        }
        
        // Checks if the user clicks on an appropriate tile the piece can go
        boolean placementIsValid = false;
        if (moveStep == 1) {
            for (int[] coordinates : testMovesList) {

                if (Arrays.equals(coordinates, steps[1])) {

                    placementIsValid = true;
                }
            }

        } 

        // Places the selected piece in the valid destination that was selected
        if (placementIsValid) {

            getSelectedPiece().isUnmoved = false;

            // Piece, whether its the "Empty" piece or an enemy piece, whose tile becomes occupied by the selected piece
            ChessPiece displacedPiece = board[steps[1][0]][steps[1][1]];

            /* If the destination piece is of the same color, it must mean the user chose to castle. 
            There are no other moves where a piece of same color as the selected piece gets displaced. */
            if (displacedPiece.getColor() == getTurnColor()) {

                // Gets the selected piece, which is the king, and castles it with the appropriate rook at the coordinates in steps[1]
                castleKing(getSelectedPiece(), steps[1], board);

            } else {

                /* If castling is not occuring, then the destination tile receieves the selected
                piece and the tile that contained the selected piece becomes empty */
                board[steps[1][0]][steps[1][1]] = getSelectedPiece();
                board[steps[0][0]][steps[0][1]] = new Empty();
            }

            /* If the movement resulted a piece being lost, the lost piece is 
            added to the appropriate list so that the view can display it */
            if (displacedPiece.getColor() != getTurnColor() && displacedPiece.getType() != "Empty") {

                if (getTurnColor() == "White") {
                    
                    lostBlackPieces.add(new Tile(displacedPiece, new Color(165,185, 210)));

                } else {

                    lostWhitePieces.add(new Tile(displacedPiece, new Color(165,185, 210)));
                }

            }

            // Checks if the moved piece is a pawn that has reached end of the board
            final int bottomRowIndex = 7;
            final int topRowIndex = 0;
            boolean isWhitePawnAtEnd = getSelectedPiece().getType() == "Pawn" && steps[1][0] == topRowIndex && getSelectedPiece().getColor() == "White";
            boolean isBlackPawnAtEnd = getSelectedPiece().getType() == "Pawn" && steps[1][0] == bottomRowIndex && getSelectedPiece().getColor() == "Black";

            if (isWhitePawnAtEnd || isBlackPawnAtEnd) {
                isPawnAtEnd = true; // By setting it to true, the game pauses and the user must selected what piece the pawn should become
            }

            // Now that the move is over, the turn color switches
            setTurnColor(ChessLib.flipTurnColor(getTurnColor()));

            checkStatus(board); // Checks the status of the board to see if the game is in a stalemate check, or checkmate

            // Now that the move is over, the destinations list the a selected piece can go should be cleared
            currentPossibleDestinationsList.clear();

            /* The step number resets to 0. The other player must now first select 
            a piece before they can choose a destination in their second step */
            moveStep = 0;

        } else if (getSelectedPiece().getColor().equals(getTurnColor()) && moveStep==0) {

            // If the player selects their piece, then the first step for performing a move is complete. mo
            currentPossibleDestinationsList = testMovesList;
            currentPossibleDestinationsList.add(steps[0]);
            moveStep = 1; // moveStep is now 1, meaning the user can now select a valid destination to send the selected piece

        } else if (board[steps[1][0]][steps[1][1]].getColor().equals(getTurnColor())) {

            // Scenario where a user selects another of their pieces even though they have already selected one

                /* Because the user selected another of their pieces rather than a valid destination for they 
                piece they selected previously, the piece selected in the second phase is now the selected piece */
                steps[0] = steps[1];
                setSelectedPiece(board[steps[1][0]][steps[1][1]]);

                // Gets all the available destinations the newly selected piece can go
                testMovesList = testMoves(getSelectedPiece(), steps[1], board);
                if (getSelectedPiece().getType() == "King" && getSelectedPiece().isUnmoved) {
                    testMovesList.addAll(getCastlingCoordinates(board, getTurnColor()));
                }
                currentPossibleDestinationsList = testMovesList;
                currentPossibleDestinationsList.add(steps[1]);

                // Because a piece is selected, moveStep is remains 1, which means the destination for the piece can be selected now
                moveStep = 1;
        }
    }

    private void castleKing(ChessPiece king, int[] rookCoordinates, ChessPiece[][] boardIn) {

        // Castles the king with the selected rook whose coordinates are inputted

        PieceFactory pieceFactory = new PieceFactory();
        final int lastColIndex = 7;
        final int firstColIndex = 0;

        // Castles black king with its right rook
        if (rookCoordinates[1] == lastColIndex) {

            boardIn[rookCoordinates[0]][6] = king;
            boardIn[rookCoordinates[0]][5] = pieceFactory.createPiece("Rook", king.getColor());
            boardIn[rookCoordinates[0]][4] = new Empty();
            boardIn[rookCoordinates[0]][7] = new Empty();

        } else if (rookCoordinates[1] == firstColIndex) {

            // Castles king with its left rook
            boardIn[rookCoordinates[0]][2] = king;
            boardIn[rookCoordinates[0]][3] = pieceFactory.createPiece("Rook", king.getColor());
            boardIn[rookCoordinates[0]][4] = new Empty();
            boardIn[rookCoordinates[0]][0] = new Empty();
        } 
    }

    private ArrayList<int[]> getCastlingCoordinates(ChessPiece[][] boardIn, String kingColor) {

        /* This method takes the current state of the board and the chosen 
        king color to determine the rooks, if any, the king can castle with */

        ArrayList<int[]> castlingCoordinatesList = new ArrayList<int[]>();
        PieceFactory pieceFactory = new PieceFactory();
        ChessPiece[][] testBoard = ChessLib.deepCopyBoard(boardIn);
        int kingRowNum = 0;

        switch (kingColor) {
            case "White":
                kingRowNum = 7; // If the king is white, then we are examining the unmoved rooks, which would be in row 7 as well
                break;
            case "Black":
                kingRowNum = 0; // If the king is black, then we are examining the unmoved rooks, which would be in row 7 as well
                break;
        }

        final int kingColIndex = 4;
        final int rightRookColIndex = 7;
        final int leftRookColIndex = 0;
        
        /*  If all the spaces between the rook and king are empty and both the king and rook are unmoved, castling could take place,
        assuming other requirements are met. e.g., the king cannot castle to escape check if its already in check. */

        // Checks the right-hand rook to see if it can be castled with the selected king.
        if (boardIn[kingRowNum][kingColIndex+1].getType() == "Empty" && boardIn[kingRowNum][kingColIndex+2].getType() == "Empty" && boardIn[kingRowNum][7].isUnmoved && boardIn[kingRowNum][rightRookColIndex].getType() == "Rook" && !isKingInCheck(board)) {

            // King cannot pass through check while it moves toward the castle
            testBoard[kingRowNum][kingColIndex+1] = pieceFactory.createPiece("King", kingColor);
            testBoard[kingRowNum][kingColIndex] = new Empty();
            if (!isKingInCheck(testBoard)) {

                testBoard = ChessLib.deepCopyBoard(boardIn);
                testBoard[kingRowNum][kingColIndex+2] = pieceFactory.createPiece("King", kingColor);
                testBoard[kingRowNum][kingColIndex+1] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[kingRowNum][kingColIndex] = new Empty();

                // If the king lands into check after the castling ends, then castling with the right rook cannot happen
                if (!isKingInCheck(testBoard)) {

                    int[] rightCastlingCoordinates = new int[2];
                    rightCastlingCoordinates[0] = kingRowNum;
                    rightCastlingCoordinates[1] = rightRookColIndex;
                    
                    // canCastle lets the view know the castling is valid, which the view does by coloring the rook's tile yellow.
                    boardIn[rightCastlingCoordinates[0]][rightCastlingCoordinates[1]].canCastle = true;

                    castlingCoordinatesList.add(rightCastlingCoordinates);
                }
            }
        }

        // Checks the left rook to see if it can be castled with the selected king
        if (boardIn[kingRowNum][kingColIndex-1].getType() == "Empty" && boardIn[kingRowNum][kingColIndex-2].getType() == "Empty" && boardIn[kingRowNum][kingColIndex-3].getType() == "Empty") {

            testBoard[kingRowNum][kingColIndex-1] = pieceFactory.createPiece("King", kingColor);
            testBoard[kingRowNum][kingColIndex] = new Empty();
            if (!isKingInCheck(testBoard)) {

                testBoard = ChessLib.deepCopyBoard(boardIn);
                testBoard[kingRowNum][kingColIndex-2] = pieceFactory.createPiece("King", kingColor);
                testBoard[kingRowNum][kingColIndex-1] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[kingRowNum][kingColIndex] = new Empty();
                testBoard[kingRowNum][leftRookColIndex] = new Empty();

                if (!isKingInCheck(testBoard)) {

                    int[] leftCastlingCoordinates = new int[2];
                    leftCastlingCoordinates[0] = kingRowNum;
                    leftCastlingCoordinates[1] = leftRookColIndex;

                    boardIn[leftCastlingCoordinates[0]][leftCastlingCoordinates[1]].canCastle = true;

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

                    if (boardIn[row][col].getColor() == getTurnColor() && !testMoves(boardIn[row][col], pieceCoordinates, boardIn).isEmpty()) {
                        cantMove = false;
                    }
                }
            }

            if (cantMove && isKingInCheck(boardIn)) {

                String winningColor = null;

                switch(getTurnColor()) {
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

                setGameStatus(String.format("%s in check        ", getTurnColor()));

            } else {

                setGameStatus(String.format("%s's turn        ", getTurnColor()));
            }
    }

    private ArrayList<int[]> testMoves(ChessPiece piece, int[] pieceCoordinates, ChessPiece[][] boardIn) {

        ArrayList<int[]> movesList = new ArrayList<int[]>();

        for (int[] possibleMove : piece.possibleMovesList(boardIn, pieceCoordinates)) {

            ChessPiece[][] testBoard = ChessLib.deepCopyBoard(boardIn);

            testBoard[possibleMove[0]][possibleMove[1]] = piece;
            testBoard[pieceCoordinates[0]][pieceCoordinates[1]] = new Empty();
                
            if (!isKingInCheck(testBoard)) {
                
                movesList.add(possibleMove);
            }
        }

        return movesList;
    }

    private boolean isKingInCheck(ChessPiece[][] boardIn) {

        int BOARD_SIZE = 8;

        for (int row=0; row < BOARD_SIZE; row++ ) {
            for (int col=0; col < BOARD_SIZE; col++) {

                int[] pieceCoordinates = new int[2];
                ChessPiece piece = boardIn[row][col];
                pieceCoordinates[0] = row;
                pieceCoordinates[1] = col;

                if (ChessLib.isCoordinatesInArrayList(getKingCoordinates(boardIn, getTurnColor()), piece.possibleMovesList(boardIn, pieceCoordinates))) {
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

                if (boardIn[row][col].getType() == "King" && boardIn[row][col].getColor() == pieceColor) {

                    kingCoordinates[0] = row;
                    kingCoordinates[1] = col;
                }
            }
        }

        return kingCoordinates;
    }

    public void flipBoard(ChessPiece[][] boardIn) {

        for (ChessPiece[] row : boardIn) {

            Collections.reverse(Arrays.asList(row));
        }

        Collections.reverse(Arrays.asList(boardIn));
    }

    public void resetModelProperties() {

        moveStep = 0;
        steps = new int[2][2];
        board = ChessLib.initializeBoard();
        currentPossibleDestinationsList = new ArrayList<int[]>();
        setTurnColor("White");
        gameStatus = "White's turn        ";
        isPawnAtEnd = false;
        lostWhitePieces = new ArrayList<Tile>();
        lostBlackPieces = new ArrayList<Tile>();
    }
}