import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

    public Rook(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Rook");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackRook.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteRook.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}