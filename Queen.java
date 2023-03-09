import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

    public  Queen(String queenColor) {
        super(queenColor);
        setColor(queenColor);
        setType("Queen");

        switch(queenColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackQueen.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whiteQueen.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        Rook rook = new Rook(getColor());

        Bishop bishop = new Bishop(getColor());

        ArrayList<int[]> possibleCoordinatesList = rook.possibleMovesList(chessBoard, pieceCoordinates);

        possibleCoordinatesList.addAll(bishop.possibleMovesList(chessBoard, pieceCoordinates));

        return possibleCoordinatesList;
        }
}