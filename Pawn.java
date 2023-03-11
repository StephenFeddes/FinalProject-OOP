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

    public ArrayList<int[]> possibleMovesList(ChessPiece[][] boardIn, int[] pieceCoordinates) {

        ArrayList <int[]> possibleCoordinatesList = new ArrayList<int[]>();
        
        int moveDirection = 1;

        int[] possibleCoordinates = new int[2];

        // Gets the direction the pawn is moving, which depends on its color
        switch (getColor()) {
            case "White":
                moveDirection = -1;
                break;
            
            case "Black":
                moveDirection = 1;
                break;
        }

        // Gets the moves the pawn can make that are in front of it
        try {
            ChessPiece pieceInFront1 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1]]; 

            // Checks if the pawn can move at least one tile forward. A pawn can only move forward if the tiles in front are empty
            if (pieceInFront1.getType() == "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1];
                possibleCoordinatesList.add(possibleCoordinates.clone());

                // Checks to see if the pawn can move two tiles forward. If a pawn has already moved, it cannot move forward twice
                ChessPiece pieceInFront2 = boardIn[pieceCoordinates[0] + 2*moveDirection][pieceCoordinates[1]];
                if (pieceInFront2.getType() == "Empty" && isUnmoved) {
                    
                    possibleCoordinates[1] = pieceCoordinates[1];
                    possibleCoordinates[0] = pieceCoordinates[0] + 2*moveDirection;
                    possibleCoordinatesList.add(possibleCoordinates.clone());
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        /* Checks one diagonally adjacent tile in front of the pawn to see if it
        contains an enemy piece. If it does, add that tile's coordinates to the list */
        try {
            ChessPiece pieceInDiagonal1 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] + moveDirection];
            if (pieceInDiagonal1.getColor() != getColor() && pieceInDiagonal1.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] + moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }

        } catch(ArrayIndexOutOfBoundsException e) {}

        // Checks the other diagonally adjacent tile in front of the pawn
        try {

            ChessPiece pieceInDiagonal2 = boardIn[pieceCoordinates[0] + moveDirection][pieceCoordinates[1] - moveDirection];
            if (pieceInDiagonal2.getColor() != getColor() && pieceInDiagonal2.getType() != "Empty") {

                possibleCoordinates[0] = pieceCoordinates[0] + moveDirection;
                possibleCoordinates[1] = pieceCoordinates[1] - moveDirection;
                possibleCoordinatesList.add(possibleCoordinates.clone());
            }
        } catch(ArrayIndexOutOfBoundsException e) {}

        return possibleCoordinatesList;
        } 
    }