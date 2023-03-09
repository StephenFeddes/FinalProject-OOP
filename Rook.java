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

        rookMoves(board, pieceCoordinates, possibleCoordinatesList, 1, 0);
        rookMoves(board, pieceCoordinates, possibleCoordinatesList, -1, 0);
        rookMoves(board, pieceCoordinates, possibleCoordinatesList, 1, 1);
        rookMoves(board, pieceCoordinates, possibleCoordinatesList, -1, 1);
        
        return possibleCoordinatesList;
    }

    public void rookMoves(ChessPiece[][] boardIn, int[] pieceCoordinatesIn, ArrayList <int[]> possibleCoordinatesListIn, int direction, int colOrRow) {
        
        int[] possibleCoordinates = pieceCoordinatesIn.clone();
        int bound = 7;
        if (direction == -1) {
            bound = 0;
        }

        try {
            possibleCoordinates[colOrRow] = possibleCoordinates[colOrRow]+direction;
            ChessPiece nextPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];
            
            while (nextPiece.getColor() != getColor() && ((possibleCoordinates[colOrRow] <= bound && bound == 7) || (possibleCoordinates[colOrRow] >= bound && bound == 0))) {
    
                possibleCoordinatesListIn.add(possibleCoordinates.clone());
                possibleCoordinates[colOrRow] = possibleCoordinates[colOrRow]+direction;

                if (nextPiece.getColor() != getColor() && nextPiece.getType() != "Empty") {
                    break;
                }

                nextPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
    }
}