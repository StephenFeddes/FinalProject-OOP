import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String bishopColor) {
        super(bishopColor);
        setPieceColor(bishopColor);
        setPieceType("Bishop");

        switch(bishopColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackBishop.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteBishop.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}