import javax.swing.ImageIcon;

public class King extends ChessPiece {

    public King(String rookColor) {
        super(rookColor);
        setPieceColor(rookColor);
        setPieceType("King");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackKing.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteKing.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}