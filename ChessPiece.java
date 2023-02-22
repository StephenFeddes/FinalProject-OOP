import javax.swing.ImageIcon;

public abstract class ChessPiece {
    private String type;
    private String color;
    private ImageIcon pieceImage;
    
    public ImageIcon getPieceImage() {return pieceImage; };
    public String getType() { return type; }
    public String getColor() { return color; }

    public void setPieceImage(ImageIcon pieceImage) { this.pieceImage = pieceImage; }
    public void setType(String pieceType) { this.type = pieceType; }
    public void setColor(String pieceColor) { this.color = pieceColor; }

    public ChessPiece(String pieceColor) {
        setColor(pieceColor);
    }

    abstract void movePiece(ChessPiece[][] chessBoard);
}
