import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String bishopColor) {
        super(bishopColor);
        setColor(bishopColor);
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

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();
        int[] possibleCoordinates;

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getColor() != getColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]+1;
                possibleCoordinates[1] = possibleCoordinates[1]+1;

                if (nextPiece.getColor() != getColor() && nextPiece.getType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getColor() != getColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]-1;
                possibleCoordinates[1] = possibleCoordinates[1]+1;

                if (nextPiece.getColor() != getColor() && nextPiece.getType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getColor() != getColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]+1;
                possibleCoordinates[1] = possibleCoordinates[1]-1;

                if (nextPiece.getColor() != getColor() && nextPiece.getType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getColor() != getColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]-1;
                possibleCoordinates[1] = possibleCoordinates[1]-1;

                if (nextPiece.getColor() != getColor() && nextPiece.getType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
        
        return possibleNextLocations;
    }
}