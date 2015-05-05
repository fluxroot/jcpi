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
import com.fluxchess.jcpi.utils.IMoveGenerator;

public final class X88MoveGenerator implements IMoveGenerator {

  // Move deltas
  private static final int[] moveDeltaPawn = {16, 17, 15};
  private static final int[] moveDeltaKnight = {+33, +18, -14, -31, -33, -18, +14, +31};
  private static final int[] moveDeltaBishop = {+17, -15, -17, +15};
  private static final int[] moveDeltaRook = {+16, +1, -16, -1};
  private static final int[] moveDeltaQueen = {+16, +17, +1, -15, -16, -17, -1, +15};
  private static final int[] moveDeltaKing = {+16, +17, +1, -15, -16, -17, -1, +15};

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

    public static final int MAXATTACK = 16;

    public int count = 0;
    public final int[] delta = new int[MAXATTACK];
    public final int[] position = new int[MAXATTACK];
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
    getAttack(attack, ChessmanList.next(board.kings[board.activeColor].positions), IntColor.opposite(board.activeColor), false);

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

    for (long positions = board.pawns[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addPawnCaptureMoves(moveList, board.board[position], position);
    }
    for (long positions = board.pawns[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addPawnNonCaptureMoves(moveList, board.board[position], position);
    }
    for (long positions = board.knights[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addMoves(moveList, board.board[position], position, moveDeltaKnight, Position.NOPOSITION);
    }
    for (long positions = board.bishops[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addMoves(moveList, board.board[position], position, moveDeltaBishop, Position.NOPOSITION);
    }
    for (long positions = board.rooks[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addMoves(moveList, board.board[position], position, moveDeltaRook, Position.NOPOSITION);
    }
    for (long positions = board.queens[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      addMoves(moveList, board.board[position], position, moveDeltaQueen, Position.NOPOSITION);
    }
    assert board.kings[activeColor].size() == 1;
    int kingPosition = ChessmanList.next(board.kings[activeColor].positions);
    addMoves(moveList, board.board[kingPosition], kingPosition, moveDeltaKing, Position.NOPOSITION);
    addCastlingMoves(moveList, board.board[kingPosition], kingPosition);
  }

  private void generateEvasion(MoveList moveList, Attack attack) {
    assert moveList != null;
    assert attack != null;

    int activeColor = board.activeColor;

    assert board.kings[activeColor].size() == 1;
    int kingPosition = ChessmanList.next(board.kings[activeColor].positions);
    int kingPiece = board.board[kingPosition];
    int attackerColor = IntColor.opposite(activeColor);
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, kingPosition, kingPosition, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    // Generate king moves
    for (int delta : moveDeltaKing) {
      assert attack.count > 0;
      boolean isOnCheckLine = false;
      for (int i = 0; i < attack.count; i++) {
        if (IntChessman.isSliding(IntPiece.getChessman(board.board[attack.position[i]])) && delta == attack.delta[i]) {
          isOnCheckLine = true;
          break;
        }
      }
      if (!isOnCheckLine) {
        int targetPosition = kingPosition + delta;
        if ((targetPosition & 0x88) == 0 && !isAttacked(targetPosition, attackerColor)) {
          int targetPiece = board.board[targetPosition];
          if (targetPiece == IntPiece.NOPIECE) {
            int move = Move.setTargetPosition(moveTemplate, targetPosition);
            moveList.moves[moveList.size++] = move;
          } else {
            if (IntPiece.getColor(targetPiece) == attackerColor) {
              assert IntPiece.getChessman(targetPiece) != IntChessman.KING;
              int move = Move.setTargetPositionAndPiece(moveTemplate, targetPosition, targetPiece);
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

    int attackerPosition = attack.position[0];
    int attackerPiece = board.board[attackerPosition];

    // Capture the attacker

    addPawnCaptureMovesToTarget(moveList, activeColor, attackerPiece, attackerPosition);
    for (long positions = board.knights[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      if (!isPinned(position, activeColor)) {
        addMoves(moveList, board.board[position], position, moveDeltaKnight, attackerPosition);
      }
    }
    for (long positions = board.bishops[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      if (!isPinned(position, activeColor)) {
        addMoves(moveList, board.board[position], position, moveDeltaBishop, attackerPosition);
      }
    }
    for (long positions = board.rooks[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      if (!isPinned(position, activeColor)) {
        addMoves(moveList, board.board[position], position, moveDeltaRook, attackerPosition);
      }
    }
    for (long positions = board.queens[activeColor].positions; positions != 0; positions &= positions - 1) {
      int position = ChessmanList.next(positions);
      if (!isPinned(position, activeColor)) {
        addMoves(moveList, board.board[position], position, moveDeltaQueen, attackerPosition);
      }
    }

    int attackDelta = attack.delta[0];

    // Interpose a chessman
    if (IntChessman.isSliding(IntPiece.getChessman(board.board[attackerPosition]))) {
      int targetPosition = attackerPosition + attackDelta;
      while (targetPosition != kingPosition) {
        assert (targetPosition & 0x88) == 0;
        assert board.board[targetPosition] == IntPiece.NOPIECE;

        addPawnNonCaptureMovesToTarget(moveList, activeColor, targetPosition);
        for (long positions = board.knights[activeColor].positions; positions != 0; positions &= positions - 1) {
          int position = ChessmanList.next(positions);
          if (!isPinned(position, activeColor)) {
            addNonCaptureMoves(moveList, board.board[position], position, moveDeltaKnight, targetPosition);
          }
        }
        for (long positions = board.bishops[activeColor].positions; positions != 0; positions &= positions - 1) {
          int position = ChessmanList.next(positions);
          if (!isPinned(position, activeColor)) {
            addNonCaptureMoves(moveList, board.board[position], position, moveDeltaBishop, targetPosition);
          }
        }
        for (long positions = board.rooks[activeColor].positions; positions != 0; positions &= positions - 1) {
          int position = ChessmanList.next(positions);
          if (!isPinned(position, activeColor)) {
            addNonCaptureMoves(moveList, board.board[position], position, moveDeltaRook, targetPosition);
          }
        }
        for (long positions = board.queens[activeColor].positions; positions != 0; positions &= positions - 1) {
          int position = ChessmanList.next(positions);
          if (!isPinned(position, activeColor)) {
            addNonCaptureMoves(moveList, board.board[position], position, moveDeltaQueen, targetPosition);
          }
        }

        targetPosition += attackDelta;
      }
    }
  }

  private void addNonCaptureMoves(MoveList moveList, int originPiece, int originPosition, int[] moveDelta, int endPosition) {
    assert moveList != null;
    assert IntPiece.isValid(originPiece);
    assert (originPosition & 0x88) == 0;
    assert board.board[originPosition] == originPiece;
    assert moveDelta != null;
    assert (endPosition & 0x88) == 0 || endPosition == Position.NOPOSITION;

    boolean sliding = IntChessman.isSliding(IntPiece.getChessman(originPiece));
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, originPosition, originPosition, originPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    for (int delta : moveDelta) {
      int targetPosition = originPosition + delta;

      // Get moves to empty squares
      while ((targetPosition & 0x88) == 0 && board.board[targetPosition] == IntPiece.NOPIECE) {
        if (endPosition == Position.NOPOSITION || targetPosition == endPosition) {
          int move = Move.setTargetPosition(moveTemplate, targetPosition);
          moveList.moves[moveList.size++] = move;
        }

        if (!sliding) {
          break;
        }

        targetPosition += delta;
      }
    }
  }

  private void addMoves(MoveList moveList, int originPiece, int originPosition, int[] moveDelta, int endPosition) {
    assert moveList != null;
    assert IntPiece.isValid(originPiece);
    assert (originPosition & 0x88) == 0;
    assert board.board[originPosition] == originPiece;
    assert moveDelta != null;
    assert (endPosition & 0x88) == 0 || endPosition == Position.NOPOSITION;

    boolean sliding = IntChessman.isSliding(IntPiece.getChessman(originPiece));
    int oppositeColor = IntColor.opposite(IntPiece.getColor(originPiece));
    int moveTemplate = Move.valueOf(Move.Type.NORMAL, originPosition, originPosition, originPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);

    for (int delta : moveDelta) {
      int targetPosition = originPosition + delta;

      // Get moves to empty squares
      while ((targetPosition & 0x88) == 0) {
        int targetPiece = board.board[targetPosition];
        if (targetPiece == IntPiece.NOPIECE) {
          if (endPosition == Position.NOPOSITION || targetPosition == endPosition) {
            int move = Move.setTargetPosition(moveTemplate, targetPosition);
            moveList.moves[moveList.size++] = move;
          }

          if (!sliding) {
            break;
          }

          targetPosition += delta;
        } else {
          if (endPosition == Position.NOPOSITION || targetPosition == endPosition) {
            // Get the move to the square the next chessman is standing on
            if (IntPiece.getColor(targetPiece) == oppositeColor
              && IntPiece.getChessman(targetPiece) != IntChessman.KING) {
              int move = Move.setTargetPositionAndPiece(moveTemplate, targetPosition, targetPiece);
              moveList.moves[moveList.size++] = move;
            }
          }
          break;
        }
      }
    }
  }

  private void addPawnNonCaptureMoves(MoveList moveList, int pawnPiece, int pawnPosition) {
    assert moveList != null;
    assert IntPiece.isValid(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (pawnPosition & 0x88) == 0;
    assert board.board[pawnPosition] == pawnPiece;

    int pawnColor = IntPiece.getColor(pawnPiece);

    int delta = moveDeltaPawn[0];
    if (pawnColor == IntColor.BLACK) {
      delta *= -1;
    }

    // Move one square forward
    int targetPosition = pawnPosition + delta;
    if ((targetPosition & 0x88) == 0 && board.board[targetPosition] == IntPiece.NOPIECE) {
      // GenericRank.R8 = position > 111
      // GenericRank.R1 = position < 8
      if ((targetPosition > 111 && pawnColor == IntColor.WHITE)
        || (targetPosition < 8 && pawnColor == IntColor.BLACK)) {
        int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnPosition, targetPosition, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
        moveList.moves[moveList.size++] = move;
        move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
        moveList.moves[moveList.size++] = move;
      } else {
        int move = Move.valueOf(Move.Type.NORMAL, pawnPosition, targetPosition, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;

        // Move two squares forward
        targetPosition += delta;
        if ((targetPosition & 0x88) == 0 && board.board[targetPosition] == IntPiece.NOPIECE) {
          // GenericRank.R4 = end >>> 4 == 3
          // GenericRank.R5 = end >>> 4 == 4
          if (((targetPosition >>> 4) == 3 && pawnColor == IntColor.WHITE)
            || ((targetPosition >>> 4) == 4 && pawnColor == IntColor.BLACK)) {
            assert ((pawnPosition >>> 4) == 1 && (targetPosition >>> 4) == 3 && pawnColor == IntColor.WHITE) || ((pawnPosition >>> 4) == 6 && (targetPosition >>> 4) == 4 && pawnColor == IntColor.BLACK);

            move = Move.valueOf(Move.Type.PAWNDOUBLE, pawnPosition, targetPosition, pawnPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
            moveList.moves[moveList.size++] = move;
          }
        }
      }
    }
  }

  private void addPawnNonCaptureMovesToTarget(MoveList moveList, int pawnColor, int targetPosition) {
    assert moveList != null;
    assert IntColor.isValid(pawnColor);
    assert (targetPosition & 0x88) == 0;
    assert board.board[targetPosition] == IntPiece.NOPIECE;

    int delta = moveDeltaPawn[0];
    int pawnPiece = IntPiece.BLACKPAWN;
    if (pawnColor == IntColor.WHITE) {
      delta *= -1;
      pawnPiece = IntPiece.WHITEPAWN;
    }

    // Move one square backward
    int pawnPosition = targetPosition + delta;
    if ((pawnPosition & 0x88) == 0) {
      int piece = board.board[pawnPosition];
      if (piece != IntPiece.NOPIECE) {
        if (piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnPosition, pawnColor)) {
            // GenericRank.R8 = position > 111
            // GenericRank.R1 = position < 8
            if ((targetPosition > 111 && pawnColor == IntColor.WHITE)
              || (targetPosition < 8 && pawnColor == IntColor.BLACK)) {
              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnPosition, targetPosition, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              int move = Move.valueOf(Move.Type.NORMAL, pawnPosition, targetPosition, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
      } else {
        // Move two squares backward
        pawnPosition += delta;
        if ((pawnPosition & 0x88) == 0) {
          // GenericRank.R4 = end >>> 4 == 3
          // GenericRank.R5 = end >>> 4 == 4
          if (((pawnPosition >>> 4) == 1 && pawnColor == IntColor.WHITE)
            || ((pawnPosition >>> 4) == 6 && pawnColor == IntColor.BLACK)) {
            assert ((pawnPosition >>> 4) == 1 && (targetPosition >>> 4) == 3 && pawnColor == IntColor.WHITE) || ((pawnPosition >>> 4) == 6 && (targetPosition >>> 4) == 4 && pawnColor == IntColor.BLACK);

            piece = board.board[pawnPosition];
            if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
              if (!isPinned(pawnPosition, pawnColor)) {
                int move = Move.valueOf(Move.Type.PAWNDOUBLE, pawnPosition, targetPosition, piece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
                moveList.moves[moveList.size++] = move;
              }
            }
          }
        }
      }
    }
  }

  private void addPawnCaptureMoves(MoveList moveList, int pawnPiece, int pawnPosition) {
    assert moveList != null;
    assert IntPiece.isValid(pawnPiece);
    assert IntPiece.getChessman(pawnPiece) == IntChessman.PAWN;
    assert (pawnPosition & 0x88) == 0;
    assert board.board[pawnPosition] == pawnPiece;

    int pawnColor = IntPiece.getColor(pawnPiece);

    for (int i = 1; i < moveDeltaPawn.length; i++) {
      int delta = moveDeltaPawn[i];
      if (pawnColor == IntColor.BLACK) {
        delta *= -1;
      }

      int targetPosition = pawnPosition + delta;
      if ((targetPosition & 0x88) == 0) {
        int targetPiece = board.board[targetPosition];
        if (targetPiece != IntPiece.NOPIECE) {
          if (IntColor.opposite(IntPiece.getColor(targetPiece)) == pawnColor
            && IntPiece.getChessman(targetPiece) != IntChessman.KING) {
            // Capturing move

            // GenericRank.R8 = position > 111
            // GenericRank.R1 = position < 8
            if ((targetPosition > 111 && pawnColor == IntColor.WHITE)
              || (targetPosition < 8 && pawnColor == IntColor.BLACK)) {
              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnPosition, targetPosition, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              int move = Move.valueOf(Move.Type.NORMAL, pawnPosition, targetPosition, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        } else if (targetPosition == board.enPassant) {
          // En passant move
          assert board.enPassant != Position.NOPOSITION;
          assert ((targetPosition >>> 4) == 2 && pawnColor == IntColor.BLACK) || ((targetPosition >>> 4) == 5 && pawnColor == IntColor.WHITE);

          // Calculate the en passant position
          int enPassantTargetPosition;
          if (pawnColor == IntColor.WHITE) {
            enPassantTargetPosition = targetPosition - 16;
          } else {
            enPassantTargetPosition = targetPosition + 16;
          }
          targetPiece = board.board[enPassantTargetPosition];
          assert IntPiece.getChessman(targetPiece) == IntChessman.PAWN;
          assert IntPiece.getColor(targetPiece) == IntColor.opposite(pawnColor);

          int move = Move.valueOf(Move.Type.ENPASSANT, pawnPosition, targetPosition, pawnPiece, targetPiece, IntChessman.NOCHESSMAN);
          moveList.moves[moveList.size++] = move;
        }
      }
    }
  }

  private void addPawnCaptureMovesToTarget(MoveList moveList, int pawnColor, int targetPiece, int targetPosition) {
    assert moveList != null;
    assert IntColor.isValid(pawnColor);
    assert IntPiece.isValid(targetPiece);
    assert IntPiece.getColor(targetPiece) == IntColor.opposite(pawnColor);
    assert (targetPosition & 0x88) == 0;
    assert board.board[targetPosition] == targetPiece;
    assert IntPiece.getChessman(board.board[targetPosition]) != IntChessman.KING;

    int pawnPiece = IntPiece.BLACKPAWN;
    int enPassantDelta = -16;
    if (pawnColor == IntColor.WHITE) {
      pawnPiece = IntPiece.WHITEPAWN;
      enPassantDelta = 16;
    }
    int enPassantPosition = targetPosition + enPassantDelta;

    for (int i = 1; i < moveDeltaPawn.length; i++) {
      int delta = moveDeltaPawn[i];
      if (pawnColor == IntColor.WHITE) {
        delta *= -1;
      }

      int pawnPosition = targetPosition + delta;
      if ((pawnPosition & 0x88) == 0) {
        int piece = board.board[pawnPosition];
        if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnPosition, pawnColor)) {
            // GenericRank.R8 = position > 111
            // GenericRank.R1 = position < 8
            if ((targetPosition > 111 && pawnColor == IntColor.WHITE)
              || (targetPosition < 8 && pawnColor == IntColor.BLACK)) {
              int moveTemplate = Move.valueOf(Move.Type.PAWNPROMOTION, pawnPosition, targetPosition, piece, targetPiece, IntChessman.NOCHESSMAN);
              int move = Move.setPromotion(moveTemplate, IntChessman.QUEEN);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.ROOK);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.BISHOP);
              moveList.moves[moveList.size++] = move;
              move = Move.setPromotion(moveTemplate, IntChessman.KNIGHT);
              moveList.moves[moveList.size++] = move;
            } else {
              int move = Move.valueOf(Move.Type.NORMAL, pawnPosition, targetPosition, piece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
        if (enPassantPosition == board.enPassant) {
          // En passant move
          pawnPosition = pawnPosition + enPassantDelta;
          assert (enPassantPosition & 0x88) == 0;
          assert (pawnPosition & 0x88) == 0;

          piece = board.board[pawnPosition];
          if (piece != IntPiece.NOPIECE && piece == pawnPiece) {
            // We found a valid pawn which can do a en passant move

            if (!isPinned(pawnPosition, pawnColor)) {
              assert ((enPassantPosition >>> 4) == 2 && pawnColor == IntColor.BLACK) || ((enPassantPosition >>> 4) == 5 && pawnColor == IntColor.WHITE);
              assert IntPiece.getChessman(targetPiece) == IntChessman.PAWN;

              int move = Move.valueOf(Move.Type.ENPASSANT, pawnPosition, enPassantPosition, piece, targetPiece, IntChessman.NOCHESSMAN);
              moveList.moves[moveList.size++] = move;
            }
          }
        }
      }
    }
  }

  private void addCastlingMoves(MoveList moveList, int kingPiece, int kingPosition) {
    assert moveList != null;
    assert IntPiece.isValid(kingPiece);
    assert IntPiece.getChessman(kingPiece) == IntChessman.KING;
    assert (kingPosition & 0x88) == 0;
    assert board.board[kingPosition] == kingPiece;

    if (IntPiece.getColor(kingPiece) == IntColor.WHITE) {
      // Do not test g1 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.WHITE][IntCastling.KINGSIDE] != IntFile.NOFILE
        && board.board[Position.f1] == IntPiece.NOPIECE
        && board.board[Position.g1] == IntPiece.NOPIECE
        && !isAttacked(Position.f1, IntColor.BLACK)) {
        assert board.board[Position.e1] == IntPiece.WHITEKING;
        assert board.board[Position.h1] == IntPiece.WHITEROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingPosition, Position.g1, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
      // Do not test c1 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.WHITE][IntCastling.QUEENSIDE] != IntFile.NOFILE
        && board.board[Position.b1] == IntPiece.NOPIECE
        && board.board[Position.c1] == IntPiece.NOPIECE
        && board.board[Position.d1] == IntPiece.NOPIECE
        && !isAttacked(Position.d1, IntColor.BLACK)) {
        assert board.board[Position.e1] == IntPiece.WHITEKING;
        assert board.board[Position.a1] == IntPiece.WHITEROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingPosition, Position.c1, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
    } else {
      // Do not test g8 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.BLACK][IntCastling.KINGSIDE] != IntFile.NOFILE
        && board.board[Position.f8] == IntPiece.NOPIECE
        && board.board[Position.g8] == IntPiece.NOPIECE
        && !isAttacked(Position.f8, IntColor.WHITE)) {
        assert board.board[Position.e8] == IntPiece.BLACKKING;
        assert board.board[Position.h8] == IntPiece.BLACKROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingPosition, Position.g8, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
      // Do not test c8 whether it is attacked as we will test it in isLegal()
      if (board.castling[IntColor.BLACK][IntCastling.QUEENSIDE] != IntFile.NOFILE
        && board.board[Position.b8] == IntPiece.NOPIECE
        && board.board[Position.c8] == IntPiece.NOPIECE
        && board.board[Position.d8] == IntPiece.NOPIECE
        && !isAttacked(Position.d8, IntColor.WHITE)) {
        assert board.board[Position.e8] == IntPiece.BLACKKING;
        assert board.board[Position.a8] == IntPiece.BLACKROOK;

        int move = Move.valueOf(Move.Type.CASTLING, kingPosition, Position.c8, kingPiece, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
        moveList.moves[moveList.size++] = move;
      }
    }
  }

  private boolean isLegal(int move) {
    // Slow test for en passant
    if (Move.getType(move) == Move.Type.ENPASSANT) {
      int activeColor = board.activeColor;
      board.makeMove(move);
      boolean isCheck = isAttacked(ChessmanList.next(board.kings[activeColor].positions), IntColor.opposite(activeColor));
      board.undoMove(move);

      return !isCheck;
    }

    int originColor = IntPiece.getColor(Move.getOriginPiece(move));

    // Special test for king
    if (IntPiece.getChessman(Move.getOriginPiece(move)) == IntChessman.KING) {
      return !isAttacked(Move.getTargetPosition(move), IntColor.opposite(originColor));
    }

    assert board.kings[originColor].size() == 1;
    if (isPinned(Move.getOriginPosition(move), originColor)) {
      // We are pinned. Test if we move on the line.
      int kingPosition = ChessmanList.next(board.kings[originColor].positions);
      int attackDeltaOrigin = Attack.deltas[kingPosition - Move.getOriginPosition(move) + 127];
      int attackDeltaTarget = Attack.deltas[kingPosition - Move.getTargetPosition(move) + 127];
      return attackDeltaOrigin == attackDeltaTarget;
    }

    return true;
  }

  private boolean isPinned(int originPosition, int kingColor) {
    assert (originPosition & 0x88) == 0;
    assert IntColor.isValid(kingColor);

    int kingPosition = ChessmanList.next(board.kings[kingColor].positions);

    // We can only be pinned on an attack line
    int attackVector = Attack.vector[kingPosition - originPosition + 127];
    if (attackVector == Attack.N || attackVector == Attack.K) {
      // No line
      return false;
    }

    int delta = Attack.deltas[kingPosition - originPosition + 127];

    // Walk towards the king
    int position = originPosition + delta;
    assert (position & 0x88) == 0;
    while (board.board[position] == IntPiece.NOPIECE) {
      position += delta;
      assert (position & 0x88) == 0;
    }
    if (position != kingPosition) {
      // There's a blocker between me and the king
      return false;
    }

    // Walk away from the king
    position = originPosition - delta;
    while ((position & 0x88) == 0) {
      int attacker = board.board[position];
      if (attacker != IntPiece.NOPIECE) {
        int attackerColor = IntPiece.getColor(attacker);

        return kingColor != attackerColor && canSliderPseudoAttack(attacker, position, kingPosition);
      } else {
        position -= delta;
      }
    }

    return false;
  }

  private boolean isAttacked(int targetPosition, int attackerColor) {
    assert (targetPosition & 0x88) == 0;
    assert IntColor.isValid(attackerColor);

    return getAttack(new Attack(), targetPosition, attackerColor, true);
  }

  private boolean getAttack(Attack attack, int targetPosition, int attackerColor, boolean stop) {
    assert attack != null;
    assert (targetPosition & 0x88) == 0;
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
    int pawnAttackerPosition = targetPosition + sign * 15;
    if ((pawnAttackerPosition & 0x88) == 0) {
      int pawn = board.board[pawnAttackerPosition];
      if (pawn != IntPiece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[targetPosition - pawnAttackerPosition + 127] == sign * -15;
        attack.position[attack.count] = pawnAttackerPosition;
        attack.delta[attack.count] = sign * -15;
        attack.count++;
      }
    }
    pawnAttackerPosition = targetPosition + sign * 17;
    if ((pawnAttackerPosition & 0x88) == 0) {
      int pawn = board.board[pawnAttackerPosition];
      if (pawn != IntPiece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[targetPosition - pawnAttackerPosition + 127] == sign * -17;
        attack.position[attack.count] = pawnAttackerPosition;
        attack.delta[attack.count] = sign * -17;
        attack.count++;
      }
    }
    for (long positions = board.knights[attackerColor].positions; positions != 0; positions &= positions - 1) {
      int attackerPosition = ChessmanList.next(positions);
      assert IntPiece.getChessman(board.board[attackerPosition]) == IntChessman.KNIGHT;
      assert attackerPosition != Position.NOPOSITION;
      assert board.board[attackerPosition] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerPosition]);
      if (canAttack(IntChessman.KNIGHT, attackerColor, attackerPosition, targetPosition)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];
        assert attackDelta != 0;
        attack.position[attack.count] = attackerPosition;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    for (long positions = board.bishops[attackerColor].positions; positions != 0; positions &= positions - 1) {
      int attackerPosition = ChessmanList.next(positions);
      assert IntPiece.getChessman(board.board[attackerPosition]) == IntChessman.BISHOP;
      assert attackerPosition != Position.NOPOSITION;
      assert board.board[attackerPosition] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerPosition]);
      if (canAttack(IntChessman.BISHOP, attackerColor, attackerPosition, targetPosition)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];
        assert attackDelta != 0;
        attack.position[attack.count] = attackerPosition;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    for (long positions = board.rooks[attackerColor].positions; positions != 0; positions &= positions - 1) {
      int attackerPosition = ChessmanList.next(positions);
      assert IntPiece.getChessman(board.board[attackerPosition]) == IntChessman.ROOK;
      assert attackerPosition != Position.NOPOSITION;
      assert board.board[attackerPosition] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerPosition]);
      if (canAttack(IntChessman.ROOK, attackerColor, attackerPosition, targetPosition)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];
        assert attackDelta != 0;
        attack.position[attack.count] = attackerPosition;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    for (long positions = board.queens[attackerColor].positions; positions != 0; positions &= positions - 1) {
      int attackerPosition = ChessmanList.next(positions);
      assert IntPiece.getChessman(board.board[attackerPosition]) == IntChessman.QUEEN;
      assert attackerPosition != Position.NOPOSITION;
      assert board.board[attackerPosition] != IntPiece.NOPIECE;
      assert attackerColor == IntPiece.getColor(board.board[attackerPosition]);
      if (canAttack(IntChessman.QUEEN, attackerColor, attackerPosition, targetPosition)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];
        assert attackDelta != 0;
        attack.position[attack.count] = attackerPosition;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    assert board.kings[attackerColor].size() == 1;
    int attackerPosition = ChessmanList.next(board.kings[attackerColor].positions);
    assert IntPiece.getChessman(board.board[attackerPosition]) == IntChessman.KING;
    assert attackerPosition != Position.NOPOSITION;
    assert board.board[attackerPosition] != IntPiece.NOPIECE;
    assert attackerColor == IntPiece.getColor(board.board[attackerPosition]);
    if (canAttack(IntChessman.KING, attackerColor, attackerPosition, targetPosition)) {
      if (stop) {
        return true;
      }
      int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];
      assert attackDelta != 0;
      attack.position[attack.count] = attackerPosition;
      attack.delta[attack.count] = attackDelta;
      attack.count++;
    }

    return false;
  }

  private boolean canAttack(int attackerChessman, int attackerColor, int attackerPosition, int targetPosition) {
    assert IntChessman.isValid(attackerChessman);
    assert IntColor.isValid(attackerColor);
    assert (attackerPosition & 0x88) == 0;
    assert (targetPosition & 0x88) == 0;

    int attackVector = Attack.vector[targetPosition - attackerPosition + 127];

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
            if (canSliderAttack(attackerPosition, targetPosition)) {
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
            if (canSliderAttack(attackerPosition, targetPosition)) {
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
            if (canSliderAttack(attackerPosition, targetPosition)) {
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

  private boolean canSliderAttack(int attackerPosition, int targetPosition) {
    assert (attackerPosition & 0x88) == 0;
    assert (targetPosition & 0x88) == 0;

    int attackDelta = Attack.deltas[targetPosition - attackerPosition + 127];

    int position = attackerPosition + attackDelta;
    while ((position & 0x88) == 0 && position != targetPosition && board.board[position] == IntPiece.NOPIECE) {
      position += attackDelta;
    }

    return position == targetPosition;
  }

  private boolean canSliderPseudoAttack(int attacker, int attackerPosition, int targetPosition) {
    assert IntPiece.isValid(attacker);
    assert (attackerPosition & 0x88) == 0;
    assert (targetPosition & 0x88) == 0;

    int attackVector;

    switch (IntPiece.getChessman(attacker)) {
      case IntChessman.PAWN:
        break;
      case IntChessman.KNIGHT:
        break;
      case IntChessman.BISHOP:
        attackVector = Attack.vector[targetPosition - attackerPosition + 127];
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
        attackVector = Attack.vector[targetPosition - attackerPosition + 127];
        switch (attackVector) {
          case Attack.s:
          case Attack.S:
            return true;
          default:
            break;
        }
        break;
      case IntChessman.QUEEN:
        attackVector = Attack.vector[targetPosition - attackerPosition + 127];
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
