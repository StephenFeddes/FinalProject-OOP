import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

    public Knight(String rookColor) {
        super(rookColor);

        setType("Rook");
        
        setPieceImage(new ImageIcon(getClass().getResource("blackPawn.svg")));
    }

    public void movePiece(ChessPiece[][] chessBoard) {
    }
}