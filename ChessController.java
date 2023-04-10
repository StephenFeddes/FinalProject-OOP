import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessController {
    
    private ChessView theView;
    private ChessModel theModel;

    public ChessController(ChessView theView, ChessModel theModel) {

        this.theView = theView;
        this.theModel = theModel;

        /* Listener that responds to input triggered by the user. Allows the controller to take 
        input from the view and give it to the model. The model then updates the view. */
        ActionListener moveListener = new MoveListener();
        this.theView.addTileListener(moveListener); // Controller is activated whenever a chess tile is clicked on
        this.theView.addBeginButtonListener(moveListener); // Controller is activated when the "begin" button is clicked
    }

    // Controls input/output between the view and model
    class MoveListener implements ActionListener {

            Thread blackClockThread = null;
            Thread whiteClockThread = null;

        public void actionPerformed(ActionEvent e) {

            int[] selectedTileCoordinates = theView.getSelectedTileCoordinates(); // Tile coordinates of the last tile clicked

            /* Once a pawn reaches the end, only the options panel can interact with the controller.
            Once the pawn has been converted to the desired piece, moves can be made again. */
            if (theView.isPawnAtEnd && !theView.isBoardFlipping && !theView.isResetClicked) {

                /* Because the board is flipped after every turn, pawns always move upwards
                on a player's turn. So, the 0th index is the farthest a pawn can get to */
                final int TOP_ROW_INDEX = 0;

                // Converts the pawn to the chosen piece
                theModel.getBoard()[TOP_ROW_INDEX][selectedTileCoordinates[1]] = theView.getConvertedPiece();

                theModel.checkStatus(theModel.getBoard()); // Checks if the converted piece immediately puts the enemy king in check 
                theModel.flipBoard(theModel.getBoard()); // The board is not flipped until the pawn is converted
                theModel.isPawnAtEnd = false; // Because the pawn is converted, there is no longer a pawn at the end
            }

            // Initializes the model according to which color was selected to go first
            if (theView.isFirstTurn) {
                theModel.setTurnColor(theView.getTurnColor());
                theModel.setBoard(ChessLib.initializeBoard(theModel.getTurnColor()));
                theModel.setGameStatus(String.format("%s's turn        ", theModel.getTurnColor()));
            }

            // Takes the selected tile coordinates and gives it to the model to run chess logic on
            theModel.addMove(selectedTileCoordinates);

            // Model informs the view what piece was selected
            theView.setSelectedPiece(theModel.getSelectedPiece());
    
            boolean isCheckmate = theModel.getGameStatus().contains("Checkmate"); // Checks for checkmate

            // Starts the clock thread for whoever's turn it is, assuming other conditions are met
            if ((theModel.getTurnColor() == "White" && !isCheckmate && !theView.isResetClicked && theModel.moveStep == 0 && !theView.getSelectedPiece().getType().equals("Empty") && !theModel.isPawnAtEnd || theView.isFirstTurn) && theView.isTimerOn) {
                whiteClockThread = new Thread(new TimerRunnable("White"));
                whiteClockThread.start();
            } 
            if ((theModel.getTurnColor() == "Black" && !isCheckmate && !theView.isResetClicked && theModel.moveStep == 0 && !theView.getSelectedPiece().getType().equals("Empty") && !theModel.isPawnAtEnd || theView.isFirstTurn) && theView.isTimerOn) {
                blackClockThread = new Thread(new TimerRunnable("Black"));
                blackClockThread.start();
            } 

            // Sets the lists of lost pieces and gives it to the view to display
            theView.setLostBlackPieces(theModel.getLostBlackPieces());
            theView.setLostWhitePieces(theModel.getLostWhitePieces());

            if (!theModel.getTurnColor().equals("Null")) {
                theView.setTurnColor(theModel.getTurnColor()); // Model notifies the view whose turn it is now
            }

            theView.isPawnAtEnd = theModel.isPawnAtEnd; // Model notifies the view a pawn has reached the end of the board

            // Model gives the view the list of tiles that the selected piece could go
            theView.setAvailableTilesList(theModel.getCurrentPossibleDestinationsList());

            // Model notifies the view the status of the game, such as check, draw, checkmate, and whose turn it is
            theView.setGameStatus(theModel.getGameStatus());

            // Displays the board according to the model's logic
            if (!theView.isResetClicked) {
                theView.displayBoard(theModel.getBoard());
            }

            // Flips the board after a move is made
            if (theModel.isPlacementValid & !theModel.isPawnAtEnd) {

                /*Alerts the main game thread that the board is currently flipping, which prevents players from making
                another move while the board is flipping.*/
                theView.isBoardFlipping = true; // Alerts the main game thread that the board is currently flipping

                // The thread that flips the board is created as an anonymous inner class
                Thread boardFlipThread = new Thread(new Runnable() {
                    public void run() {
                        /* After a move is made, the game should wait 1 second to flip the board.
                        The reason this needs to be a separate thread is so that the main game thread
                        is not paused. */
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
        
                        theView.isBoardFlipping = false;
                        theModel.flipBoard(theModel.getBoard());
                        theView.displayBoard(theModel.getBoard());
                    }
                });
                boardFlipThread.start();
            }

            theView.isFirstTurn = false;

            // If the reset button is clicked, reset all the properties for the model and view
            if (theView.isResetClicked && !theView.isBoardFlipping) {

                theModel.resetModelProperties();
                theView.resetViewProperties();
            
                // Stops the clock threads
                blackClockThread.interrupt();
                whiteClockThread.interrupt();

                theView.blackClockPanel.seconds = theView.getInitialSecondsLeft();
                theView.whiteClockPanel.seconds = theView.getInitialSecondsLeft();
                theView.displayBoard(theModel.getBoard());
            } 
        }
    }
        // Clock thread class
     class TimerRunnable implements Runnable {

        int secondsLeft;
        String timeColor;

        TimerRunnable(String timeColor) {

            this.timeColor = timeColor; // Determines which side's (white or black) time the clock should keep track of
        }

        public void run() {

                // Timer for white
            if (timeColor.equals("White")) {

                try {

                    while (theView.whiteClockPanel.seconds > 0 && theModel.getTurnColor() == "White" && !Thread.interrupted()) {

                        Thread.sleep(1000);
                        
                        secondsLeft = theView.whiteClockPanel.seconds - 1;
                        theView.whiteClockPanel.updateSeconds(secondsLeft);
                        if (secondsLeft  < 100) {
                            ChessLib.playAudio("ChessData/clockTick.wav");
                        }

                        if (secondsLeft == 0) {

                            theView.isTimerOver = true;
                            theView.setGameStatus("Out of time: Black wins              ");
                            theView.displayBoard(theModel.getBoard());
                            ChessLib.playAudio("ChessData/gameOver.wav");
                            theModel.setTurnColor("Null");
                            break;
                        }
                    }
                } catch (InterruptedException e) {}

                    // Timer for black
            } else if (timeColor == "Black") {

                try {

                    while (theView.blackClockPanel.seconds > 0 && theModel.getTurnColor() == "Black") {

                        Thread.sleep(1000);
                        
                        secondsLeft = theView.blackClockPanel.seconds - 1;
                        theView.blackClockPanel.updateSeconds(secondsLeft);
                        if (secondsLeft  < 100) {
                            ChessLib.playAudio("ChessData/clockTick.wav");
                        }

                        if (secondsLeft == 0) {

                            theView.isTimerOver = true;
                            theView.setGameStatus("Out of time: Black wins              ");
                            theView.displayBoard(theModel.getBoard());
                            ChessLib.playAudio("ChessData/gameOver.wav");
                            theModel.setTurnColor("Null");
                            break;
                        }
                    }
                } catch (InterruptedException e) {}
            }
        }
    }
}