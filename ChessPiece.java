import javax.swing.ImageIcon;

public abstract class ChessPiece {
    public String type;
    private String color;
    private ImageIcon pieceImage;
    
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

    abstract void movePiece(ChessPiece[][] chessBoard);
}