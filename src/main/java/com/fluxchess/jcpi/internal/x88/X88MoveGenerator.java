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
import com.fluxchess.jcpi.utils.IMoveGenerator;

public final class X88MoveGenerator implements IMoveGenerator {

  // Move deltas
  private static final int[] moveDeltaPawn = {16, 17, 15};
  private static final int[] moveDeltaKnight = {+33, +18, -14, -31, -33, -18, +14, +31};
  private static final int[] moveDeltaBishop = {+17, -15, -17, +15};
  private static final int[] moveDeltaRook = {+16, +1, -16, -1};
  private static final int[] moveDeltaQueen = {+16, +17, +1, -15, -16, -17, -1, +15};
  private static final int[] moveDeltaKing = {+16, +17, +1, -15, -16, -17, -1, +15};

  // Board
  private final Board board;

  private static final class Attack {
    public static final int N = 0; // Neutral
    public static final int D = 1; // Diagonal
    public static final int u = 2; // Diagonal up in one
    public static final int d = 3; // Diagonal down in one
    public static final int S = 4; // Straight
    public static final int s = 5; // Straight in one
    public static final int K = 6; // Knight

    public static final int[] vector = {
      N,N,N,N,N,N,N,N,                 //   0 -   7
      D,N,N,N,N,N,N,S,N,N,N,N,N,N,D,N, //   8 -  23
      N,D,N,N,N,N,N,S,N,N,N,N,N,D,N,N, //  24 -  39
      N,N,D,N,N,N,N,S,N,N,N,N,D,N,N,N, //  40 -  55
      N,N,N,D,N,N,N,S,N,N,N,D,N,N,N,N, //  56 -  71
      N,N,N,N,D,N,N,S,N,N,D,N,N,N,N,N, //  72 -  87
      N,N,N,N,N,D,K,S,K,D,N,N,N,N,N,N, //  88 - 103
      N,N,N,N,N,K,d,s,d,K,N,N,N,N,N,N, // 104 - 119
      S,S,S,S,S,S,s,N,s,S,S,S,S,S,S,N, // 120 - 135
      N,N,N,N,N,K,u,s,u,K,N,N,N,N,N,N, // 136 - 151
      N,N,N,N,N,D,K,S,K,D,N,N,N,N,N,N, // 152 - 167
      N,N,N,N,D,N,N,S,N,N,D,N,N,N,N,N, // 168 - 183
      N,N,N,D,N,N,N,S,N,N,N,D,N,N,N,N, // 184 - 199
      N,N,D,N,N,N,N,S,N,N,N,N,D,N,N,N, // 200 - 215
      N,D,N,N,N,N,N,S,N,N,N,N,N,D,N,N, // 216 - 231
      D,N,N,N,N,N,N,S,N,N,N,N,N,N,D,N, // 232 - 247
      N,N,N,N,N,N,N,N                  // 248 - 255
    };

    public static final int[] deltas = {
      0,  0,  0,  0,  0,  0,  0,  0,                                 //   0 -   7
    -17,  0,  0,  0,  0,  0,  0,-16,  0,  0,  0,  0,  0,  0,-15,  0, //   8 -  23
      0,-17,  0,  0,  0,  0,  0,-16,  0,  0,  0,  0,  0,-15,  0,  0, //  24 -  39
      0,  0,-17,  0,  0,  0,  0,-16,  0,  0,  0,  0,-15,  0,  0,  0, //  40 -  55
      0,  0,  0,-17,  0,  0,  0,-16,  0,  0,  0,-15,  0,  0,  0,  0, //  56 -  71
      0,  0,  0,  0,-17,  0,  0,-16,  0,  0,-15,  0,  0,  0,  0,  0, //  72 -  87
      0,  0,  0,  0,  0,-17,-33,-16,-31,-15,  0,  0,  0,  0,  0,  0, //  88 - 103
      0,  0,  0,  0,  0,-18,-17,-16,-15,-14,  0,  0,  0,  0,  0,  0, // 104 - 119
     -1, -1, -1, -1, -1, -1, -1,  0,  1,  1,  1,  1,  1,  1,  1,  0, // 120 - 135
      0,  0,  0,  0,  0, 14, 15, 16, 17, 18,  0,  0,  0,  0,  0,  0, // 136 - 151
      0,  0,  0,  0,  0, 15, 31, 16, 33, 17,  0,  0,  0,  0,  0,  0, // 152 - 167
      0,  0,  0,  0, 15,  0,  0, 16,  0,  0, 17,  0,  0,  0,  0,  0, // 168 - 183
      0,  0,  0, 15,  0,  0,  0, 16,  0,  0,  0, 17,  0,  0,  0,  0, // 184 - 199
      0,  0, 15,  0,  0,  0,  0, 16,  0,  0,  0,  0, 17,  0,  0,  0, // 200 - 215
      0, 15,  0,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0, 17,  0,  0, // 216 - 231
     15,  0,  0,  0,  0,  0,  0, 16,  0,  0,  0,  0,  0,  0, 17,  0, // 232 - 247
      0,  0,  0,  0,  0,  0,  0, 0                                   // 248 - 255
    };

