import java.util.ArrayList;

public class Empty extends ChessPiece {

    public Empty() {
        setPieceType("Empty");
        
        setPieceColor("Null");
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {
        
        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();

        return possibleNextLocations;
        }
}