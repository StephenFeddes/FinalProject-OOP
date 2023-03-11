
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

        // Gives the view the controller its input should be sent to
        this.theView.addMoveListener(new MoveListener());
    }

    // Controls input/output between the view and model
    class MoveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            int[] selectedTileCoordinates = theView.getSelectedTileCoordinates(); // Tile coordinates of the last tile clicked

            // Model informs the view whose turn it is so that the view can display that fact
            theView.setTurnColor(theModel.getTurnColor());

            /* Once a pawn reaches the end, only the options panel can interact with the controller.
            Once the pawn has been converted to the desired piece, moves can be made again. */
            if (theView.isPawnAtEnd ) {
                theModel.getBoard()[selectedTileCoordinates[0]][selectedTileCoordinates[1]] = theView.getConvertedPiece();
                theModel.checkStatus(theModel.getBoard());
                theModel.isPawnAtEnd = false;
            }

            // Takes the selected tile coordinates and gives it to the model to run chess logic on
            theModel.addMove(selectedTileCoordinates);

            // Gets what the model considers to be the selected piece and informs the view
            theView.setSelectedPiece(theModel.getSelectedPiece());

            // Gets the list of lost pieces and gives it to the view to display
            theView.setLostBlackPieces(theModel.getLostBlackPieces());
            theView.setLostWhitePieces(theModel.getLostWhitePieces());

            theView.setTurnColor(theModel.getTurnColor()); // Model notifies the view whose turn it is now

            theView.isPawnAtEnd = theModel.isPawnAtEnd; // Model notifies the view a pawn has reached the end of the board

            // Model gives the view the list of tiles that the selected piece could go
            theView.setAvailableTilesList(theModel.getCurrentPossibleDestinationsList());

            // Model notifies the view the status of the game, such as check, draw, checkmate, or whose turn it is
            theView.setGameStatus(theModel.getGameStatus());

            // Displays the board according to the model's logic
            theView.displayBoard(theModel.getBoard());

            if (theModel.isPlacementValid) {
                theView.boardIsReadyToFlip = true;

                Thread boardFlipThread = new Thread(new Runnable() {

                    public void run() {
    
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        
                        theView.boardIsReadyToFlip = false;
                        theModel.flipBoard(theModel.getBoard());
                        theView.displayBoard(theModel.getBoard());
                    }
                });
    
                boardFlipThread.start();
            }

            // If the reset button is clicked, reset all the properties for the model and view and display the new board
            if (theView.isResetClicked && !theView.boardIsReadyToFlip) {
                theModel.resetModelProperties();
                theView.resetViewProperties();
                theView.displayBoard(theModel.getBoard());
            }
        }
    }
}