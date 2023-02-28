
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChessController implements ActionListener {
    
    private ChessView theView;
    private ChessModel theModel;
    public static int[] tileCoordinates = new int[2];

    public ChessController(ChessView theView, ChessModel theModel) {

        this.theView = theView;
        this.theModel = theModel;

    }

    public ChessController() {
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(tileCoordinates[0]);
        System.out.println(tileCoordinates[1]);
    }


}
