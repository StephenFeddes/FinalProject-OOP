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

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}