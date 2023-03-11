import java.util.ArrayList;

import javax.swing.ImageIcon;

public class King extends ChessPiece {

    public King(String rookColor) {
        super(rookColor);
        setColor(rookColor);
        setType("King");

        switch(rookColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackKing.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whiteKing.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        // Builds the list of coordinates the king could go
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 0, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 0, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 0, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 0, -1, true);

        return possibleNextLocations;
        }
}