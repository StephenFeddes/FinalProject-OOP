import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

    public Rook(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("Rook");

        switch(rookColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackRook.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whiteRook.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();

        // Builds the list of coordinates the rook could go
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 1, 0, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, -1, 0, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 0, 1, false);
        findCoordinates(board, pieceCoordinates, possibleCoordinatesList, 0, -1, false);
        
        return possibleCoordinatesList;
    }
}