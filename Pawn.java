import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

    public Pawn(String pawnColor) {
        super(pawnColor);
        setPieceColor(pawnColor);
        setPieceType("Pawn");

        switch(pawnColor) {
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