    private static final int MAXATTACK = 16;

    public int count = 0;
    public final int[] delta = new int[MAXATTACK];
    public final int[] square = new int[MAXATTACK];
  }

  public X88MoveGenerator(GenericBoard genericBoard) {
    if (genericBoard == null) throw new IllegalArgumentException();

    board = new Board(genericBoard);
  }

  X88MoveGenerator(Board board) {
    if (board == null) throw new IllegalArgumentException();

    this.board = board;
  }

  public GenericMove[] getGenericMoves() {
    MoveList moves = getMoves();

    GenericMove[] genericMoves = new GenericMove[moves.size];
    for (int i = 0; i < moves.size; ++i) {
      genericMoves[i] = Move.toGenericMove(moves.moves[i]);
    }

    return genericMoves;
  }

  MoveList getMoves() {
    MoveList moveList = new MoveList();

    Attack attack = new Attack();
    getAttack(attack, Square.toX88Square(Long.numberOfTrailingZeros(board.kings[board.activeColor])), IntColor.opposite(board.activeColor), false);

    if (attack.count > 0) {
      generateEvasion(moveList, attack);
    } else {
      MoveList tempList = new MoveList();

      generateMoves(tempList);

      for (int i = 0; i < tempList.size; ++i) {
        int move = tempList.moves[i];
        if (isLegal(move)) {
          moveList.moves[moveList.size++] = move;
        }
      }
    }

    return moveList;
  }

  public long perft(int depth) {
    long totalNodes = 0;

    MoveList moves = getMoves();

    if (depth <= 1) {
      return moves.size;
    }

    for (int i = 0; i < moves.size; ++i) {
      int move = moves.moves[i];

      board.makeMove(move);
      totalNodes += perft(depth - 1);
      board.undoMove(move);
    }

    return totalNodes;
  }

