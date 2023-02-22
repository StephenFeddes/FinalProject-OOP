import javax.swing.ImageIcon;

public class King extends ChessPiece {

    public King(String rookColor) {
        super(rookColor);

        setType("Rook");

        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}