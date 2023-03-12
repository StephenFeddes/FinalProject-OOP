import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.event.SwingPropertyChangeSupport;

import java.awt.Color;
import java.io.IOException;

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
    public boolean isPlacementValid = false;

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
        isPlacementValid = false;
        if (moveStep == 1) {
            for (int[] coordinates : testMovesList) {

                if (Arrays.equals(coordinates, steps[1])) {

                    isPlacementValid = true;
                }
            }

        } 

        // Places the selected piece in the valid destination that was selected
        if (isPlacementValid) {

            try {
                ChessLib.playAudio("ChessData/pieceMoved.wav");
            } catch (Exception e) {}

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
            final int TOP_ROW_INDEX = 0;

            if (getSelectedPiece().getType() == "Pawn" && steps[1][0] == TOP_ROW_INDEX) {

                try {
                    ChessLib.playAudio("ChessData/gameNotification.wav");
                } catch (Exception ex) {}

                isPawnAtEnd = true; // By setting it to true, the game pauses and the user must selected what piece the pawn should become
            }

            // Now that the move is over, the turn color switches
            setTurnColor(ChessLib.flipTurnColor(getTurnColor()));

            checkStatus(board); // Checks the status of the board to see if the game is in a stalemate check, or checkmate

            // Now that the move is over, the destinations list should be cleared
            currentPossibleDestinationsList.clear();
            //Arrays.fill(steps, null);

            /* The step number resets to 0. The other player must now first select 
            a piece before they can choose a destination in their second step */
            moveStep = 0;

        } else if (getSelectedPiece().getColor().equals(getTurnColor()) && moveStep==0) {

            try {
                ChessLib.playAudio("ChessData/pieceSelected.wav");
            } catch (Exception ex) {}

            // If the player selects their piece, then the first step for performing a move is complete.
            currentPossibleDestinationsList = testMovesList;
            currentPossibleDestinationsList.add(steps[0]);
            moveStep = 1; // moveStep is now 1, meaning the user can now select a valid destination to send the selected piece

        } else if (board[steps[1][0]][steps[1][1]].getColor().equals(getTurnColor()) && moveStep == 1) {

            try {
                ChessLib.playAudio("ChessData/pieceSelected.wav");
            } catch (Exception ex) {}

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

                // Because a piece is selected, moveStep remains 1, which means the destination for the piece can be selected now
                moveStep = 1;
        }
    }

    private void castleKing(ChessPiece king, int[] rookCoordinates, ChessPiece[][] boardIn) {

        // Castles the king with the selected rook whose coordinates are inputted

        PieceFactory pieceFactory = new PieceFactory();
        final int KING_ROW_INDEX = 7;

        int kingColIndex = 4;
        int kingSideRookColIndex = 7; // The column index of the white rook closest to its king
        int queenSideRookColIndex = 0; // The column index of the white rook closest to its queen
        int queenSideDirection = -1; // Indicates if the queen is to 
        int kingSideDirection = 1;
        if (king.getColor() == "Black") {

            // Same as above, but this time the king is black
            kingColIndex = 3; 
            kingSideRookColIndex = 0;
            queenSideRookColIndex = 7;
            queenSideDirection = 1;
            kingSideDirection = -1;
        }
       
        // Castles king with its king-side rook
        if (rookCoordinates[1] == kingSideRookColIndex) {

            boardIn[KING_ROW_INDEX][kingColIndex + 2*kingSideDirection] = king;
            boardIn[KING_ROW_INDEX][kingColIndex + kingSideDirection] = pieceFactory.createPiece("Rook", king.getColor());
            boardIn[KING_ROW_INDEX][kingColIndex] = new Empty();
            boardIn[KING_ROW_INDEX][kingSideRookColIndex] = new Empty();

        } else if (rookCoordinates[1] == queenSideRookColIndex) {

            // Castles king with its queen-side rook
            boardIn[KING_ROW_INDEX][kingColIndex + 2*queenSideDirection] = king;
            boardIn[KING_ROW_INDEX][kingColIndex + queenSideDirection] = pieceFactory.createPiece("Rook", king.getColor());
            boardIn[KING_ROW_INDEX][kingColIndex] = new Empty();
            boardIn[KING_ROW_INDEX][queenSideRookColIndex] = new Empty();
        } 
    }

    private ArrayList<int[]> getCastlingCoordinates(ChessPiece[][] boardIn, String kingColor) {

        /* This method takes the current state of the board and the chosen 
        king color to determine the rooks, if any, the king can castle with. This method assumes the king is unmoved */

        ArrayList<int[]> castlingCoordinatesList = new ArrayList<int[]>();
        PieceFactory pieceFactory = new PieceFactory();
        ChessPiece[][] testBoard = ChessLib.deepCopyBoard(boardIn);
        final int KING_ROW_NUM = 7;

        int kingColIndex = 4; // Column index of the unmoved white king
        int kingSideRookColIndex = 7; // The column index of the white rook closest to its king
        int queenSideRookColIndex = 0; // The column index of the white rook closest to its queen
        int queenSideDirection = -1; // Indicates if the white queen is to the east or west side of the board
        int kingSideDirection = 1; // Indicates if the white king is to the east or west side of the board
        if (kingColor == "Black") {

            // If the selected king is black, not white, these are the appropriate numbers
            kingColIndex = 3;
            kingSideRookColIndex = 0;
            queenSideRookColIndex = 7;
            queenSideDirection = 1;
            kingSideDirection = -1;
        }
        
        /*  If all the spaces between the rook and king are empty and both the king and rook are unmoved, castling could take place,
        assuming other requirements are met. e.g., the king cannot castle to escape check if its already in check. */

        /* Checks the king-side rook to see if it can be castled with the selected king. 
        If the rook can be castled, its coordinates are added to the list */
        if (boardIn[KING_ROW_NUM][kingColIndex+kingSideDirection].getType() == "Empty" && boardIn[KING_ROW_NUM][kingColIndex+2*kingSideDirection].getType() == "Empty" && boardIn[KING_ROW_NUM][kingSideRookColIndex].isUnmoved && !isKingInCheck(board, getTurnColor())) {

            // King cannot pass through check while it moves toward the castle
            testBoard[KING_ROW_NUM][kingColIndex+kingSideDirection] = pieceFactory.createPiece("King", kingColor);
            testBoard[KING_ROW_NUM][kingColIndex] = new Empty();
            if (!isKingInCheck(testBoard, getTurnColor())) {

                testBoard = ChessLib.deepCopyBoard(boardIn);
                testBoard[KING_ROW_NUM][kingColIndex+2*kingSideDirection] = pieceFactory.createPiece("King", kingColor);
                testBoard[KING_ROW_NUM][kingColIndex+kingSideDirection] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[KING_ROW_NUM][kingColIndex] = new Empty();
                testBoard[KING_ROW_NUM][kingSideRookColIndex] = new Empty();

                // If the king lands into check after the castling ends, then castling with the king-side rook cannot happen
                if (!isKingInCheck(testBoard, getTurnColor())) {

                    int[] kingSideCastlingCoordinates = new int[2];
                    kingSideCastlingCoordinates[0] = KING_ROW_NUM;
                    kingSideCastlingCoordinates[1] = kingSideRookColIndex;
                    
                    /* canCastle lets the view know the castling is valid for this king, 
                    which the view does by coloring the rook's tile yellow when this king is selected */
                    boardIn[kingSideCastlingCoordinates[0]][kingSideCastlingCoordinates[1]].canCastle = true;

                    castlingCoordinatesList.add(kingSideCastlingCoordinates);
                }
            }
        }

        // Checks the queen-side rook to see if it can be castled with the selected king
        if (boardIn[KING_ROW_NUM][kingColIndex+queenSideDirection].getType() == "Empty" && boardIn[KING_ROW_NUM][kingColIndex+2*queenSideDirection].getType() == "Empty" && boardIn[KING_ROW_NUM][kingColIndex+3*queenSideDirection].getType() == "Empty" && boardIn[KING_ROW_NUM][queenSideRookColIndex].isUnmoved && !isKingInCheck(board, getTurnColor())) {

            testBoard[KING_ROW_NUM][kingColIndex+queenSideDirection] = pieceFactory.createPiece("King", kingColor);
            testBoard[KING_ROW_NUM][kingColIndex] = new Empty();
            if (!isKingInCheck(testBoard, getTurnColor())) {

                testBoard = ChessLib.deepCopyBoard(boardIn);
                testBoard[KING_ROW_NUM][kingColIndex+2*queenSideDirection] = pieceFactory.createPiece("King", kingColor);
                testBoard[KING_ROW_NUM][kingColIndex+queenSideDirection] = pieceFactory.createPiece("Rook", kingColor);
                testBoard[KING_ROW_NUM][kingColIndex] = new Empty();
                testBoard[KING_ROW_NUM][queenSideRookColIndex] = new Empty();

                if (!isKingInCheck(testBoard, getTurnColor())) {

                    int[] queenSideCastlingCoordinates = new int[2];
                    queenSideCastlingCoordinates[0] = KING_ROW_NUM;
                    queenSideCastlingCoordinates[1] = queenSideRookColIndex;

                    boardIn[queenSideCastlingCoordinates[0]][queenSideCastlingCoordinates[1]].canCastle = true;

                    castlingCoordinatesList.add(queenSideCastlingCoordinates);
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
                    
                    // Checks if the player can move any valid moves on their turn
                    if (boardIn[row][col].getColor() == getTurnColor() && !testMoves(boardIn[row][col], pieceCoordinates, boardIn).isEmpty()) {
                        cantMove = false;
                    }
                }
            }

            if (cantMove && isKingInCheck(boardIn, getTurnColor())) {

                // If the current player cannot move, then the other player has the winning color (white or black)
                String winningColor = ChessLib.flipTurnColor(getTurnColor());

                setGameStatus(String.format("Checkmate: %s wins                  ", winningColor));

                try {
                    ChessLib.playAudio("ChessData/checkMate.wav");
                } catch (Exception ex) {}

            } else if (cantMove) {

                // If the player is not in check and they cannot make any moves on their turn, then the game is a draw
                setGameStatus("Stalemate           ");

                try {
                    ChessLib.playAudio("ChessData/gameNotification.wav");
                } catch (Exception ex) {}

            } else if (isKingInCheck(boardIn, getTurnColor())) {

                setGameStatus(String.format("%s in check        ", getTurnColor()));

                try {
                    ChessLib.playAudio("ChessData/gameNotification.wav");
                } catch (Exception ex) {}

            } else {

                // If the player is not in check, checkmate, or stalemate, then the view should only say who's turn it is
                setGameStatus(String.format("%s's turn        ", getTurnColor()));
            }
    }

    private ArrayList<int[]> testMoves(ChessPiece piece, int[] pieceCoordinates, ChessPiece[][] boardIn) {

        ArrayList<int[]> movesList = new ArrayList<int[]>();

        /* Tests each destination a piece can usually go under normal circumstances. If, after the move, the king is 
        immediately in check, that destination should not be added to the list of destinations the selected piece could go */
        for (int[] possibleDestination : piece.possibleMovesList(boardIn, pieceCoordinates)) {

            ChessPiece[][] testBoard = ChessLib.deepCopyBoard(boardIn); // Deep copy of the board

            // Moves the piece to the destination it could go, assuming it does not put the player's king in check
            testBoard[possibleDestination[0]][possibleDestination[1]] = piece;
            testBoard[pieceCoordinates[0]][pieceCoordinates[1]] = new Empty();
            
            // Checks if the king is in check after the piece is moved
            if (!isKingInCheck(testBoard, getTurnColor())) {
                
                /* If the king is not in check after the piece is moved, then that destination is added 
                to the list of destinations the selected piece could go */
                movesList.add(possibleDestination); // If the king is not 
            }
        }

        return movesList;
    }

    private boolean isKingInCheck(ChessPiece[][] boardIn, String kingColor) {

        int BOARD_SIZE = 8;

        /* Checks all the places a piece can go. If any of the pieces can 
        go to the tile containing the opposite-color king, then the king is in check */
        for (int row=0; row < BOARD_SIZE; row++ ) {
            for (int col=0; col < BOARD_SIZE; col++) {

                int[] pieceCoordinates = new int[2];
                ChessPiece piece = boardIn[row][col];
                pieceCoordinates[0] = row;
                pieceCoordinates[1] = col;

                // Checks if the opposite king's coordinates is contained in any of the list of moves a piece could make
                if (ChessLib.isCoordinatesInArrayList(getKingCoordinates(boardIn, kingColor), piece.possibleMovesList(boardIn, pieceCoordinates))) {
                    return true;
                }
            }
        }

        return false;
    }

    private int[] getKingCoordinates(ChessPiece[][] boardIn, String pieceColor) {
    
        int[] kingCoordinates = new int[2];

        int BOARD_SIZE = 8;
        // Checks all the coordinates on the board until it finds the coordinates that contain the desired king
        for (int row=0; row < BOARD_SIZE; row++) {
            for (int col=0; col < BOARD_SIZE; col++) {

                if (boardIn[row][col].getType() == "King" && boardIn[row][col].getColor() == pieceColor) {

                    kingCoordinates[0] = row;
                    kingCoordinates[1] = col;
                    return kingCoordinates;
                }
            }
        }

        return kingCoordinates;
    }

    public void flipBoard(ChessPiece[][] boardIn) {

        // Reverses the order within each row
        for (ChessPiece[] row : boardIn) {

            Collections.reverse(Arrays.asList(row));
        }

        // Reverses of the order between rows
        Collections.reverse(Arrays.asList(boardIn));
    }

    public void resetModelProperties() {

        // Resets all the model properties to their initial state
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