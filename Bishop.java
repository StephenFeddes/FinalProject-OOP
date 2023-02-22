import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String rookColor) {
        super(rookColor);

        setType("Rook");

        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}