import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

    public Knight(String knightColor) {
        super(knightColor);
        setColor(knightColor);
        setType("Knight");

        switch(knightColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackKnight.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whiteKnight.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        // Builds the list of coordinates the knight could go
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, 2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, 2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -1, -2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 1, -2, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 2, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, 2, 1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -2, -1, true);
        findCoordinates(board, pieceCoordinates, possibleNextLocations, -2, 1, true);
        
        return possibleNextLocations;
    }
}