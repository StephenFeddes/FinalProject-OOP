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
        int[] possibleCoordinates;

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+2;
            possibleCoordinates[1] = possibleCoordinates[1]+1;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+2;
            possibleCoordinates[1] = possibleCoordinates[1]-1;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-2;
            possibleCoordinates[1] = possibleCoordinates[1]-1;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-2;
            possibleCoordinates[1] = possibleCoordinates[1]+1;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]+2;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]+2;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]-2;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]-2;

            if (board[possibleCoordinates[0]][possibleCoordinates[1]].getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
        
        return possibleNextLocations;
    }
}