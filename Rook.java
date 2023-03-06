import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Rook extends ChessPiece {

    public Rook(String rookColor) {
        super(rookColor);
        setPieceColor(rookColor);
        setPieceType("Rook");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackRook.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whiteRook.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] board, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        int[] possibleCoordinates;
         
        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getPieceColor() != getPieceColor() && possibleCoordinates[0] <= 7) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]+1;

                if (nextPiece.getPieceColor() != getPieceColor() && nextPiece.getPieceType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[0] = possibleCoordinates[0]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getPieceColor() != getPieceColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[0] = possibleCoordinates[0]-1;

                if (nextPiece.getPieceColor() != getPieceColor() && nextPiece.getPieceType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[1] = possibleCoordinates[1]+1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getPieceColor() != getPieceColor() && possibleCoordinates[0] <= 7) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[1] = possibleCoordinates[1]+1;

                if (nextPiece.getPieceColor() != getPieceColor() && nextPiece.getPieceType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            possibleCoordinates = pieceCoordinates.clone();
            possibleCoordinates[1] = possibleCoordinates[1]-1;
            ChessPiece nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            while (nextPiece.getPieceColor() != getPieceColor() && possibleCoordinates[0] >= 0) {
    
                possibleNextLocations.add(possibleCoordinates.clone());
                possibleCoordinates[1] = possibleCoordinates[1]-1;

                if (nextPiece.getPieceColor() != getPieceColor() && nextPiece.getPieceType() != "Empty") {
                    break;
                }

                nextPiece = board[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
        
        return possibleNextLocations;
    }

}