public class ChessGame {

    public static void main(String[] args) {

        ChessView theView = new ChessView();

        ChessModel theModel = new ChessModel();

        ChessController theController = new ChessController(theView, theModel);

        theView.setVisible(true);
    }
}
