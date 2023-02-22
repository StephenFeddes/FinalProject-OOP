import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

    public Pawn(String rookColor) {
        super(rookColor);

        setType("Rook");

        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}