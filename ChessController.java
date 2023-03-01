
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

            //System.out.println(theView.getTileCoordinates()[0]);
            //System.out.println(theView.getTileCoordinates()[1]);

            selectedTileCoordinates = theView.getTileCoordinates();

            theModel.addMove(selectedTileCoordinates);

            //System.out.println(theModel.getDisplayIsReady());

            if (theModel.getDisplayIsReady()) {
            theView.setBoard(theModel.getBoard());
            }
        }
    }
}


