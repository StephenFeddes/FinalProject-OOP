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
}