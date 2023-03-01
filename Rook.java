import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

    public Rook(String rookColor) {
        super(rookColor);
        setPieceColor(rookColor);
        setPieceType("Rook");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackRook.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteRook.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}