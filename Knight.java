import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Knight extends ChessPiece {

    public Knight(String knightColor) {
        super(knightColor);
        setPieceColor(knightColor);
        setPieceType("Knight");

        switch(knightColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackKnight.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whiteKnight.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}