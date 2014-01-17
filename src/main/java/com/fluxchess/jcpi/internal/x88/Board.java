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
    assert piece != IntPiece.NOPIECE;
    assert Square.isValid(square);
    assert board[square] == IntPiece.NOPIECE;

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color] |= 1L << Square.toBitSquare(square);
        break;
      case IntChessman.KNIGHT:
        knights[color] |= 1L << Square.toBitSquare(square);
        break;
      case IntChessman.BISHOP:
        bishops[color] |= 1L << Square.toBitSquare(square);
        break;
      case IntChessman.ROOK:
        rooks[color] |= 1L << Square.toBitSquare(square);
        break;
      case IntChessman.QUEEN:
        queens[color] |= 1L << Square.toBitSquare(square);
        break;
      case IntChessman.KING:
        kings[color] |= 1L << Square.toBitSquare(square);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = piece;
  }

  private void remove(int square) {
    assert Square.isValid(square);
    assert board[square] != IntPiece.NOPIECE;

    int piece = board[square];

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color] &= ~(1L << Square.toBitSquare(square));
        break;
      case IntChessman.KNIGHT:
        knights[color] &= ~(1L << Square.toBitSquare(square));
        break;
      case IntChessman.BISHOP:
        bishops[color] &= ~(1L << Square.toBitSquare(square));
        break;
      case IntChessman.ROOK:
        rooks[color] &= ~(1L << Square.toBitSquare(square));
        break;
      case IntChessman.QUEEN:
        queens[color] &= ~(1L << Square.toBitSquare(square));
        break;
      case IntChessman.KING:
        kings[color] &= ~(1L << Square.toBitSquare(square));
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = IntPiece.NOPIECE;
  }

  private void move(int originSquare, int targetSquare) {
    assert Square.isValid(originSquare);
    assert Square.isValid(targetSquare);
    assert board[originSquare] != IntPiece.NOPIECE;
    assert board[targetSquare] == IntPiece.NOPIECE;

    int piece = board[originSquare];
    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color] &= ~(1L << Square.toBitSquare(originSquare));
        pawns[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      case IntChessman.KNIGHT:
        knights[color] &= ~(1L << Square.toBitSquare(originSquare));
        knights[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      case IntChessman.BISHOP:
        bishops[color] &= ~(1L << Square.toBitSquare(originSquare));
        bishops[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      case IntChessman.ROOK:
        rooks[color] &= ~(1L << Square.toBitSquare(originSquare));
        rooks[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      case IntChessman.QUEEN:
        queens[color] &= ~(1L << Square.toBitSquare(originSquare));
        queens[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      case IntChessman.KING:
        kings[color] &= ~(1L << Square.toBitSquare(originSquare));
        kings[color] |= 1L << Square.toBitSquare(targetSquare);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[originSquare] = IntPiece.NOPIECE;
    board[targetSquare] = piece;
  }

  public void makeMove(int move) {
    assert Move.getOriginPiece(move) == board[Move.getOriginSquare(move)];

    State entry = stack[stackSize];

    int type = Move.getType(move);
    switch (type) {
      case Move.Type.NORMAL:
        makeMoveNormal(move, entry);
        break;
      case Move.Type.PAWNDOUBLE:
        makeMovePawnDouble(move, entry);
        break;
      case Move.Type.PAWNPROMOTION:
        makeMovePawnPromotion(move, entry);
        break;
      case Move.Type.ENPASSANT:
        makeMoveEnPassant(move, entry);
        break;
      case Move.Type.CASTLING:
        makeMoveCastling(move, entry);
        break;
      default:
        assert false : type;
        break;
    }

    activeColor = IntColor.opposite(activeColor);

    ++halfMoveNumber;

    ++stackSize;
    assert stackSize < MAX_GAMEMOVES;
  }

  public void undoMove(int move) {
    --stackSize;
    State entry = stack[stackSize];

    --halfMoveNumber;

    activeColor = IntColor.opposite(activeColor);

    int type = Move.getType(move);
    switch (type) {
      case Move.Type.NORMAL:
        undoMoveNormal(move, entry);
        break;
      case Move.Type.PAWNDOUBLE:
        undoMovePawnDouble(move, entry);
        break;
      case Move.Type.PAWNPROMOTION:
        undoMovePawnPromotion(move, entry);
        break;
      case Move.Type.ENPASSANT:
        undoMoveEnPassant(move, entry);
        break;
      case Move.Type.CASTLING:
        undoMoveCastling(move, entry);
        break;
      default:
        assert false : type;
        break;
    }
  }

  private void makeMoveNormal(int move, State entry) {
    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Remove target piece and adjust castling rights.
    int targetSquare = Move.getTargetSquare(move);
    int targetPiece = Move.getTargetPiece(move);
    if (targetPiece != IntPiece.NOPIECE) {
      assert targetPiece == board[targetSquare];
      remove(targetSquare);

      switch (targetSquare) {
        case Square.a1:
          if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.a8:
          if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.h1:
          if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.h8:
          if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        default:
          break;
      }
    }

    // Move piece
    int originSquare = Move.getOriginSquare(move);
    move(originSquare, targetSquare);

    // Update castling
    switch (originSquare) {
      case Square.a1:
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEROOK;
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.a8:
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKROOK;
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.h1:
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEROOK;
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.h8:
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKROOK;
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.e1:
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEKING;
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEKING;
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.e8:
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKKING;
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKKING;
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      default:
        break;
    }

    // Save and update en passant
    entry.enPassant = enPassant;
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Save and update half move clock
    entry.halfMoveClock = halfMoveClock;
    if (IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.PAWN || targetPiece != IntPiece.NOPIECE) {
      halfMoveClock = 0;
    } else {
      ++halfMoveClock;
    }
  }

  private void undoMoveNormal(int move, State entry) {
    // Restore half move clock
    halfMoveClock = entry.halfMoveClock;

    // Restore en passant
    if (entry.enPassant != Square.NOSQUARE) {
      enPassant = entry.enPassant;
    }

    // Move piece
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    assert Move.getOriginPiece(move) == board[targetSquare];
    move(targetSquare, originSquare);

    // Restore target piece
    int targetPiece = Move.getTargetPiece(move);
    if (targetPiece != IntPiece.NOPIECE) {
      put(targetPiece, targetSquare);
    }

    // Restore castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        if (entry.castling[color][castling] != this.castling[color][castling]) {
          this.castling[color][castling] = entry.castling[color][castling];
        }
      }
    }
  }

  private void makeMovePawnDouble(int move, State entry) {
    // Move pawn
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originColor = IntPiece.getColor(Move.getOriginPiece(move));
    assert IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.PAWN;
    assert (originSquare >>> 4 == IntRank.R2 && originColor == IntColor.WHITE) || (originSquare >>> 4 == IntRank.R7 && originColor == IntColor.BLACK);
    assert (targetSquare >>> 4 == IntRank.R4 && originColor == IntColor.WHITE) || (targetSquare >>> 4 == IntRank.R5 && originColor == IntColor.BLACK);
    assert Math.abs(originSquare - targetSquare) == 32;
    move(originSquare, targetSquare);

    // Save and calculate en passant square
    entry.enPassant = enPassant;
    if (originColor == IntColor.WHITE) {
      enPassant = targetSquare - 16;
    } else {
      enPassant = targetSquare + 16;
    }
    assert Square.isValid(enPassant);
    assert Math.abs(originSquare - enPassant) == 16;

    // Save and update half move clock
    entry.halfMoveClock = halfMoveClock;
    halfMoveClock = 0;
  }

  private void undoMovePawnDouble(int move, State entry) {
    // Restore half move clock
    halfMoveClock = entry.halfMoveClock;

    // Restore en passant
    assert enPassant != Square.NOSQUARE;
    enPassant = entry.enPassant;

    // Move pawn
    move(Move.getTargetSquare(move), Move.getOriginSquare(move));
  }

  private void makeMovePawnPromotion(int move, State entry) {
    // Remove target piece and adjust castling rights.
    int targetSquare = Move.getTargetSquare(move);
    int targetPiece = Move.getTargetPiece(move);
    if (targetPiece != IntPiece.NOPIECE) {
      // Save castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          entry.castling[color][castling] = this.castling[color][castling];
        }
      }

      assert targetPiece == board[targetSquare];
      remove(targetSquare);

      switch (targetSquare) {
        case Square.a1:
          if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.a8:
          if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.h1:
          if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        case Square.h8:
          if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        default:
          break;
      }
    }

    // Remove pawn at the origin square
    int originSquare = Move.getOriginSquare(move);
    int originColor = IntPiece.getColor(Move.getOriginPiece(move));
    assert IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.PAWN;
    assert (originSquare >>> 4 == IntRank.R7 && originColor == IntColor.WHITE) || (originSquare >>> 4 == IntRank.R2 && originColor == IntColor.BLACK);
    remove(originSquare);

    // Create promotion chessman
    int promotion = Move.getPromotion(move);
    assert promotion != IntChessman.NOCHESSMAN;
    int promotionPiece = IntPiece.valueOf(promotion, originColor);
    assert (targetSquare >>> 4 == IntRank.R8 && originColor == IntColor.WHITE) || (targetSquare >>> 4 == IntRank.R1 && originColor == IntColor.BLACK);
    put(promotionPiece, targetSquare);

    // Save and update en passant
    entry.enPassant = enPassant;
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Save and update half move clock
    entry.halfMoveClock = halfMoveClock;
    halfMoveClock = 0;
  }

  private void undoMovePawnPromotion(int move, State entry) {
    // Restore half move clock
    halfMoveClock = entry.halfMoveClock;

    // Restore en passant
    if (entry.enPassant != Square.NOSQUARE) {
      enPassant = entry.enPassant;
    }

    // Remove promotion chessman at the target square
    int targetSquare = Move.getTargetSquare(move);
    remove(targetSquare);

    // Restore target piece
    int targetPiece = Move.getTargetPiece(move);
    if (targetPiece != IntPiece.NOPIECE) {
      put(targetPiece, targetSquare);

      // Restore castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          if (entry.castling[color][castling] != this.castling[color][castling]) {
            this.castling[color][castling] = entry.castling[color][castling];
          }
        }
      }
    }

    // Put pawn at the origin square
    put(Move.getOriginPiece(move), Move.getOriginSquare(move));
  }

  private void makeMoveEnPassant(int move, State entry) {
    // Remove target pawn
    int targetSquare = Move.getTargetSquare(move);
    int originColor = IntPiece.getColor(Move.getOriginPiece(move));
    int captureSquare;
    if (originColor == IntColor.WHITE) {
      captureSquare = targetSquare - 16;
    } else {
      captureSquare = targetSquare + 16;
    }
    assert Move.getTargetPiece(move) == board[captureSquare];
    assert IntPiece.getChessman(Move.getTargetPiece(move)) == IntChessman.PAWN;
    assert IntPiece.getColor(Move.getTargetPiece(move)) == IntColor.opposite(originColor);
    remove(captureSquare);

    // Move pawn
    assert IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.PAWN;
    assert targetSquare == enPassant;
    move(Move.getOriginSquare(move), targetSquare);

    // Save and update en passant
    entry.enPassant = enPassant;
    enPassant = Square.NOSQUARE;

    // Update half move clock
    entry.halfMoveClock = halfMoveClock;
    halfMoveClock = 0;
  }

  private void undoMoveEnPassant(int move, State entry) {
    // Restore half move clock
    halfMoveClock = entry.halfMoveClock;

    // Restore en passant
    enPassant = entry.enPassant;

    // Move pawn
    int targetSquare = Move.getTargetSquare(move);
    move(targetSquare, Move.getOriginSquare(move));

    // Restore target pawn
    int captureSquare;
    if (IntPiece.getColor(Move.getOriginPiece(move)) == IntColor.WHITE) {
      captureSquare = targetSquare - 16;
    } else {
      captureSquare = targetSquare + 16;
    }
    put(Move.getTargetPiece(move), captureSquare);
  }

  private void makeMoveCastling(int move, State entry) {
    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Move king
    int kingTargetSquare = Move.getTargetSquare(move);
    assert IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.KING;
    move(Move.getOriginSquare(move), kingTargetSquare);

    // Get rook squares and update castling rights
    int rookOriginSquare = Square.NOSQUARE;
    int rookTargetSquare = Square.NOSQUARE;
    switch (kingTargetSquare) {
      case Square.g1:
        rookOriginSquare = Square.h1;
        rookTargetSquare = Square.f1;
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.c1:
        rookOriginSquare = Square.a1;
        rookTargetSquare = Square.d1;
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.g8:
        rookOriginSquare = Square.h8;
        rookTargetSquare = Square.f8;
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Square.c8:
        rookOriginSquare = Square.a8;
        rookTargetSquare = Square.d8;
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      default:
        assert false : kingTargetSquare;
        break;
    }

    // Move rook
    assert IntPiece.getChessman(board[rookOriginSquare]) == IntChessman.ROOK;
    move(rookOriginSquare, rookTargetSquare);

    // Save and update en passant
    entry.enPassant = enPassant;
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Save and update half move clock
    entry.halfMoveClock = halfMoveClock;
    ++halfMoveClock;
  }

  private void undoMoveCastling(int move, State entry) {
    // Restore half move clock
    halfMoveClock = entry.halfMoveClock;

    // Restore en passant
    if (entry.enPassant != Square.NOSQUARE) {
      enPassant = entry.enPassant;
    }

    int kingTargetSquare = Move.getTargetSquare(move);

    // Get rook squares
    int rookOriginSquare = Square.NOSQUARE;
    int rookTargetSquare = Square.NOSQUARE;
    switch (kingTargetSquare) {
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
        assert false : kingTargetSquare;
        break;
    }

    // Move rook
    move(rookTargetSquare, rookOriginSquare);

    // Move king
    move(kingTargetSquare, Move.getOriginSquare(move));

    // Restore the castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        if (entry.castling[color][castling] != this.castling[color][castling]) {
          this.castling[color][castling] = entry.castling[color][castling];
        }
      }
    }
  }

}
