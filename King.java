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
        int[] possibleCoordinates;

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1];
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1];
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0];
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0];
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];

            if (nextPiece.getType() == "Empty" || nextPiece.getColor() != getColor()) {
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        return possibleNextLocations;
        }
}