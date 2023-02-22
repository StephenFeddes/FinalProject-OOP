import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

    public Rook(String rookColor) {
        super(rookColor);

        setType("Rook");

        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}