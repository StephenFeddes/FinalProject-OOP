import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Bishop");

        switch(rookColor) {
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