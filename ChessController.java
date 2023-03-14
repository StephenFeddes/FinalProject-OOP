import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessController {
    
    private ChessView theView;
    private ChessModel theModel;

    public ChessController(ChessView theView, ChessModel theModel) {

        this.theView = theView;
        this.theModel = theModel;

        // Gives the view the controller its input should be sent to
        ActionListener moveListener = new MoveListener();
        this.theView.addTileListener(moveListener);
        this.theView.addBeginButtonListener(moveListener);
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
                final int TOP_ROW_INDEX = 0;
                theModel.getBoard()[TOP_ROW_INDEX][selectedTileCoordinates[1]] = theView.getConvertedPiece();
                theModel.checkStatus(theModel.getBoard());
                theModel.flipBoard(theModel.getBoard());
                theModel.isPawnAtEnd = false;
            }

            if (theView.isFirstTurn) {
            
                theModel.setTurnColor(theView.getTurnColor());
                theModel.setBoard(ChessLib.initializeBoard(theModel.getTurnColor()));
                theModel.setGameStatus(String.format("%s's turn        ", theModel.getTurnColor()));
            }

            // Takes the selected tile coordinates and gives it to the model to run chess logic on
            theModel.addMove(selectedTileCoordinates);

            // Gets what the model considers to be the selected piece and informs the view
            theView.setSelectedPiece(theModel.getSelectedPiece());
    
            boolean isCheckmate = theModel.getGameStatus().contains("Checkmate");
            if ((theModel.getTurnColor() == "White" && !isCheckmate && !theView.isResetClicked && theModel.moveStep == 0 && !theView.getSelectedPiece().getType().equals("Empty") && !theModel.isPawnAtEnd || theView.isFirstTurn) && theView.isTimerOn) {
                whiteClockThread = new Thread(new TimerRunnable("White"));
                whiteClockThread.start();
            } 
            if ((theModel.getTurnColor() == "Black" && !isCheckmate && !theView.isResetClicked && theModel.moveStep == 0 && !theView.getSelectedPiece().getType().equals("Empty") && !theModel.isPawnAtEnd || theView.isFirstTurn) && theView.isTimerOn) {
                blackClockThread = new Thread(new TimerRunnable("Black"));
                blackClockThread.start();
            } 

            // Gets the list of lost pieces and gives it to the view to display
            theView.setLostBlackPieces(theModel.getLostBlackPieces());
            theView.setLostWhitePieces(theModel.getLostWhitePieces());

            if (!theModel.getTurnColor().equals("Null")) {
                theView.setTurnColor(theModel.getTurnColor()); // Model notifies the view whose turn it is now
            }

            theView.isPawnAtEnd = theModel.isPawnAtEnd; // Model notifies the view a pawn has reached the end of the board

            // Model gives the view the list of tiles that the selected piece could go
            theView.setAvailableTilesList(theModel.getCurrentPossibleDestinationsList());

            // Model notifies the view the status of the game, such as check, draw, checkmate, or whose turn it is
            theView.setGameStatus(theModel.getGameStatus());

            // Displays the board according to the model's logic
            if (!theView.isResetClicked) {
                theView.displayBoard(theModel.getBoard());
            }

            // Flips the board after a move is made
            if (theModel.isPlacementValid & !theModel.isPawnAtEnd) {
                
                theView.isBoardFlipping = true;

                Thread boardFlipThread = new Thread(new Runnable() {

                    public void run() {

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

            // If the reset button is clicked, reset all the properties for the model and view and display the new board
            if (theView.isResetClicked && !theView.isBoardFlipping) {

                theModel.resetModelProperties();
                theView.resetViewProperties();
            
                blackClockThread.interrupt();
                whiteClockThread.interrupt();
                theView.blackClockPanel.seconds = theView.getInitialSecondsLeft();
                theView.whiteClockPanel.seconds = theView.getInitialSecondsLeft();
                theView.displayBoard(theModel.getBoard());
            } 
        }
    }

        class TimerRunnable implements Runnable {

            int secondsLeft;
            String timeColor;

            TimerRunnable(String timeColor) {

                this.timeColor = timeColor;
            }

            public void run() {

                if (timeColor == "White") {

                    while (theView.whiteClockPanel.seconds > 0 && theModel.getTurnColor() == "White" && !Thread.interrupted()) {

                        try {

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
                        } catch (InterruptedException e) {}
                    }
                } else if (timeColor == "Black") {

                    while (theView.blackClockPanel.seconds > 0 && theModel.getTurnColor() == "Black" && !Thread.interrupted()) {

                        try {

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
                        } catch (InterruptedException e) {}
                    }
                }
            }
        }
    }