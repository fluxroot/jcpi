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

  public final ChessmanList[] pawns = new ChessmanList[IntColor.values.length];
  public final ChessmanList[] knights = new ChessmanList[IntColor.values.length];
  public final ChessmanList[] bishops = new ChessmanList[IntColor.values.length];
  public final ChessmanList[] rooks = new ChessmanList[IntColor.values.length];
  public final ChessmanList[] queens = new ChessmanList[IntColor.values.length];
  public final ChessmanList[] kings = new ChessmanList[IntColor.values.length];

  public final int[][] castling = new int[IntColor.values.length][IntCastling.values.length];
  public int enPassant = Square.NOSQUARE;
  public int activeColor = IntColor.WHITE;
  public int halfMoveClock = 0;
  private int halfMoveNumber;

  // Board stack
  private final StackEntry[] stack = new StackEntry[MAX_GAMEMOVES];
  private int stackSize = 0;

  private static final class StackEntry {
    public final int[][] castling = new int[IntColor.values.length][IntCastling.values.length];
    public int enPassant = Square.NOSQUARE;
    public int halfMoveClock = 0;

    public StackEntry() {
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          this.castling[color][castling] = IntFile.NOFILE;
        }
      }
    }
  }

  public Board(GenericBoard genericBoard) {
    assert genericBoard != null;

    for (int i = 0; i < stack.length; ++i) {
      stack[i] = new StackEntry();
    }

    // Initialize chessman lists
    for (int color : IntColor.values) {
      pawns[color] = new ChessmanList();
      knights[color] = new ChessmanList();
      bishops[color] = new ChessmanList();
      rooks[color] = new ChessmanList();
      queens[color] = new ChessmanList();
      kings[color] = new ChessmanList();
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
    assert (square & 0x88) == 0;
    assert board[square] == IntPiece.NOPIECE;

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].add(square);
        break;
      case IntChessman.KNIGHT:
        knights[color].add(square);
        break;
      case IntChessman.BISHOP:
        bishops[color].add(square);
        break;
      case IntChessman.ROOK:
        rooks[color].add(square);
        break;
      case IntChessman.QUEEN:
        queens[color].add(square);
        break;
      case IntChessman.KING:
        kings[color].add(square);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = piece;
  }

  private int remove(int square) {
    assert (square & 0x88) == 0;
    assert board[square] != IntPiece.NOPIECE;

    int piece = board[square];

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].remove(square);
        break;
      case IntChessman.KNIGHT:
        knights[color].remove(square);
        break;
      case IntChessman.BISHOP:
        bishops[color].remove(square);
        break;
      case IntChessman.ROOK:
        rooks[color].remove(square);
        break;
      case IntChessman.QUEEN:
        queens[color].remove(square);
        break;
      case IntChessman.KING:
        kings[color].remove(square);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[square] = IntPiece.NOPIECE;

    return piece;
  }

  private int move(int originSquare, int targetSquare) {
    assert (originSquare & 0x88) == 0;
    assert (targetSquare & 0x88) == 0;
    assert board[originSquare] != IntPiece.NOPIECE;
    assert board[targetSquare] == IntPiece.NOPIECE;

    int piece = board[originSquare];
    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].remove(originSquare);
        pawns[color].add(targetSquare);
        break;
      case IntChessman.KNIGHT:
        knights[color].remove(originSquare);
        knights[color].add(targetSquare);
        break;
      case IntChessman.BISHOP:
        bishops[color].remove(originSquare);
        bishops[color].add(targetSquare);
        break;
      case IntChessman.ROOK:
        rooks[color].remove(originSquare);
        rooks[color].add(targetSquare);
        break;
      case IntChessman.QUEEN:
        queens[color].remove(originSquare);
        queens[color].add(targetSquare);
        break;
      case IntChessman.KING:
        kings[color].remove(originSquare);
        kings[color].add(targetSquare);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[originSquare] = IntPiece.NOPIECE;
    board[targetSquare] = piece;

    return piece;
  }

  public void makeMove(int move) {
    // Get current stack entry
    StackEntry entry = stack[stackSize];

    // Save history
    entry.halfMoveClock = halfMoveClock;
    entry.enPassant = enPassant;

    int type = Move.getType(move);
    switch (type) {
      case Move.Type.NORMAL:
        makeMoveNormal(move, entry);
        break;
      case Move.Type.PAWNDOUBLE:
        makeMovePawnDouble(move);
        break;
      case Move.Type.PAWNPROMOTION:
        makeMovePawnPromotion(move, entry);
        break;
      case Move.Type.ENPASSANT:
        makeMoveEnPassant(move);
        break;
      case Move.Type.CASTLING:
        makeMoveCastling(move, entry);
        break;
      default:
        assert false : type;
        break;
    }

    // Update half move number
    halfMoveNumber++;

    // Update active color
    activeColor = IntColor.opposite(activeColor);

    // Update stack size
    stackSize++;
    assert stackSize < MAX_GAMEMOVES;
  }

  public void undoMove(int move) {
    --stackSize;
    StackEntry entry = stack[stackSize];

    --halfMoveNumber;

    activeColor = IntColor.opposite(activeColor);

    // Restore zobrist history
    halfMoveClock = entry.halfMoveClock;
    enPassant = entry.enPassant;

    int type = Move.getType(move);
    switch (type) {
      case Move.Type.NORMAL:
        undoMoveNormal(move, entry);
        break;
      case Move.Type.PAWNDOUBLE:
        undoMovePawnDouble(move);
        break;
      case Move.Type.PAWNPROMOTION:
        undoMovePawnPromotion(move, entry);
        break;
      case Move.Type.ENPASSANT:
        undoMoveEnPassant(move);
        break;
      case Move.Type.CASTLING:
        undoMoveCastling(move, entry);
        break;
      default:
        assert false : type;
        break;
    }
  }

  private void makeMoveNormal(int move, StackEntry entry) {
    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Save the captured chessman
    int targetSquare = Move.getTargetSquare(move);
    int targetPiece = IntPiece.NOPIECE;
    if (board[targetSquare] != IntPiece.NOPIECE) {
      targetPiece = remove(targetSquare);
      assert targetPiece == Move.getTargetPiece(move);

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
    int originPiece = move(originSquare, targetSquare);

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

    // Update en passant
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Update half move clock
    if (IntPiece.getChessman(originPiece) == IntChessman.PAWN || targetPiece != IntPiece.NOPIECE) {
      halfMoveClock = 0;
    } else {
      ++halfMoveClock;
    }
  }

  private void undoMoveNormal(int move, StackEntry entry) {
    // Move the chessman
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    move(targetSquare, originSquare);

    // Restore the captured chessman
    if (Move.getTargetPiece(move) != IntPiece.NOPIECE) {
      put(Move.getTargetPiece(move), targetSquare);
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

  private void makeMovePawnDouble(int move) {
    // Move pawn
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int pawnPiece = move(originSquare, targetSquare);
    int pawnColor = IntPiece.getColor(pawnPiece);

    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (originSquare >>> 4 == 1 && pawnColor == IntColor.WHITE) || (originSquare >>> 4 == 6 && pawnColor == IntColor.BLACK) : toGenericBoard().toString() + ":" + Move.toString(move);
    assert (targetSquare >>> 4 == 3 && pawnColor == IntColor.WHITE) || (targetSquare >>> 4 == 4 && pawnColor == IntColor.BLACK);
    assert Math.abs(originSquare - targetSquare) == 32;

    // Calculate the en passant square
    int captureSquare;
    if (pawnColor == IntColor.WHITE) {
      captureSquare = targetSquare - 16;
    } else {
      captureSquare = targetSquare + 16;
    }

    assert (captureSquare & 0x88) == 0;
    assert Math.abs(originSquare - captureSquare) == 16;

    // Update en passant
    enPassant = captureSquare;

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMovePawnDouble(int move) {
    // Move pawn
    move(Move.getTargetSquare(move), Move.getOriginSquare(move));
  }

  private void makeMovePawnPromotion(int move, StackEntry entry) {
    // Remove the pawn at the origin square
    int originSquare = Move.getOriginSquare(move);
    int pawnPiece = remove(originSquare);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    int pawnColor = IntPiece.getColor(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntPiece.getChessman(Move.getOriginPiece(move));
    assert pawnColor == IntPiece.getColor(Move.getOriginPiece(move));

    // Save the captured chessman
    int targetSquare = Move.getTargetSquare(move);
    int targetPiece;
    if (board[targetSquare] != IntPiece.NOPIECE) {
      // Save castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          entry.castling[color][castling] = this.castling[color][castling];
        }
      }

      targetPiece = remove(targetSquare);
      assert targetPiece == Move.getTargetPiece(move);

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

    // Create the promotion chessman
    int promotion = Move.getPromotion(move);
    int promotionPiece = IntPiece.valueOf(promotion, pawnColor);
    put(promotionPiece, targetSquare);

    // Update en passant
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMovePawnPromotion(int move, StackEntry entry) {
    // Remove promotion chessman at the target square
    int targetSquare = Move.getTargetSquare(move);
    remove(targetSquare);

    // Restore the captured chessman
    if (Move.getTargetPiece(move) != IntPiece.NOPIECE) {
      put(Move.getTargetPiece(move), targetSquare);

      // Restore castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          if (entry.castling[color][castling] != this.castling[color][castling]) {
            this.castling[color][castling] = entry.castling[color][castling];
          }
        }
      }
    }

    // Put the pawn at the origin square
    put(Move.getOriginPiece(move), Move.getOriginSquare(move));
  }

  private void makeMoveEnPassant(int move) {
    // Move the pawn
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int pawn = move(originSquare, targetSquare);
    assert IntPiece.getChessman(pawn) == IntChessman.PAWN;
    int pawnColor = IntPiece.getColor(pawn);

    // Calculate the en passant square
    int captureSquare;
    if (pawnColor == IntColor.WHITE) {
      captureSquare = targetSquare - 16;
    } else {
      assert pawnColor == IntColor.BLACK;

      captureSquare = targetSquare + 16;
    }

    // Remove the captured pawn
    int target = remove(captureSquare);
    assert target == Move.getTargetPiece(move);

    // Update en passant
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMoveEnPassant(int move) {
    // Move pawn
    int targetSquare = Move.getTargetSquare(move);
    int pawnPiece = move(targetSquare, Move.getOriginSquare(move));

    // Calculate the en passant square
    int captureSquare;
    if (IntPiece.getColor(pawnPiece) == IntColor.WHITE) {
      captureSquare = targetSquare - 16;
    } else {
      captureSquare = targetSquare + 16;
    }

    // Restore the captured pawn
    put(Move.getTargetPiece(move), captureSquare);
  }

  private void makeMoveCastling(int move, StackEntry entry) {
    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Move the king
    int kingOriginSquare = Move.getOriginSquare(move);
    int kingTargetSquare = Move.getTargetSquare(move);
    int kingPiece = move(kingOriginSquare, kingTargetSquare);
    assert IntPiece.getChessman(kingPiece) == IntChessman.KING;

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
    int rookPiece = move(rookOriginSquare, rookTargetSquare);
    assert IntPiece.getChessman(rookPiece) == IntChessman.ROOK;

    // Update en passant
    if (enPassant != Square.NOSQUARE) {
      enPassant = Square.NOSQUARE;
    }

    // Update half move clock
    ++halfMoveClock;
  }

  private void undoMoveCastling(int move, StackEntry entry) {
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
        assert false : String.format("%s: %s", toString(), Move.toString(kingTargetSquare));
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
