public class PieceFactory {
    public ChessPiece createPiece(String pieceType, String pieceColor)
    {
        if (pieceType == null || pieceType.isEmpty())
            return null;
        switch (pieceType) {
        case "Empty":
            return new Empty();
        case "Pawn":
            return new Pawn(pieceColor);
        case "Knight":
            return new Knight(pieceColor);
        case "Bishop":
            return new Bishop(pieceColor);
        case "Rook":
            return new Rook(pieceColor);
        case "Queen":
            return new Queen(pieceColor);
        case "King":
            return new King(pieceColor);
        default:
            throw new IllegalArgumentException("Unknown "+pieceType);
        }
    }
}