/*
 * Copyright 2007-2019 The Java Chess Protocol Interface Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.*;

final class Board {

  private static final int BOARDSIZE = 128;
  private static final int MAX_GAMEMOVES = 4096;

  public final int[] board = new int[BOARDSIZE];

  public final long[] pawns = new long[Color.values.length];
  public final long[] knights = new long[Color.values.length];
  public final long[] bishops = new long[Color.values.length];
  public final long[] rooks = new long[Color.values.length];
  public final long[] queens = new long[Color.values.length];
  public final long[] kings = new long[Color.values.length];

  public final int[][] castling = new int[Color.values.length][Castling.values.length];
  public int enPassant = Square.NOSQUARE;
  public int activeColor = Color.WHITE;
  public int halfMoveClock = 0;
  private int halfMoveNumber;

  private final State[] stack = new State[MAX_GAMEMOVES];
  private int stackSize = 0;

  private static final class State {
    public final int[][] castling = new int[Color.values.length][Castling.values.length];
    public int enPassant = Square.NOSQUARE;
    public int halfMoveClock = 0;

    public State() {
      for (int color : Color.values) {
        for (int castling : Castling.values) {
          this.castling[color][castling] = File.NOFILE;
        }
      }
    }
  }

  public Board(GenericBoard genericBoard) {
    assert genericBoard != null;

    // Initialize stack
    for (int i = 0; i < stack.length; ++i) {
      stack[i] = new State();
    }

    // Initialize board
    for (int square : Square.values) {
      board[square] = Piece.NOPIECE;

      GenericPiece genericPiece = genericBoard.getPiece(Square.toGenericPosition(square));
      if (genericPiece != null) {
        int piece = Piece.valueOf(genericPiece);
        put(piece, square);
      }
    }

    // Initialize castling
    for (int color : Color.values) {
      for (int castling : Castling.values) {
        GenericFile genericFile = genericBoard.getCastling(Color.toGenericColor(color), Castling.toGenericCastling(castling));
        if (genericFile != null) {
          this.castling[color][castling] = File.valueOf(genericFile);
        } else {
          this.castling[color][castling] = File.NOFILE;
        }
      }
    }

    // Initialize en passant
    if (genericBoard.getEnPassant() != null) {
      enPassant = Square.valueOf(genericBoard.getEnPassant());
    }

    // Initialize active color
    if (activeColor != Color.valueOf(genericBoard.getActiveColor())) {
      activeColor = Color.valueOf(genericBoard.getActiveColor());
    }

    // Initialize half move clock
    halfMoveClock = genericBoard.getHalfMoveClock();

    // Initialize the full move number
    setFullMoveNumber(genericBoard.getFullMoveNumber());
  }

  public GenericBoard toGenericBoard() {
    GenericBoard genericBoard = new GenericBoard();

    // Set board
    for (int square : Square.values) {
      if (board[square] != Piece.NOPIECE) {
        genericBoard.setPiece(Piece.toGenericPiece(board[square]), Square.toGenericPosition(square));
      }
    }

    // Set castling
    for (int color : Color.values) {
      for (int castling : Castling.values) {
        if (this.castling[color][castling] != File.NOFILE) {
          genericBoard.setCastling(Color.toGenericColor(color), Castling.toGenericCastling(castling), File.toGenericFile(this.castling[color][castling]));
        }
      }
    }

    // Set en passant
    if (enPassant != Square.NOSQUARE) {
      genericBoard.setEnPassant(Square.toGenericPosition(enPassant));
    }

    // Set active color
    genericBoard.setActiveColor(Color.toGenericColor(activeColor));

    // Set half move clock
    genericBoard.setHalfMoveClock(halfMoveClock);

    // Set full move number
    genericBoard.setFullMoveNumber(getFullMoveNumber());

    return genericBoard;
  }

  public String toString() {
    return toGenericBoard().toString();
  }

  public int getFullMoveNumber() {
    return halfMoveNumber / 2;
  }

  private void setFullMoveNumber(int fullMoveNumber) {
    assert fullMoveNumber > 0;

    halfMoveNumber = fullMoveNumber * 2;
    if (activeColor == Color.valueOf(GenericColor.BLACK)) {
      ++halfMoveNumber;
    }
  }

  private void put(int piece, int square) {
    assert Piece.isValid(piece);
    assert Square.isValid(square);
    assert board[square] == Piece.NOPIECE;

    int chessman = Piece.getChessman(piece);
    int color = Piece.getColor(piece);

    switch (chessman) {
      case PieceType.PAWN:
        pawns[color] |= Square.toBitboard(square);
        break;
      case PieceType.KNIGHT:
        knights[color] |= Square.toBitboard(square);
        break;
      case PieceType.BISHOP:
        bishops[color] |= Square.toBitboard(square);
        break;
      case PieceType.ROOK:
        rooks[color] |= Square.toBitboard(square);
        break;
      case PieceType.QUEEN:
        queens[color] |= Square.toBitboard(square);
        break;
      case PieceType.KING:
        kings[color] |= Square.toBitboard(square);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = piece;
  }

  private int remove(int square) {
    assert Square.isValid(square);
    assert Piece.isValid(board[square]);

    int piece = board[square];

    int chessman = Piece.getChessman(piece);
    int color = Piece.getColor(piece);

    switch (chessman) {
      case PieceType.PAWN:
        pawns[color] &= ~(Square.toBitboard(square));
        break;
      case PieceType.KNIGHT:
        knights[color] &= ~(Square.toBitboard(square));
        break;
      case PieceType.BISHOP:
        bishops[color] &= ~(Square.toBitboard(square));
        break;
      case PieceType.ROOK:
        rooks[color] &= ~(Square.toBitboard(square));
        break;
      case PieceType.QUEEN:
        queens[color] &= ~(Square.toBitboard(square));
        break;
      case PieceType.KING:
        kings[color] &= ~(Square.toBitboard(square));
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = Piece.NOPIECE;

    return piece;
  }

  public void makeMove(int move) {
    State entry = stack[stackSize];

    // Get variables
    int type = Move.getType(move);
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originPiece = Move.getOriginPiece(move);
    int originColor = Piece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);
    int captureSquare = targetSquare;
    if (type == Move.Type.ENPASSANT) {
      captureSquare += (originColor == Color.WHITE ? Square.deltaS : Square.deltaN);
    }

    // Save castling rights
    for (int color : Color.values) {
      for (int castling : Castling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Save enPassant
    entry.enPassant = enPassant;

    // Save halfMoveClock
    entry.halfMoveClock = halfMoveClock;

    // Remove target piece and update castling rights
    if (targetPiece != Piece.NOPIECE) {
      assert targetPiece == board[captureSquare];
      remove(captureSquare);

      clearCastling(captureSquare);
    }

    // Move piece
    assert originPiece == board[originSquare];
    remove(originSquare);
    if (type == Move.Type.PAWNPROMOTION) {
      put(Piece.valueOf(Move.getPromotion(move), originColor), targetSquare);
    } else {
      put(originPiece, targetSquare);
    }

    // Move rook and update castling rights
    if (type == Move.Type.CASTLING) {
      int rookOriginSquare = Square.NOSQUARE;
      int rookTargetSquare = Square.NOSQUARE;
      switch (targetSquare) {
        case Square.g1:
          rookOriginSquare = Square.h1;
          rookTargetSquare = Square.f1;
          break;
        case Square.c1:
          rookOriginSquare = Square.a1;
          rookTargetSquare = Square.d1;
          break;
        case Square.g8:
          rookOriginSquare = Square.h8;
          rookTargetSquare = Square.f8;
          break;
        case Square.c8:
          rookOriginSquare = Square.a8;
          rookTargetSquare = Square.d8;
          break;
        default:
          assert false : targetSquare;
          break;
      }

      assert Piece.getChessman(board[rookOriginSquare]) == PieceType.ROOK;
      int rookPiece = remove(rookOriginSquare);
      put(rookPiece, rookTargetSquare);
    }

    // Update castling
    clearCastling(originSquare);

    // Update enPassant
    if (type == Move.Type.PAWNDOUBLE) {
      enPassant = targetSquare + (originColor == Color.WHITE ? Square.deltaS : Square.deltaN);
      assert Square.isValid(enPassant);
    } else {
      enPassant = Square.NOSQUARE;
    }

    // Update activeColor
    activeColor = Color.opposite(activeColor);

    // Update halfMoveClock
    if (Piece.getChessman(originPiece) == PieceType.PAWN || targetPiece != Piece.NOPIECE) {
      halfMoveClock = 0;
    } else {
      ++halfMoveClock;
    }

    // Update fullMoveNumber
    ++halfMoveNumber;

    ++stackSize;
    assert stackSize < MAX_GAMEMOVES;
  }

  public void undoMove(int move) {
    --stackSize;
    assert stackSize >= 0;

    State entry = stack[stackSize];

    // Get variables
    int type = Move.getType(move);
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originPiece = Move.getOriginPiece(move);
    int originColor = Piece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);
    int captureSquare = targetSquare;
    if (type == Move.Type.ENPASSANT) {
      captureSquare += (originColor == Color.WHITE ? Square.deltaS : Square.deltaN);
      assert Square.isValid(captureSquare);
    }

    // Update fullMoveNumber
    --halfMoveNumber;

    // Update activeColor
    activeColor = Color.opposite(activeColor);

    // Undo move rook
    if (type == Move.Type.CASTLING) {
      int rookOriginSquare = Square.NOSQUARE;
      int rookTargetSquare = Square.NOSQUARE;
      switch (targetSquare) {
        case Square.g1:
          rookOriginSquare = Square.h1;
          rookTargetSquare = Square.f1;
          break;
        case Square.c1:
          rookOriginSquare = Square.a1;
          rookTargetSquare = Square.d1;
          break;
        case Square.g8:
          rookOriginSquare = Square.h8;
          rookTargetSquare = Square.f8;
          break;
        case Square.c8:
          rookOriginSquare = Square.a8;
          rookTargetSquare = Square.d8;
          break;
        default:
          assert false : targetSquare;
          break;
      }

      assert Piece.getChessman(board[rookTargetSquare]) == PieceType.ROOK;
      int rookPiece = remove(rookTargetSquare);
      put(rookPiece, rookOriginSquare);
    }

    // Undo move piece
    remove(targetSquare);
    put(originPiece, originSquare);

    // Restore target piece
    if (targetPiece != Piece.NOPIECE) {
      put(targetPiece, captureSquare);
    }

    // Restore halfMoveClock
    halfMoveClock = entry.halfMoveClock;

    // Restore enPassant
    enPassant = entry.enPassant;

    // Restore castling rights
    for (int color : Color.values) {
      for (int castling : Castling.values) {
        if (entry.castling[color][castling] != this.castling[color][castling]) {
          this.castling[color][castling] = entry.castling[color][castling];
        }
      }
    }
  }

  private void clearCastling(int color, int castling) {
    assert Color.isValid(color);
    assert Castling.isValid(castling);

    if (this.castling[color][castling] != File.NOFILE) {
      this.castling[color][castling] = File.NOFILE;
    }
  }

  private void clearCastling(int square) {
    assert Square.isLegal(square);

    switch (square) {
      case Square.a1:
        clearCastling(Color.WHITE, Castling.QUEENSIDE);
        break;
      case Square.h1:
        clearCastling(Color.WHITE, Castling.KINGSIDE);
        break;
      case Square.a8:
        clearCastling(Color.BLACK, Castling.QUEENSIDE);
        break;
      case Square.h8:
        clearCastling(Color.BLACK, Castling.KINGSIDE);
        break;
      case Square.e1:
        clearCastling(Color.WHITE, Castling.QUEENSIDE);
        clearCastling(Color.WHITE, Castling.KINGSIDE);
        break;
      case Square.e8:
        clearCastling(Color.BLACK, Castling.QUEENSIDE);
        clearCastling(Color.BLACK, Castling.KINGSIDE);
        break;
      default:
        break;
    }
  }

}
