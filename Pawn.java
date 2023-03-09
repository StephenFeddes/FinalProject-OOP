import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Pawn extends ChessPiece {

    public Pawn(String pawnColor) {
        super(pawnColor);
        setColor(pawnColor);
        setType("Pawn");

        switch(pawnColor) {
            case "Black":
                setImage(new ImageIcon("ChessPieces/blackPawn.png"));
                break;

            case "White":
                setImage(new ImageIcon("ChessPieces/whitePawn.png"));
                break;
        }
    }

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();
        
        int moveDirection = 1;

        int[] possibleCoordinates = new int[2];

        switch (getColor()) {
            case "White":
                moveDirection = -1;
                break;
            
            case "Black":
                moveDirection = 1;
                break;
        }

        try {

            ChessPiece pieceInFront1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]];
            if (pieceInFront1.getType() == "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {

            ChessPiece pieceInDiagonal1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] + moveDirection];
            if (pieceInDiagonal1.getColor() != getColor() && pieceInDiagonal1.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] + moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }

        } catch(ArrayIndexOutOfBoundsException e) {}

        try {

            ChessPiece pieceInDiagonal2 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] - moveDirection];
            if (pieceInDiagonal2.getColor() != getColor() && pieceInDiagonal2.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] - moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
                
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        try {
            ChessPiece pieceInFront1 = chessBoard[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]];
            ChessPiece pieceInFront2 = chessBoard[pieceCoordinates[0] + 2*moveDirection][pieceCoordinates[1]];
            if (isUnmoved && pieceInFront2.getType()=="Empty" && pieceInFront1.getType()=="Empty") {
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleCoordinates[0] = pieceCoordinates[0] + 2*moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        return possibleCoordinatesList;
        } 
    }