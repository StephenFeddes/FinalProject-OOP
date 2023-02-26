import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

    public Knight(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Knight");

        switch(getColor()) {
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