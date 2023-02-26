import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

    public Pawn(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Pawn");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackPawn.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whitePawn.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}