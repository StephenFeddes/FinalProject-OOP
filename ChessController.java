
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

            selectedTileCoordinates = theView.getTileCoordinates();

            theModel.addMove(selectedTileCoordinates);

            theView.setAvailableTilesList(theModel.getCurrentPossibleMovesList());

            theView.displayBoard(theModel.getBoard());
        }
    }
}


