package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private Collection<ChessMove> availableMoves;
    private Collection<ChessMove> moves;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        this.availableMoves = new ArrayList<>();
        this.moves = new ArrayList<>();

    }

    public Collection<ChessMove> movesCalculator() {
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) { // REMEMBER THIS SYNTAX
            case ChessPiece.PieceType.BISHOP:
                availableMoves = new BishopMovesCalculator(board, myPosition).getMoves();
                break;
            case ChessPiece.PieceType.KNIGHT:
                availableMoves = new KnightMovesCalculator(board, myPosition).getMoves();
                break;
            case ChessPiece.PieceType.ROOK:
                availableMoves = new RookMovesCalculator(board, myPosition).getMoves();
                break;
            case ChessPiece.PieceType.QUEEN:
                availableMoves = new QueenMovesCalculator(board, myPosition).getMoves();
                break;
            case ChessPiece.PieceType.KING:
                availableMoves = new KingMovesCalculator(board, myPosition).getMoves();
                break;
            case ChessPiece.PieceType.PAWN:
                availableMoves = new PawnMovesCalculator(board, myPosition).getMoves();
                break;

        }
        return availableMoves;
    }

    public Collection<ChessMove> sliderMovements(int[][] direction, int maxSteps) {
        int ogRow = myPosition.getRow();
        int ogCol = myPosition.getColumn();
        ChessPiece ogPiece = board.getPiece(myPosition);
        for (int[] dirVec : direction) {
            for (int i = 1; i < maxSteps; i++) {
                int newRow = ogRow + i * dirVec[0];
                int newCol = ogCol + i * dirVec[1];
                if (newRow <= 0 || newRow >= 9 || newCol <= 0 || newCol >= 9) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece newPiece = board.getPiece(new ChessPosition(newRow,newCol));
                if (newPiece == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    continue;
                }
                if (newPiece.getTeamColor() == ogPiece.getTeamColor()) {
                    break;
                }
                if (newPiece.getTeamColor() != ogPiece.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
            }
        }
        return moves;
    }
}



class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> getMoves() {
        int[][] direction = {{1,1},{1,-1},{-1,1},{-1,-1}};
        int maxSteps = 8;
        return sliderMovements(direction, maxSteps);
    }
}

class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> getMoves() {
        int[][] direction = {{1,0},{-1,0},{0,1},{0,-1}};
        int maxSteps = 8;
        return sliderMovements(direction, maxSteps);
    }
}

class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> getMoves() {
        int[][] direction = {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};
        int maxSteps = 2;
        return sliderMovements(direction, maxSteps);
    }
}

class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public Collection<ChessMove> getMoves() {
        int[][] direction = {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};
        int maxSteps = 8;
        return sliderMovements(direction, maxSteps);
    }
}

class KnightMovesCalculator extends PieceMovesCalculator {

    private ChessPosition myPosition;
    private ChessBoard board;
    private Collection<ChessMove> moves;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
        this.myPosition = myPosition;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public Collection<ChessMove> getMoves() {
        int[][] direction = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        int maxSteps = 2;
        return sliderMovements(direction, maxSteps);
    }
}

class PawnMovesCalculator extends PieceMovesCalculator {

    private ChessPosition myPosition;
    private ChessBoard board;
    private Collection<ChessMove> moves;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
        this.myPosition = myPosition;
        this.board = board;
        this.moves = new ArrayList<>();
    }

    public Collection<ChessMove> getMoves() {
        int maxSteps = 2;
        int ogRow = myPosition.getRow();
        int ogCol = myPosition.getColumn();
        ChessPiece ogPiece = board.getPiece(myPosition);
        int[][] direction = {{1, 0}, {1, 1}, {1, -1}};
        int promRow = 8;
        int initialRow = 2;
        if (ogPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = new int[][]{{-1, 0}, {-1, 1}, {-1, -1}};
            initialRow = 7;
            promRow = 1;
        }
        for (int dirVec = 0; dirVec < direction.length; dirVec++) {
            if (dirVec == 0) {
                if (ogRow == initialRow) {
                    maxSteps = 3;
                } else {maxSteps = 2;}
                for (int i = 1; i < maxSteps; i++) {
                    int newRow = ogRow + i * direction[dirVec][0];
                    int newCol = ogCol + i * direction[dirVec][1];
                    if (newRow <= 0 || newRow >= 9 || newCol <= 0 || newCol >= 9) {
                        break;
                    }
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece newPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    if (newRow == promRow && newPiece == null) {
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        break;
                    }
                    if (newPiece == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                        continue;
                    }
                    if (newPiece.getTeamColor() == ogPiece.getTeamColor()) {
                        break;
                    }
                    if (newPiece.getTeamColor() != ogPiece.getTeamColor()) {
                        break;
                    }
                }
            } else {
                for (int i = 1; i < 2; i++) {
                    int newRow = ogRow + i * direction[dirVec][0];
                    int newCol = ogCol + i * direction[dirVec][1];
                    if (newRow <= 0 || newRow >= 9 || newCol <= 0 || newCol >= 9) {
                        break;
                    }
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece newPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    if (newPiece == null) {
                        break;
                    }
                    if (ogPiece.getTeamColor() == newPiece.getTeamColor()) {
                        break;
                    }
                    if (newRow == promRow) {
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
    return moves;
    }
}

