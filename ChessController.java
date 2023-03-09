
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ChessController {
    
    private ChessView theView;
    private ChessModel theModel;

    public ChessController(ChessView theView, ChessModel theModel) {

        this.theView = theView;
        this.theModel = theModel;

        this.theView.addMoveListener(new MoveListener());
    }

    class MoveListener implements ActionListener {

        int[] selectedTileCoordinates;

        public void actionPerformed(ActionEvent e) {

            selectedTileCoordinates = theView.getSelectedTileCoordinates();

            theView.setTurnColor(theModel.currentTurn);

            if (theView.isPawnAtEnd && theView.getConvertedPiece() != null) {
                theModel.getBoard()[selectedTileCoordinates[0]][selectedTileCoordinates[1]] = theView.getConvertedPiece();
                theModel.checkStatus(theModel.getBoard());
                theModel.isPawnAtEnd = false;
            }

            theModel.addMove(selectedTileCoordinates);

            theView.setLostBlackPieces(theModel.getLostBlackPieces());
            theView.setLostWhitePieces(theModel.getLostWhitePieces());

            theView.setTurnColor(theModel.currentTurn);

            theView.isPawnAtEnd = theModel.isPawnAtEnd;

            theView.setAvailableTilesList(theModel.getCurrentPossibleMovesList());

            theView.setGameStatus(theModel.getGameStatus());

            if (theView.isResetClicked) {
                theModel.resetModelProperties();
                theView.resetViewProperties();
            }

            theView.displayBoard(theModel.getBoard());

            if (theView.isResetClicked) {
                theModel.setBoard(initializeBoard());
            }
        }
    }

public static boolean isCoordinatesInArrayList(int[] coordinates, ArrayList<int[]> availableTilesList) {

    for (int[] availableTileCoordinates : availableTilesList) {

        if (availableTileCoordinates[0]==coordinates[0] && availableTileCoordinates[1]==coordinates[1]) {
            return true;
        }
    }

    return false;
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

