import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Bishop extends ChessPiece {

    public Bishop(String bishopColor) {
        super(bishopColor);
        setPieceColor(bishopColor);
        setPieceType("Bishop");

        switch(bishopColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackBishop.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whiteBishop.png"));
                break;
        }
    }
    
    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}