  private void generateMoves(MoveList moveList) {
    assert moveList != null;

    int activeColor = board.activeColor;

    for (long squares = board.pawns[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addPawnCaptureMoves(moveList, board.board[square], square);
    }
    for (long squares = board.pawns[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addPawnNonCaptureMoves(moveList, board.board[square], square);
    }
    for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(moveList, board.board[square], square, moveDeltaKnight, Square.NOSQUARE);
    }
    for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(moveList, board.board[square], square, moveDeltaBishop, Square.NOSQUARE);
    }
    for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(moveList, board.board[square], square, moveDeltaRook, Square.NOSQUARE);
    }
    for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(moveList, board.board[square], square, moveDeltaQueen, Square.NOSQUARE);
    }
    assert Long.bitCount(board.kings[activeColor]) == 1;
    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor]));
    addMoves(moveList, board.board[kingSquare], kingSquare, moveDeltaKing, Square.NOSQUARE);
    addCastlingMoves(moveList, board.board[kingSquare], kingSquare);
  }

  private void generateEvasion(MoveList moveList, Attack attack) {
    assert moveList != null;
    assert attack != null;
    assert attack.count > 0;

    int activeColor = board.activeColor;

    assert Long.bitCount(board.kings[activeColor]) == 1;
    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor]));
    int kingPiece = board.board[kingSquare];
    int attackerColor = IntColor.opposite(activeColor);
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, kingSquare, kingSquare, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    // Generate king moves
    for (int delta : moveDeltaKing) {
      boolean isOnCheckLine = false;
      for (int i = 0; i < attack.count; ++i) {
        if (IntChessman.isSliding(IntPiece.getChessman(board.board[attack.square[i]])) && delta == attack.delta[i]) {
          isOnCheckLine = true;
          break;
        }
      }
      if (!isOnCheckLine) {
        int targetSquare = kingSquare + delta;
        if ((targetSquare & 0x88) == 0 && !isAttacked(targetSquare, attackerColor)) {
          int targetPiece = board.board[targetSquare];
          if (targetPiece == IntPiece.NOPIECE) {
            int move = Move.setTargetSquare(moveTemplate, targetSquare);
            moveList.moves[moveList.size++] = move;
          } else {
            if (IntPiece.getColor(targetPiece) == attackerColor) {
              assert IntPiece.getChessman(targetPiece) != IntChessman.KING;
              int move = Move.setTargetSquareAndPiece(moveTemplate, targetSquare, targetPiece);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
      }
    }

    // Double check
    if (attack.count >= 2) {
      return;
    }

    assert attack.count == 1;

    int attackerSquare = attack.square[0];
    int attackerPiece = board.board[attackerSquare];

    // Capture the attacker

    addPawnCaptureMovesToTarget(moveList, activeColor, attackerPiece, attackerSquare);
    for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(moveList, board.board[square], square, moveDeltaKnight, attackerSquare);
      }
    }
    for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(moveList, board.board[square], square, moveDeltaBishop, attackerSquare);
      }
    }
    for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(moveList, board.board[square], square, moveDeltaRook, attackerSquare);
      }
    }
    for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(moveList, board.board[square], square, moveDeltaQueen, attackerSquare);
      }
    }

    int attackDelta = attack.delta[0];

    // Interpose a chessman
    if (IntChessman.isSliding(IntPiece.getChessman(board.board[attackerSquare]))) {
      int targetSquare = attackerSquare + attackDelta;
      while (targetSquare != kingSquare) {
        assert (targetSquare & 0x88) == 0;
        assert board.board[targetSquare] == IntPiece.NOPIECE;

        addPawnNonCaptureMovesToTarget(moveList, activeColor, targetSquare);
        for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(moveList, board.board[square], square, moveDeltaKnight, targetSquare);
          }
        }
        for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(moveList, board.board[square], square, moveDeltaBishop, targetSquare);
          }
        }
        for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(moveList, board.board[square], square, moveDeltaRook, targetSquare);
          }
        }
        for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(moveList, board.board[square], square, moveDeltaQueen, targetSquare);
          }
        }

        targetSquare += attackDelta;
      }
    }
  }

  private void addNonCaptureMoves(MoveList moveList, int originPiece, int originSquare, int[] moveDelta, int endSquare) {
    assert moveList != null;
    assert IntPiece.isValid(originPiece);
    assert (originSquare & 0x88) == 0;
    assert board.board[originSquare] == originPiece;
    assert moveDelta != null;
    assert (endSquare & 0x88) == 0 || endSquare == Square.NOSQUARE;

    boolean sliding = IntChessman.isSliding(IntPiece.getChessman(originPiece));
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, originSquare, originSquare, originPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    for (int delta : moveDelta) {
      int targetSquare = originSquare + delta;

      // Get moves to empty squares
      while ((targetSquare & 0x88) == 0 && board.board[targetSquare] == IntPiece.NOPIECE) {
        if (endSquare == Square.NOSQUARE || targetSquare == endSquare) {
          int move = Move.setTargetSquare(moveTemplate, targetSquare);
          moveList.moves[moveList.size++] = move;
        }

        if (!sliding) {
          break;
        }

        targetSquare += delta;
      }
    }
  }

  private void addMoves(MoveList moveList, int originPiece, int originSquare, int[] moveDelta, int endSquare) {
    assert moveList != null;
    assert IntPiece.isValid(originPiece);
    assert (originSquare & 0x88) == 0;
    assert board.board[originSquare] == originPiece;
    assert moveDelta != null;
    assert (endSquare & 0x88) == 0 || endSquare == Square.NOSQUARE;

    boolean sliding = IntChessman.isSliding(IntPiece.getChessman(originPiece));
    int oppositeColor = IntColor.opposite(IntPiece.getColor(originPiece));
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, originSquare, originSquare, originPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    for (int delta : moveDelta) {
      int square = originSquare + delta;

      // Get moves to empty squares
      while ((square & 0x88) == 0) {
        int targetPiece = board.board[square];
        if (targetPiece == IntPiece.NOPIECE) {
          if (endSquare == Square.NOSQUARE || square == endSquare) {
            int move = Move.setTargetSquare(moveTemplate, square);
            moveList.moves[moveList.size++] = move;
          }

          if (!sliding) {
            break;
          }

          square += delta;
        } else {
          if (endSquare == Square.NOSQUARE || square == endSquare) {
            // Get the move to the square the next chessman is standing on
            if (IntPiece.getColor(targetPiece) == oppositeColor
              && IntPiece.getChessman(targetPiece) != IntChessman.KING) {
              int move = Move.setTargetSquareAndPiece(moveTemplate, square, targetPiece);
              moveList.moves[moveList.size++] = move;
            }
          }
          break;
        }
      }
    }
  }

  private void addPawnNonCaptureMoves(MoveList moveList, int pawnPiece, int pawnSquare) {
    assert moveList != null;
    assert IntPiece.isValid(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (pawnSquare & 0x88) == 0;
    assert board.board[pawnSquare] == pawnPiece;

    int pawnColor = IntPiece.getColor(pawnPiece);

    int delta = moveDeltaPawn[0];
    if (pawnColor == IntColor.BLACK) {
      delta *= -1;
    }

    // Move one square forward
    int targetSquare = pawnSquare + delta;
    if ((targetSquare & 0x88) == 0 && board.board[targetSquare] == IntPiece.NOPIECE) {
      // GenericRank.R8 = square > 111
      // GenericRank.R1 = square < 8
      if ((targetSquare > 111 && pawnColor == IntColor.WHITE)
        || (targetSquare < 8 && pawnColor == IntColor.BLACK)) {
        int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
        moveList.moves[moveList.size++] = move;
      } else {
        int move = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;

        // Move two squares forward
        targetSquare += delta;
        if ((targetSquare & 0x88) == 0 && board.board[targetSquare] == IntPiece.NOPIECE) {
          // GenericRank.R4 = end >>> 4 == 3
          // GenericRank.R5 = end >>> 4 == 4
          if (((targetSquare >>> 4) == 3 && pawnColor == IntColor.WHITE)
            || ((targetSquare >>> 4) == 4 && pawnColor == IntColor.BLACK)) {
            assert ((pawnSquare >>> 4) == 1 && (targetSquare >>> 4) == 3 && pawnColor == IntColor.WHITE) || ((pawnSquare >>> 4) == 6 && (targetSquare >>> 4) == 4 && pawnColor == IntColor.BLACK);

            move = Move.valueOf(Move.Type.PAWNDOUBLE, pawnSquare, targetSquare, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
            moveList.moves[moveList.size++] = move;
          }
        }
      }
    }
  }

  private void addPawnNonCaptureMovesToTarget(MoveList moveList, int pawnColor, int targetSquare) {
    assert moveList != null;
    assert IntColor.isValid(pawnColor);
    assert (targetSquare & 0x88) == 0;
    assert board.board[targetSquare] == IntPiece.NOPIECE;

    int delta = moveDeltaPawn[0];
    int pawnPiece = IntPiece.BLACKPAWN;
    if (pawnColor == IntColor.WHITE) {
      delta *= -1;
      pawnPiece = IntPiece.WHITEPAWN;
    }

    // Move one square backward
    int pawnSquare = targetSquare + delta;
    if ((pawnSquare & 0x88) == 0) {
      int piece = board.board[pawnSquare];
      if (piece != IntPiece.NOPIECE) {
        if (piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnSquare, pawnColor)) {
            // GenericRank.R8 = square > 111
            // GenericRank.R1 = square < 8
            if ((targetSquare > 111 && pawnColor == IntColor.WHITE)
              || (targetSquare < 8 && pawnColor == IntColor.BLACK)) {
              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              int move = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
      } else {
        // Move two squares backward
        pawnSquare += delta;
        if ((pawnSquare & 0x88) == 0) {
          // GenericRank.R4 = end >>> 4 == 3
          // GenericRank.R5 = end >>> 4 == 4
          if (((pawnSquare >>> 4) == 1 && pawnColor == IntColor.WHITE)
            || ((pawnSquare >>> 4) == 6 && pawnColor == IntColor.BLACK)) {
            assert ((pawnSquare >>> 4) == 1 && (targetSquare >>> 4) == 3 && pawnColor == IntColor.WHITE) || ((pawnSquare >>> 4) == 6 && (targetSquare >>> 4) == 4 && pawnColor == IntColor.BLACK);

            piece = board.board[pawnSquare];
            if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
              if (!isPinned(pawnSquare, pawnColor)) {
                int move = Move.valueOf(Move.Type.PAWNDOUBLE, pawnSquare, targetSquare, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
                moveList.moves[moveList.size++] = move;
              }
            }
          }
        }
      }
    }
  }

  private void addPawnCaptureMoves(MoveList moveList, int pawnPiece, int pawnSquare) {
    assert moveList != null;
    assert IntPiece.isValid(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (pawnSquare & 0x88) == 0;
    assert board.board[pawnSquare] == pawnPiece;

    int pawnColor = IntPiece.getColor(pawnPiece);

    for (int i = 1; i < moveDeltaPawn.length; ++i) {
      int delta = moveDeltaPawn[i];
      if (pawnColor == IntColor.BLACK) {
        delta *= -1;
      }

      int targetSquare = pawnSquare + delta;
      if ((targetSquare & 0x88) == 0) {
        int targetPiece = board.board[targetSquare];
        if (targetPiece != IntPiece.NOPIECE) {
          if (IntColor.opposite(IntPiece.getColor(targetPiece)) == pawnColor
            && IntPiece.getChessman(targetPiece) != IntChessman.KING) {
            // Capturing move

            if ((targetSquare >>> 4 == IntRank.R8 && pawnColor == IntColor.WHITE)
              || (targetSquare >>> 4 == IntRank.R1 && pawnColor == IntColor.BLACK)) {
              // Pawn promotion capturing move

              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              // Normal capturing move

              int move = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        } else if (targetSquare == board.enPassant) {
          // En passant move
          assert (targetSquare >>> 4 == IntRank.R3 && pawnColor == IntColor.BLACK)
            || ((targetSquare >>> 4) == IntRank.R6 && pawnColor == IntColor.WHITE);

          int captureSquare;
          if (pawnColor == IntColor.WHITE) {
            captureSquare = targetSquare - 16;
          } else {
            captureSquare = targetSquare + 16;
          }
          targetPiece = board.board[captureSquare];
          assert IntPiece.getChessman(targetPiece) == IntChessman.PAWN;
          assert IntPiece.getColor(targetPiece) == IntColor.opposite(pawnColor);

          int move = Move.valueOf(Move.Type.ENPASSANT, pawnSquare, targetSquare, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
          moveList.moves[moveList.size++] = move;
        }
      }
    }
  }

  private void addPawnCaptureMovesToTarget(MoveList moveList, int pawnColor, int targetPiece, int targetSquare) {
    assert moveList != null;
    assert IntColor.isValid(pawnColor);
    assert IntPiece.isValid(targetPiece);
    assert IntPiece.getColor(targetPiece) == IntColor.opposite(pawnColor);
    assert (targetSquare & 0x88) == 0;
    assert board.board[targetSquare] == targetPiece;
    assert IntPiece.getChessman(board.board[targetSquare]) != IntChessman.KING;

    int pawnPiece = IntPiece.BLACKPAWN;
    int enPassantDelta = -16;
    if (pawnColor == IntColor.WHITE) {
      pawnPiece = IntPiece.WHITEPAWN;
      enPassantDelta = 16;
    }
    int enPassantSquare = targetSquare + enPassantDelta;

    for (int i = 1; i < moveDeltaPawn.length; i++) {
      int delta = moveDeltaPawn[i];
      if (pawnColor == IntColor.WHITE) {
        delta *= -1;
      }

      int pawnSquare = targetSquare + delta;
      if ((pawnSquare & 0x88) == 0) {
        int piece = board.board[pawnSquare];
        if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnSquare, pawnColor)) {
            // GenericRank.R8 = square > 111
            // GenericRank.R1 = square < 8
            if ((targetSquare > 111 && pawnColor == IntColor.WHITE)
              || (targetSquare < 8 && pawnColor == IntColor.BLACK)) {
              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, targetPiece, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              int move = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, piece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
        if (enPassantSquare == board.enPassant) {
          // En passant move
          pawnSquare = pawnSquare + enPassantDelta;
          assert (enPassantSquare & 0x88) == 0;
          assert (pawnSquare & 0x88) == 0;

          piece = board.board[pawnSquare];
          if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
            // We found a valid pawn which can do a en passant move

            if (!isPinned(pawnSquare, pawnColor)) {
              assert ((enPassantSquare >>> 4) == 2 && pawnColor == IntColor.BLACK) || ((enPassantSquare >>> 4) == 5 && pawnColor == IntColor.WHITE);
              assert IntPiece.getChessman(targetPiece) == IntChessman.PAWN;

              int move = Move.valueOf(Move.Type.ENPASSANT, pawnSquare, enPassantSquare, piece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
      }
    }
  }

  private void addCastlingMoves(MoveList moveList, int kingPiece, int kingSquare) {
    assert moveList != null;
    assert IntPiece.isValid(kingPiece);
    assert IntPiece.getChessman(kingPiece) == IntChessman.KING;
    assert (kingSquare & 0x88) == 0;
    assert board.board[kingSquare] == kingPiece;

    if (IntPiece.getColor(kingPiece) == IntColor.WHITE) {
      // Do not test g1 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE
        && board.board[Square.f1] == IntPiece.NOPIECE
        && board.board[Square.g1] == IntPiece.NOPIECE
        && !isAttacked(Square.f1, IntColor.BLACK)) {
        assert board.board[Square.e1] == IntPiece.WHITEKING;
        assert board.board[Square.h1] == IntPiece.WHITEROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.g1, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
      // Do not test c1 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE
        && board.board[Square.b1] == IntPiece.NOPIECE
        && board.board[Square.c1] == IntPiece.NOPIECE
        && board.board[Square.d1] == IntPiece.NOPIECE
        && !isAttacked(Square.d1, IntColor.BLACK)) {
        assert board.board[Square.e1] == IntPiece.WHITEKING;
        assert board.board[Square.a1] == IntPiece.WHITEROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.c1, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
    } else {
      // Do not test g8 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE
        && board.board[Square.f8] == IntPiece.NOPIECE
        && board.board[Square.g8] == IntPiece.NOPIECE
        && !isAttacked(Square.f8, IntColor.WHITE)) {
        assert board.board[Square.e8] == IntPiece.BLACKKING;
        assert board.board[Square.h8] == IntPiece.BLACKROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.g8, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
      // Do not test c8 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE
        && board.board[Square.b8] == IntPiece.NOPIECE
        && board.board[Square.c8] == IntPiece.NOPIECE
        && board.board[Square.d8] == IntPiece.NOPIECE
        && !isAttacked(Square.d8, IntColor.WHITE)) {
        assert board.board[Square.e8] == IntPiece.BLACKKING;
        assert board.board[Square.a8] == IntPiece.BLACKROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.c8, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
    }
  }

  private boolean isLegal(int move) {
    // Slow test for en passant
    if (Move.getType(move) == Move.Type.ENPASSANT) {
      int activeColor = board.activeColor;
      board.makeMove(move);
      boolean isCheck = isAttacked(Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor])), IntColor.opposite(activeColor));
      board.undoMove(move);

      return !isCheck;
    }

    int originColor = IntPiece.getColor(Move.getOriginPiece(move));

    // Special test for king
    if (IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.KING) {
      return !isAttacked(Move.getTargetSquare(move), IntColor.opposite(originColor));
    }

    assert Long.bitCount(board.kings[originColor]) == 1;
    if (isPinned(Move.getOriginSquare(move), originColor)) {
      // We are pinned. Test if we move on the line.
      int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[originColor]));
      int attackDeltaOrigin = Attack.deltas[kingSquare - Move.getOriginSquare(move) + 127];
      int attackDeltaTarget = Attack.deltas[kingSquare - Move.getTargetSquare(move) + 127];
      return attackDeltaOrigin == attackDeltaTarget;
    }

    return true;
  }

  private boolean isPinned(int originSquare, int kingColor) {
    assert (originSquare & 0x88) == 0;
    assert IntColor.isValid(kingColor);

    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[kingColor]));

    // We can only be pinned on an attack line
    int attackVector = Attack.vector[kingSquare - originSquare + 127];
    if (attackVector == Attack.N || attackVector == Attack.K) {
      // No line
      return false;
    }

    int delta = Attack.deltas[kingSquare - originSquare + 127];

    // Walk towards the king
    int square = originSquare + delta;
    assert (square & 0x88) == 0;
    while (board.board[square] == IntPiece.NOPIECE) {
      square += delta;
      assert (square & 0x88) == 0;
    }
    if (square != kingSquare) {
      // There's a blocker between me and the king
      return false;
    }

    // Walk away from the king
    square = originSquare - delta;
    while ((square & 0x88) == 0) {
      int attacker = board.board[square];
      if (attacker != IntPiece.NOPIECE) {
        int attackerColor = IntPiece.getColor(attacker);

        return kingColor != attackerColor && canSliderPseudoAttack(attacker, square, kingSquare);
      } else {
        square -= delta;
      }
    }

    return false;
  }

  private boolean isAttacked(int targetSquare, int attackerColor) {
    assert (targetSquare & 0x88) == 0;
    assert IntColor.isValid(attackerColor);

    return getAttack(new Attack(), targetSquare, attackerColor, true);
  }

  private boolean getAttack(Attack attack, int targetSquare, int attackerColor, boolean stop) {
    assert attack != null;
    assert (targetSquare & 0x88) == 0;
    assert IntColor.isValid(attackerColor);

    attack.count = 0;

    // Pawn attacks
    int pawnPiece = IntPiece.WHITEPAWN;
    int sign = -1;
    if (attackerColor == IntColor.BLACK) {
      pawnPiece = IntPiece.BLACKPAWN;
      sign = 1;
    } else {
      assert attackerColor == IntColor.WHITE;
    }
    int pawnAttackerSquare = targetSquare + sign * 15;
    if ((pawnAttackerSquare & 0x88) == 0) {
      int pawn = board.board[pawnAttackerSquare];
      if (pawn != IntPiece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[targetSquare - pawnAttackerSquare + 127] == sign * -15;
        attack.square[attack.count] = pawnAttackerSquare;
        attack.delta[attack.count] = sign * -15;
        ++attack.count;
      }
    }
    pawnAttackerSquare = targetSquare + sign * 17;
    if ((pawnAttackerSquare & 0x88) == 0) {
      int pawn = board.board[pawnAttackerSquare];
      if (pawn != IntPiece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[targetSquare - pawnAttackerSquare + 127] == sign * -17;
        attack.square[attack.count] = pawnAttackerSquare;
        attack.delta[attack.count] = sign * -17;
        ++attack.count;
      }
    }
    for (long squares = board.knights[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert IntPiece.getChessman(board.board[attackerSquare]) == IntChessman.KNIGHT;
      assert attackerSquare != Square.NOSQUARE;
      assert board.board[attackerSquare] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerSquare]);
      if (canAttack(IntChessman.KNIGHT, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.bishops[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert IntPiece.getChessman(board.board[attackerSquare]) == IntChessman.BISHOP;
      assert attackerSquare != Square.NOSQUARE;
      assert board.board[attackerSquare] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerSquare]);
      if (canAttack(IntChessman.BISHOP, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.rooks[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert IntPiece.getChessman(board.board[attackerSquare]) == IntChessman.ROOK;
      assert attackerSquare != Square.NOSQUARE;
      assert board.board[attackerSquare] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerSquare]);
      if (canAttack(IntChessman.ROOK, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.queens[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert IntPiece.getChessman(board.board[attackerSquare]) == IntChessman.QUEEN;
      assert attackerSquare != Square.NOSQUARE;
      assert board.board[attackerSquare] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerSquare]);
      if (canAttack(IntChessman.QUEEN, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    assert Long.bitCount(board.kings[attackerColor]) == 1;
    int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[attackerColor]));
    assert IntPiece.getChessman(board.board[attackerSquare]) == IntChessman.KING;
    assert attackerSquare != Square.NOSQUARE;
    assert board.board[attackerSquare] != IntPiece.NOPIECE;
    assert attackerColor == IntPiece.getColor(board.board[attackerSquare]);
    if (canAttack(IntChessman.KING, attackerColor, attackerSquare, targetSquare)) {
      if (stop) {
        return true;
      }
      int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];
      assert attackDelta != 0;
      attack.square[attack.count] = attackerSquare;
      attack.delta[attack.count] = attackDelta;
      ++attack.count;
    }

    return false;
  }

  private boolean canAttack(int attackerChessman, int attackerColor, int attackerSquare, int targetSquare) {
    assert IntChessman.isValid(attackerChessman);
    assert IntColor.isValid(attackerColor);
    assert (attackerSquare & 0x88) == 0;
    assert (targetSquare & 0x88) == 0;

    int attackVector = Attack.vector[targetSquare - attackerSquare + 127];

    switch (attackerChessman) {
      case IntChessman.PAWN:
        if (attackVector == Attack.u && attackerColor == IntColor.WHITE) {
          return true;
        } else if (attackVector == Attack.d && attackerColor == IntColor.BLACK) {
          return true;
        }
        break;
      case IntChessman.KNIGHT:
        if (attackVector == Attack.K) {
          return true;
        }
        break;
      case IntChessman.BISHOP:
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
            return true;
          case Attack.D:
            if (canSliderAttack(attackerSquare, targetSquare)) {
              return true;
            }
            break;
          default:
            break;
        }
        break;
      case IntChessman.ROOK:
        switch (attackVector) {
          case Attack.s:
            return true;
          case Attack.S:
            if (canSliderAttack(attackerSquare, targetSquare)) {
              return true;
            }
            break;
          default:
            break;
        }
        break;
      case IntChessman.QUEEN:
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
          case Attack.s:
            return true;
          case Attack.D:
          case Attack.S:
            if (canSliderAttack(attackerSquare, targetSquare)) {
              return true;
            }
            break;
          default:
            break;
        }
        break;
      case IntChessman.KING:
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
          case Attack.s:
            return true;
          default:
            break;
        }
        break;
      default:
        assert false : attackerChessman;
        break;
    }

    return false;
  }

  private boolean canSliderAttack(int attackerSquare, int targetSquare) {
    assert (attackerSquare & 0x88) == 0;
    assert (targetSquare & 0x88) == 0;

    int attackDelta = Attack.deltas[targetSquare - attackerSquare + 127];

    int square = attackerSquare + attackDelta;
    while ((square & 0x88) == 0 && square != targetSquare && board.board[square] == IntPiece.NOPIECE) {
      square += attackDelta;
    }

    return square == targetSquare;
  }

  private boolean canSliderPseudoAttack(int attacker, int attackerSquare, int targetSquare) {
    assert IntPiece.isValid(attacker);
    assert (attackerSquare & 0x88) == 0;
    assert (targetSquare & 0x88) == 0;

    int attackVector;

    switch (IntPiece.getChessman(attacker)) {
      case IntChessman.PAWN:
        break;
      case IntChessman.KNIGHT:
        break;
      case IntChessman.BISHOP:
        attackVector = Attack.vector[targetSquare - attackerSquare + 127];
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
          case Attack.D:
            return true;
          default:
            break;
        }
        break;
      case IntChessman.ROOK:
        attackVector = Attack.vector[targetSquare - attackerSquare + 127];
        switch (attackVector) {
          case Attack.s:
          case Attack.S:
            return true;
          default:
            break;
        }
        break;
      case IntChessman.QUEEN:
        attackVector = Attack.vector[targetSquare - attackerSquare + 127];
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
          case Attack.s:
          case Attack.D:
          case Attack.S:
            return true;
          default:
            break;
        }
        break;
      case IntChessman.KING:
        break;
      default:
        assert false : IntPiece.getChessman(attacker);
        break;
    }

    return false;
  }

}
