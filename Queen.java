import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Queen extends ChessPiece {

    public  Queen(String queenColor) {
        super(queenColor);
        setPieceColor(queenColor);
        setPieceType("Queen");

        switch(queenColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackQueen.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whiteQueen.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}