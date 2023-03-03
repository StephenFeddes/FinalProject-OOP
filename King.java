import java.util.ArrayList;

import javax.swing.ImageIcon;

public class King extends ChessPiece {

    public King(String rookColor) {
        super(rookColor);
        setPieceColor(rookColor);
        setPieceType("King");

        switch(rookColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackKing.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whiteKing.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}