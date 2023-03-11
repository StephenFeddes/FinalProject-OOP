import javax.swing.ImageIcon;
import java.awt.Color;
import java.util.ArrayList;

public abstract class ChessPiece {
    public String type;
    private String color;
    private ImageIcon pieceImage;
    public boolean isUnmoved = true;
    public boolean canCastle = false;
    
    public ImageIcon getImage() { return pieceImage; };
    public String getType() { return type; }
    public String getColor() { return color; }

    public void setImage(ImageIcon pieceImage) { this.pieceImage = pieceImage; }
    public void setType(String pieceType) { this.type = pieceType; }
    public void setColor(String pieceColor) { this.color = pieceColor; }

    public ChessPiece(String pieceColor) {
        setColor(pieceColor);
    }

    public ChessPiece() {}

    abstract ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates);

    /* Gets coordinates based on the input and adds them to the list of possible coordinates a piece could go. 
    If isOneCoordinate is false, all the tile coordinates are consecutively added in the inputted direction
    until a tile is reached that cannot be added. If is true, only one coordinate is added at most, period. */
    public void findCoordinates(ChessPiece[][] boardIn, int[] pieceCoordinatesIn, ArrayList <int[]> possibleCoordinatesListIn, int colDirection, int rowDirection, boolean isOneCoordinate) {
        
        try {
        
            int[] possibleCoordinates = pieceCoordinatesIn.clone();
            possibleCoordinates[0] = possibleCoordinates[0]+colDirection;
            possibleCoordinates[1] = possibleCoordinates[1]+rowDirection;
            ChessPiece currentPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];

            // Keeps checking tiles in the direction of colDirection, rowDirection until it reaches a non-empty tile
            while (currentPiece.getColor() != getColor()) {
    
                possibleCoordinatesListIn.add(possibleCoordinates.clone());

                if (isOneCoordinate) {
                    break;
                }

                possibleCoordinates[0] = possibleCoordinates[0]+colDirection;;
                possibleCoordinates[1] = possibleCoordinates[1]+rowDirection;

                // If the current piece added is a piece from the opposing side, stop adding in this direction
                if (currentPiece.getColor() != getColor() && currentPiece.getType() != "Empty") {
                    break;
                }

                // If next piece is the same color as the selected piece, stop the loop. Do not include that piece's coordinates.
                currentPiece = boardIn[possibleCoordinates[0]][possibleCoordinates[1]];
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
    }
}