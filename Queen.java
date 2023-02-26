import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

    public  Queen(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Queen");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("blackQueen.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("whiteQueen.png"));
                break;
        }
    }

    public void movePiece(ChessPiece[][] chessBoard) {}
}