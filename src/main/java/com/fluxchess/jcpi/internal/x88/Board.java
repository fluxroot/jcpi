/*
 * Copyright 2007-2015 the original author or authors.
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
  public int enPassant = Position.NOPOSITION;
  public int activeColor = IntColor.WHITE;
  public int halfMoveClock = 0;
  private int halfMoveNumber;

  // Board stack
  private final StackEntry[] stack = new StackEntry[MAX_GAMEMOVES];
  private int stackSize = 0;

  private static final class StackEntry {
    public final int[][] castling = new int[IntColor.values.length][IntCastling.values.length];
    public int enPassant = Position.NOPOSITION;
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

    // Initialize position lists
    for (int color : IntColor.values) {
      pawns[color] = new ChessmanList();
      knights[color] = new ChessmanList();
      bishops[color] = new ChessmanList();
      rooks[color] = new ChessmanList();
      queens[color] = new ChessmanList();
      kings[color] = new ChessmanList();
    }

    // Initialize board
    for (int position : Position.values) {
      board[position] = IntPiece.NOPIECE;

      GenericPiece genericPiece = genericBoard.getPiece(Position.toGenericPosition(position));
      if (genericPiece != null) {
        int piece = IntPiece.valueOf(genericPiece);
        put(piece, position);
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
      enPassant = Position.valueOf(genericBoard.getEnPassant());
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
    for (int position : Position.values) {
      if (board[position] != IntPiece.NOPIECE) {
        genericBoard.setPiece(IntPiece.toGenericPiece(board[position]), Position.toGenericPosition(position));
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
    if (enPassant != Position.NOPOSITION) {
      genericBoard.setEnPassant(Position.toGenericPosition(enPassant));
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

  private void put(int piece, int position) {
    assert piece != IntPiece.NOPIECE;
    assert (position & 0x88) == 0;
    assert board[position] == IntPiece.NOPIECE;

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].add(position);
        break;
      case IntChessman.KNIGHT:
        knights[color].add(position);
        break;
      case IntChessman.BISHOP:
        bishops[color].add(position);
        break;
      case IntChessman.ROOK:
        rooks[color].add(position);
        break;
      case IntChessman.QUEEN:
        queens[color].add(position);
        break;
      case IntChessman.KING:
        kings[color].add(position);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[position] = piece;
  }

  private int remove(int position) {
    assert (position & 0x88) == 0;
    assert board[position] != IntPiece.NOPIECE;

    int piece = board[position];

    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].remove(position);
        break;
      case IntChessman.KNIGHT:
        knights[color].remove(position);
        break;
      case IntChessman.BISHOP:
        bishops[color].remove(position);
        break;
      case IntChessman.ROOK:
        rooks[color].remove(position);
        break;
      case IntChessman.QUEEN:
        queens[color].remove(position);
        break;
      case IntChessman.KING:
        kings[color].remove(position);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[position] = IntPiece.NOPIECE;

    return piece;
  }

  private int move(int originPosition, int targetPosition) {
    assert (originPosition & 0x88) == 0;
    assert (targetPosition & 0x88) == 0;
    assert board[originPosition] != IntPiece.NOPIECE;
    assert board[targetPosition] == IntPiece.NOPIECE;

    int piece = board[originPosition];
    int chessman = IntPiece.getChessman(piece);
    int color = IntPiece.getColor(piece);

    switch (chessman) {
      case IntChessman.PAWN:
        pawns[color].remove(originPosition);
        pawns[color].add(targetPosition);
        break;
      case IntChessman.KNIGHT:
        knights[color].remove(originPosition);
        knights[color].add(targetPosition);
        break;
      case IntChessman.BISHOP:
        bishops[color].remove(originPosition);
        bishops[color].add(targetPosition);
        break;
      case IntChessman.ROOK:
        rooks[color].remove(originPosition);
        rooks[color].add(targetPosition);
        break;
      case IntChessman.QUEEN:
        queens[color].remove(originPosition);
        queens[color].add(targetPosition);
        break;
      case IntChessman.KING:
        kings[color].remove(originPosition);
        kings[color].add(targetPosition);
        break;
      default:
        assert false : chessman;
        break;
    }

    board[originPosition] = IntPiece.NOPIECE;
    board[targetPosition] = piece;

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
    int targetPosition = Move.getTargetPosition(move);
    int targetPiece = IntPiece.NOPIECE;
    if (board[targetPosition] != IntPiece.NOPIECE) {
      targetPiece = remove(targetPosition);
      assert targetPiece == Move.getTargetPiece(move);

      switch (targetPosition) {
        case Position.a1:
          if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.a8:
          if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.h1:
          if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.h8:
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
    int originPosition = Move.getOriginPosition(move);
    int originPiece = move(originPosition, targetPosition);

    // Update castling
    switch (originPosition) {
      case Position.a1:
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEROOK;
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.a8:
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKROOK;
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.h1:
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEROOK;
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.h8:
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.BLACKROOK;
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.e1:
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEKING;
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          assert Move.getOriginPiece(move) == IntPiece.WHITEKING;
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.e8:
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
    if (enPassant != Position.NOPOSITION) {
      enPassant = Position.NOPOSITION;
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
    int originPosition = Move.getOriginPosition(move);
    int targetPosition = Move.getTargetPosition(move);
    move(targetPosition, originPosition);

    // Restore the captured chessman
    if (Move.getTargetPiece(move) != IntPiece.NOPIECE) {
      put(Move.getTargetPiece(move), targetPosition);
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
    int originPosition = Move.getOriginPosition(move);
    int targetPosition = Move.getTargetPosition(move);
    int pawnPiece = move(originPosition, targetPosition);
    int pawnColor = IntPiece.getColor(pawnPiece);

    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (originPosition >>> 4 == 1 && pawnColor == IntColor.WHITE) || (originPosition >>> 4 == 6 && pawnColor == IntColor.BLACK) : toGenericBoard().toString() + ":" + Move.toString(move);
    assert (targetPosition >>> 4 == 3 && pawnColor == IntColor.WHITE) || (targetPosition >>> 4 == 4 && pawnColor == IntColor.BLACK);
    assert Math.abs(originPosition - targetPosition) == 32;

    // Calculate the en passant position
    int capturePosition;
    if (pawnColor == IntColor.WHITE) {
      capturePosition = targetPosition - 16;
    } else {
      capturePosition = targetPosition + 16;
    }

    assert (capturePosition & 0x88) == 0;
    assert Math.abs(originPosition - capturePosition) == 16;

    // Update en passant
    enPassant = capturePosition;

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMovePawnDouble(int move) {
    // Move pawn
    move(Move.getTargetPosition(move), Move.getOriginPosition(move));
  }

  private void makeMovePawnPromotion(int move, StackEntry entry) {
    // Remove the pawn at the origin position
    int originPosition = Move.getOriginPosition(move);
    int pawnPiece = remove(originPosition);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    int pawnColor = IntPiece.getColor(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntPiece.getChessman(Move.getOriginPiece(move));
    assert pawnColor == IntPiece.getColor(Move.getOriginPiece(move));

    // Save the captured chessman
    int targetPosition = Move.getTargetPosition(move);
    int targetPiece;
    if (board[targetPosition] != IntPiece.NOPIECE) {
      // Save castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          entry.castling[color][castling] = this.castling[color][castling];
        }
      }

      targetPiece = remove(targetPosition);
      assert targetPiece == Move.getTargetPiece(move);

      switch (targetPosition) {
        case Position.a1:
          if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.a8:
          if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.BLACKROOK;
            castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.h1:
          if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
            assert targetPiece == IntPiece.WHITEROOK;
            castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
          }
          break;
        case Position.h8:
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
    put(promotionPiece, targetPosition);

    // Update en passant
    if (enPassant != Position.NOPOSITION) {
      enPassant = Position.NOPOSITION;
    }

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMovePawnPromotion(int move, StackEntry entry) {
    // Remove promotion chessman at the target position
    int targetPosition = Move.getTargetPosition(move);
    remove(targetPosition);

    // Restore the captured chessman
    if (Move.getTargetPiece(move) != IntPiece.NOPIECE) {
      put(Move.getTargetPiece(move), targetPosition);

      // Restore castling rights
      for (int color : IntColor.values) {
        for (int castling : IntCastling.values) {
          if (entry.castling[color][castling] != this.castling[color][castling]) {
            this.castling[color][castling] = entry.castling[color][castling];
          }
        }
      }
    }

    // Put the pawn at the origin position
    put(Move.getOriginPiece(move), Move.getOriginPosition(move));
  }

  private void makeMoveEnPassant(int move) {
    // Move the pawn
    int originPosition = Move.getOriginPosition(move);
    int targetPosition = Move.getTargetPosition(move);
    int pawn = move(originPosition, targetPosition);
    assert IntPiece.getChessman(pawn) == IntChessman.PAWN;
    int pawnColor = IntPiece.getColor(pawn);

    // Calculate the en passant position
    int capturePosition;
    if (pawnColor == IntColor.WHITE) {
      capturePosition = targetPosition - 16;
    } else {
      assert pawnColor == IntColor.BLACK;

      capturePosition = targetPosition + 16;
    }

    // Remove the captured pawn
    int target = remove(capturePosition);
    assert target == Move.getTargetPiece(move);

    // Update en passant
    if (enPassant != Position.NOPOSITION) {
      enPassant = Position.NOPOSITION;
    }

    // Update half move clock
    halfMoveClock = 0;
  }

  private void undoMoveEnPassant(int move) {
    // Move pawn
    int targetPosition = Move.getTargetPosition(move);
    int pawnPiece = move(targetPosition, Move.getOriginPosition(move));

    // Calculate the en passant position
    int capturePosition;
    if (IntPiece.getColor(pawnPiece) == IntColor.WHITE) {
      capturePosition = targetPosition - 16;
    } else {
      capturePosition = targetPosition + 16;
    }

    // Restore the captured pawn
    put(Move.getTargetPiece(move), capturePosition);
  }

  private void makeMoveCastling(int move, StackEntry entry) {
    // Save castling rights
    for (int color : IntColor.values) {
      for (int castling : IntCastling.values) {
        entry.castling[color][castling] = this.castling[color][castling];
      }
    }

    // Move the king
    int kingOriginPosition = Move.getOriginPosition(move);
    int kingTargetPosition = Move.getTargetPosition(move);
    int kingPiece = move(kingOriginPosition, kingTargetPosition);
    assert IntPiece.getChessman(kingPiece) == IntChessman.KING;

    // Get rook positions and update castling rights
    int rookOriginPosition = Position.NOPOSITION;
    int rookTargetPosition = Position.NOPOSITION;
    switch (kingTargetPosition) {
      case Position.g1:
        rookOriginPosition = Position.h1;
        rookTargetPosition = Position.f1;
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.c1:
        rookOriginPosition = Position.a1;
        rookTargetPosition = Position.d1;
        if (castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.WHITE][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.g8:
        rookOriginPosition = Position.h8;
        rookTargetPosition = Position.f8;
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      case Position.c8:
        rookOriginPosition = Position.a8;
        rookTargetPosition = Position.d8;
        if (castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.QUEENSIDE] = IntFile.NOFILE;
        }
        if (castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE) {
          castling[IntColor.BLACK][IntCastling.KINGSIDE] = IntFile.NOFILE;
        }
        break;
      default:
        assert false : kingTargetPosition;
        break;
    }

    // Move rook
    int rookPiece = move(rookOriginPosition, rookTargetPosition);
    assert IntPiece.getChessman(rookPiece) == IntChessman.ROOK;

    // Update en passant
    if (enPassant != Position.NOPOSITION) {
      enPassant = Position.NOPOSITION;
    }

    // Update half move clock
    halfMoveClock++;
  }

  private void undoMoveCastling(int move, StackEntry entry) {
    int kingTargetPosition = Move.getTargetPosition(move);

    // Get the rook positions
    int rookOriginPosition = Position.NOPOSITION;
    int rookTargetPosition = Position.NOPOSITION;
    switch (kingTargetPosition) {
      case Position.g1:
        rookOriginPosition = Position.h1;
        rookTargetPosition = Position.f1;
        break;
      case Position.c1:
        rookOriginPosition = Position.a1;
        rookTargetPosition = Position.d1;
        break;
      case Position.g8:
        rookOriginPosition = Position.h8;
        rookTargetPosition = Position.f8;
        break;
      case Position.c8:
        rookOriginPosition = Position.a8;
        rookTargetPosition = Position.d8;
        break;
      default:
        assert false : String.format("%s: %s", toString(), Move.toString(kingTargetPosition));
        break;
    }

    // Move rook
    move(rookTargetPosition, rookOriginPosition);

    // Move king
    move(kingTargetPosition, Move.getOriginPosition(move));

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
