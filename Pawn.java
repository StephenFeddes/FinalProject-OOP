import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

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
        
        int moveDirection = 1;

        int[] possibleCoordinates = new int[2];

        switch (getPieceColor()) {
            case "White":
                moveDirection = -1;
                break;
            
            case "Black":
                moveDirection = 1;
                break;
        }

        try {

            ChessPiece pieceInFront1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]];
            if (pieceInFront1.getPieceType() == "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {

            ChessPiece pieceInDiagonal1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] + moveDirection];
            if (pieceInDiagonal1.getPieceColor() != getPieceColor() && pieceInDiagonal1.getPieceType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] + moveDirection;
                possibleNextLocations.add(possibleCoordinates.clone());
            }

        } catch(ArrayIndexOutOfBoundsException e) {}

        try {

            ChessPiece pieceInDiagonal2 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] - moveDirection];
            if (pieceInDiagonal2.getPieceColor() != getPieceColor() && pieceInDiagonal2.getPieceType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] - moveDirection;
                possibleNextLocations.add(possibleCoordinates.clone());
                
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            ChessPiece pieceInFront1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]];
            ChessPiece pieceInFront2 = chessBoard[pieceCoordinates[0] + 2*moveDirection][pieceCoordinates[1]];
            if (isUnmoved && pieceInFront2.getPieceType()=="Empty" && pieceInFront1.getPieceType()=="Empty") {
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleCoordinates[0] = pieceCoordinates[0] + 2*moveDirection;
                possibleNextLocations.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        return possibleNextLocations;
        } 
    }

