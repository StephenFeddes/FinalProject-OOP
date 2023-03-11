import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String bishopColor) {
        super.setColor(bishopColor);
        setType("Bishop");

        switch(bishopColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackBishop.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whiteBishop.png"));
                break;
        }
    }
    
    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();

        // Builds the list of coordinates the bishop could go
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, 1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, -1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, -1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, 1, false);

        return possibleCoordinatesList;
    }
}