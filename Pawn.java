import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

    private boolean isUnmoved = true;

    public Pawn(String pawnColor) {
        super(pawnColor);
        setPieceColor(pawnColor);
        setPieceType("Pawn");

        switch(pawnColor) {
            case "Black":
                setPieceImage(new ImageIcon("ChessPieces/blackPawn.png"));
                break;

            case "White":
                setPieceImage(new ImageIcon("ChessPieces/whitePawn.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleNextLocations = new ArrayList<int[]>();
        

        int[] possibleCoordinates = new int[2];

        possibleCoordinates[0] = pieceCoordinates[0] + 1;
        possibleCoordinates[1] = pieceCoordinates[1];

        possibleNextLocations.add(possibleCoordinates.clone());

        return possibleNextLocations;
        }

    }