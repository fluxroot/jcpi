/*
 * Copyright 2007-2014 the original author or authors.
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

  public final long[] pawns = new long[IntColor.values.length];
  public final long[] knights = new long[IntColor.values.length];
  public final long[] bishops = new long[IntColor.values.length];
  public final long[] rooks = new long[IntColor.values.length];
  public final long[] queens = new long[IntColor.values.length];
  public final long[] kings = new long[IntColor.values.length];

  public final int[][] castling = new int[IntColor.values.length][IntCastling.values.length];
  public int enPassant = Square.NOSQUARE;
  public int activeColor = IntColor.WHITE;
  public int halfMoveClock = 0;
  private int halfMoveNumber;

  private final State[] stack = new State[MAX_GAMEMOVES];
  private int stackSize = 0;

  private static final class State {
    public final int[][] castling = new int[IntColor.values.length][IntCastling.values.length];
    public int enPassant = Square.NOSQUARE;
    public int halfMoveClock = 0;

    public State() {
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          this.castling[color][castling] = IntFile.NOFILE;
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
      board[square] = IntPiece.NOPIECE;

      GenericPiece genericPiece = genericBoard.getPiece(Square.toGenericPosition(square));
      if (genericPiece != null) {
        int piece = IntPiece.valueOf(genericPiece);
        put(piece, square);
      }
    }

    // Initialize castling
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        GenericFile genericFile = genericBoard.getCastling(IntColor.toGenericColor(color), IntCastling.toGenericCastling(castling));
        if (genericFile != null) {
          this.castling[color][castling] = IntFile.valueOf(genericFile);
        } else {
          this.castling[color][castling] = IntFile.NOFILE;
        }
      }
    }

    // Initialize en passant
    if (genericBoard.getEnPassant() != null) {
      enPassant = Square.valueOf(genericBoard.getEnPassant());
    }

    // Initialize active color
    if (activeColor != IntColor.valueOf(genericBoard.getActiveColor())) {
      activeColor = IntColor.valueOf(genericBoard.getActiveColor());
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
      if (board[square] != IntPiece.NOPIECE) {
        genericBoard.setPiece(IntPiece.toGenericPiece(board[square]), Square.toGenericPosition(square));
      }
    }

    // Set castling
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        if (this.castling[color][castling] != IntFile.NOFILE) {
          genericBoard.setCastling(IntColor.toGenericColor(color), IntCastling.toGenericCastling(castling), IntFile.toGenericFile(this.castling[color][castling]));
        }
      }
    }

    // Set en passant
    if (enPassant != Square.NOSQUARE) {
      genericBoard.setEnPassant(Square.toGenericPosition(enPassant));
    }

    // Set active color
    genericBoard.setActiveColor(IntColor.toGenericColor(activeColor));

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
    if (activeColor == IntColor.valueOf(GenericColor.BLACK)) {
      ++halfMoveNumber;
    }
  }

  private void put(int piece, int square) {
    assert IntPiece.isValid(piece);
    assert Square.isValid(square);
    assert board[square] == IntPiece.NOPIECE;

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color] |= Square.toBitboard(square);
        break;
      case IntChessman.KNIGHT:
        knights[color] |= Square.toBitboard(square);
        break;
      case IntChessman.BISHOP:
        bishops[color] |= Square.toBitboard(square);
        break;
      case IntChessman.ROOK:
        rooks[color] |= Square.toBitboard(square);
        break;
      case IntChessman.QUEEN:
        queens[color] |= Square.toBitboard(square);
        break;
      case IntChessman.KING:
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
    assert IntPiece.isValid(board[square]);

    int piece = board[square];

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color] &= ~(Square.toBitboard(square));
        break;
      case IntChessman.KNIGHT:
        knights[color] &= ~(Square.toBitboard(square));
        break;
      case IntChessman.BISHOP:
        bishops[color] &= ~(Square.toBitboard(square));
        break;
      case IntChessman.ROOK:
        rooks[color] &= ~(Square.toBitboard(square));
        break;
      case IntChessman.QUEEN:
        queens[color] &= ~(Square.toBitboard(square));
        break;
      case IntChessman.KING:
        kings[color] &= ~(Square.toBitboard(square));
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = IntPiece.NOPIECE;

    return piece;
  }

  public void makeMove(int move) {
    State entry = stack[stackSize];

    // Get variables
    int type = Move.getType(move);
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originPiece = Move.getOriginPiece(move);
    int originColor = IntPiece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);
    int captureSquare = targetSquare;
    if (type == Move.Type.ENPASSANT) {
      captureSquare += (originColor == IntColor.WHITE ? Square.deltaS : Square.deltaN);
    }

    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Save enPassant
    entry.enPassant = enPassant;

    // Save halfMoveClock
    entry.halfMoveClock = halfMoveClock;

    // Remove target piece and update castling rights
    if (targetPiece != IntPiece.NOPIECE) {
      assert targetPiece == board[captureSquare];
      remove(captureSquare);

      clearCastling(captureSquare);
    }

    // Move piece
    assert originPiece == board[originSquare];
    remove(originSquare);
    if (type == Move.Type.PAWNPROMOTION) {
      put(IntPiece.valueOf(Move.getPromotion(move), originColor), targetSquare);
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

      assert IntPiece.getChessman(board[rookOriginSquare]) == IntChessman.ROOK;
      int rookPiece = remove(rookOriginSquare);
      put(rookPiece, rookTargetSquare);
    }

    // Update castling
    clearCastling(originSquare);

    // Update enPassant
    if (type == Move.Type.PAWNDOUBLE) {
      enPassant = targetSquare + (originColor == IntColor.WHITE ? Square.deltaS : Square.deltaN);
      assert Square.isValid(enPassant);
    } else {
      enPassant = Square.NOSQUARE;
    }

    // Update activeColor
    activeColor = IntColor.opposite(activeColor);

    // Update halfMoveClock
    if (IntPiece.getChessman(originPiece) == IntChessman.PAWN || targetPiece != IntPiece.NOPIECE) {
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
    int originColor = IntPiece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);
    int captureSquare = targetSquare;
    if (type == Move.Type.ENPASSANT) {
      captureSquare += (originColor == IntColor.WHITE ? Square.deltaS : Square.deltaN);
      assert Square.isValid(captureSquare);
    }

    // Update fullMoveNumber
    --halfMoveNumber;

    // Update activeColor
    activeColor = IntColor.opposite(activeColor);

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

      assert IntPiece.getChessman(board[rookTargetSquare]) == IntChessman.ROOK;
      int rookPiece = remove(rookTargetSquare);
      put(rookPiece, rookOriginSquare);
    }

    // Undo move piece
    remove(targetSquare);
    put(originPiece, originSquare);

    // Restore target piece
    if (targetPiece != IntPiece.NOPIECE) {
      put(targetPiece, captureSquare);
    }

    // Restore halfMoveClock
    halfMoveClock = entry.halfMoveClock;

    // Restore enPassant
    enPassant = entry.enPassant;

    // Restore castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        if (entry.castling[color][castling] != this.castling[color][castling]) {
          this.castling[color][castling] = entry.castling[color][castling];
        }
      }
    }
  }

  private void clearCastling(int color, int castling) {
    assert IntColor.isValid(color);
    assert IntCastling.isValid(castling);

    if (this.castling[color][castling] != IntFile.NOFILE) {
      this.castling[color][castling] = IntFile.NOFILE;
    }
  }

  private void clearCastling(int square) {
    assert Square.isLegal(square);

    switch (square) {
      case Square.a1:
        clearCastling(IntColor.WHITE, IntCastling.QUEENSIDE);
        break;
      case Square.h1:
        clearCastling(IntColor.WHITE, IntCastling.KINGSIDE);
        break;
      case Square.a8:
        clearCastling(IntColor.BLACK, IntCastling.QUEENSIDE);
        break;
      case Square.h8:
        clearCastling(IntColor.BLACK, IntCastling.KINGSIDE);
        break;
      case Square.e1:
        clearCastling(IntColor.WHITE, IntCastling.QUEENSIDE);
        clearCastling(IntColor.WHITE, IntCastling.KINGSIDE);
        break;
      case Square.e8:
        clearCastling(IntColor.BLACK, IntCastling.QUEENSIDE);
        clearCastling(IntColor.BLACK, IntCastling.KINGSIDE);
        break;
      default:
        break;
    }
  }

}
