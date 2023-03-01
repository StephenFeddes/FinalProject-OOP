import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

    public  Queen(String queenColor) {
        super(queenColor);
        setPieceColor(queenColor);
        setPieceType("Queen");

        switch(queenColor) {
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