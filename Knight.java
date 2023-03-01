import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

    public Knight(String knightColor) {
        super(knightColor);
        setPieceColor(knightColor);
        setPieceType("Knight");

        switch(knightColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackKnight.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteKnight.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}