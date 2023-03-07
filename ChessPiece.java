import javax.swing.ImageIcon;
import java.awt.Color;
import java.util.ArrayList;

public abstract class ChessPiece {
    public String type;
    private String color;
    private ImageIcon pieceImage;
    public boolean isUnmoved = true;
    
    public ImageIcon getPieceImage() {return pieceImage; };
    public String getPieceType() { return type; }
    public String getPieceColor() { return color; }

    public void setPieceImage(ImageIcon pieceImage) { this.pieceImage = pieceImage; }
    public void setPieceType(String pieceType) { this.type = pieceType; }
    public void setPieceColor(String pieceColor) { this.color = pieceColor; }

    public ChessPiece(String pieceColor) {
        setPieceColor(pieceColor);
    }

    public ChessPiece() {}

    abstract ArrayList<int[]> possibleMovesList(ChessPiece[][] chessBoard, int[] pieceCoordinates);
}