import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

    public Queen(String rookColor) {
        super(rookColor);

        setType("Rook");

        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}