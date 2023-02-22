import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardFrame extends JFrame {

    public BoardFrame() {
        // Inititalizations and constants
        final int numColumns = 10;
        final int numRows = 10;
        Tile newTile;
        ArrayList<Tile> tileList = new ArrayList<Tile>();
        setBounds(0,0,725,725);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel MosaicPanel = new JPanel();
        contentPane.add(MosaicPanel, BorderLayout.CENTER);
        MosaicPanel.setLayout(new GridLayout(numRows, numColumns));
        

        // Builds inital mosaic and fills the ArrayList
        for (int i=0; i < numRows*numColumns; i++) {
            newTile = new Tile();
            tileList.add(newTile);
            MosaicPanel.add(newTile);
            System.out.println(newTile);
        }
    }
}