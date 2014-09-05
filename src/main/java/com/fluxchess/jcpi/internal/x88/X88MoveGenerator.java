/*
 * Copyright (C) 2007-2014 Phokham Nonava
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

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;

public final class X88MoveGenerator {

  // Move deltas
  private static final int[][] moveDeltaPawn = {
    {Square.deltaN, Square.deltaNE, Square.deltaNW}, // IntColor.WHITE
    {Square.deltaS, Square.deltaSE, Square.deltaSW}  // IntColor.BLACK
  };
  private static final int[] moveDeltaKnight = {
    Square.deltaN + Square.deltaN + Square.deltaE,
    Square.deltaN + Square.deltaN + Square.deltaW,
    Square.deltaN + Square.deltaE + Square.deltaE,
    Square.deltaN + Square.deltaW + Square.deltaW,
    Square.deltaS + Square.deltaS + Square.deltaE,
    Square.deltaS + Square.deltaS + Square.deltaW,
    Square.deltaS + Square.deltaE + Square.deltaE,
    Square.deltaS + Square.deltaW + Square.deltaW
  };
  private static final int[] moveDeltaBishop = {
    Square.deltaNE, Square.deltaNW, Square.deltaSE, Square.deltaSW
  };
  private static final int[] moveDeltaRook = {
    Square.deltaN, Square.deltaE, Square.deltaS, Square.deltaW
  };
  private static final int[] moveDeltaQueen = {
    Square.deltaN, Square.deltaE, Square.deltaS, Square.deltaW,
    Square.deltaNE, Square.deltaNW, Square.deltaSE, Square.deltaSW
  };
  private static final int[] moveDeltaKing = {
    Square.deltaN, Square.deltaE, Square.deltaS, Square.deltaW,
    Square.deltaNE, Square.deltaNW, Square.deltaSE, Square.deltaSW
  };

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

    public static int index(int square1, int square2) {
      return square1 - square2 + 127;
    }
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
    MoveList list = getMoves();

    GenericMove[] genericMoves = new GenericMove[list.size];
    for (int i = 0; i < list.size; ++i) {
      genericMoves[i] = Move.toGenericMove(list.moves[i]);
    }

    return genericMoves;
  }

  MoveList getMoves() {
    MoveList list = new MoveList();

    Attack attack = new Attack();
    getAttack(attack, Square.toX88Square(Long.numberOfTrailingZeros(board.kings[board.activeColor])), Color.opposite(board.activeColor), false);

    if (attack.count > 0) {
      generateEvasion(list, attack);
    } else {
      MoveList tempList = new MoveList();

      generateMoves(tempList);

      for (int i = 0; i < tempList.size; ++i) {
        int move = tempList.moves[i];
        if (isLegal(move)) {
          list.moves[list.size++] = move;
        }
      }
    }

    return list;
  }

  public long perft(int depth) {
    long totalNodes = 0;

    MoveList list = getMoves();

    if (depth <= 1) {
      return list.size;
    }

    for (int i = 0; i < list.size; ++i) {
      int move = list.moves[i];

      board.makeMove(move);
      totalNodes += perft(depth - 1);
      board.undoMove(move);
    }

    return totalNodes;
  }

  private void generateMoves(MoveList list) {
    assert list != null;

    int activeColor = board.activeColor;

    for (long squares = board.pawns[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addPawnCaptureMoves(list, square);
    }
    for (long squares = board.pawns[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addPawnNonCaptureMoves(list, square);
    }
    for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(list, square, moveDeltaKnight, Square.NOSQUARE);
    }
    for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(list, square, moveDeltaBishop, Square.NOSQUARE);
    }
    for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(list, square, moveDeltaRook, Square.NOSQUARE);
    }
    for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      addMoves(list, square, moveDeltaQueen, Square.NOSQUARE);
    }
    assert Long.bitCount(board.kings[activeColor]) == 1;
    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor]));
    addMoves(list, kingSquare, moveDeltaKing, Square.NOSQUARE);
    addCastlingMoves(list, kingSquare);
  }

  private void generateEvasion(MoveList list, Attack attack) {
    assert list != null;
    assert attack != null;
    assert attack.count > 0;

    int activeColor = board.activeColor;

    assert Long.bitCount(board.kings[activeColor]) == 1;
    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor]));
    int kingPiece = board.board[kingSquare];
    int attackerColor = Color.opposite(activeColor);

    // Generate king moves
    for (int delta : moveDeltaKing) {
      boolean isOnCheckLine = false;
      for (int i = 0; i < attack.count; ++i) {
        if (PieceType.isSliding(Piece.getChessman(board.board[attack.square[i]])) && delta == attack.delta[i]) {
          isOnCheckLine = true;
          break;
        }
      }
      if (!isOnCheckLine) {
        int targetSquare = kingSquare + delta;
        if (Square.isLegal(targetSquare) && !isAttacked(targetSquare, attackerColor)) {
          int targetPiece = board.board[targetSquare];
          if (targetPiece == Piece.NOPIECE) {
            list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, kingSquare, targetSquare, kingPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
          } else {
            if (Piece.getColor(targetPiece) == attackerColor) {
              assert Piece.getChessman(targetPiece) != PieceType.KING;
              list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, kingSquare, targetSquare, kingPiece, targetPiece, PieceType.NOCHESSMAN);
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

    addPawnCaptureMovesToTarget(list, activeColor, attackerSquare);
    for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(list, square, moveDeltaKnight, attackerSquare);
      }
    }
    for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(list, square, moveDeltaBishop, attackerSquare);
      }
    }
    for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(list, square, moveDeltaRook, attackerSquare);
      }
    }
    for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
      int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      if (!isPinned(square, activeColor)) {
        addMoves(list, square, moveDeltaQueen, attackerSquare);
      }
    }

    int attackDelta = attack.delta[0];

    // Interpose a chessman
    if (PieceType.isSliding(Piece.getChessman(board.board[attackerSquare]))) {
      int targetSquare = attackerSquare + attackDelta;
      while (targetSquare != kingSquare) {
        assert Square.isValid(targetSquare);
        assert board.board[targetSquare] == Piece.NOPIECE;

        addPawnNonCaptureMovesToTarget(list, activeColor, targetSquare);
        for (long squares = board.knights[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(list, square, moveDeltaKnight, targetSquare);
          }
        }
        for (long squares = board.bishops[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(list, square, moveDeltaBishop, targetSquare);
          }
        }
        for (long squares = board.rooks[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(list, square, moveDeltaRook, targetSquare);
          }
        }
        for (long squares = board.queens[activeColor]; squares != 0; squares &= squares - 1) {
          int square = Square.toX88Square(Long.numberOfTrailingZeros(squares));
          if (!isPinned(square, activeColor)) {
            addNonCaptureMoves(list, square, moveDeltaQueen, targetSquare);
          }
        }

        targetSquare += attackDelta;
      }
    }
  }

  private void addNonCaptureMoves(MoveList list, int originSquare, int[] moveDelta, int endSquare) {
    assert list != null;
    assert Square.isValid(originSquare);
    assert moveDelta != null;
    assert Square.isValid(endSquare) || endSquare == Square.NOSQUARE;

    int originPiece = board.board[originSquare];
    assert Piece.isValid(originPiece);
    boolean sliding = PieceType.isSliding(Piece.getChessman(originPiece));

    for (int delta : moveDelta) {
      int targetSquare = originSquare + delta;

      // Get moves to empty squares
      while (Square.isLegal(targetSquare) && board.board[targetSquare] == Piece.NOPIECE) {
        if (endSquare == Square.NOSQUARE || targetSquare == endSquare) {
          list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, originSquare, targetSquare, originPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
        }

        if (!sliding) {
          break;
        }

        targetSquare += delta;
      }
    }
  }

  private void addMoves(MoveList list, int originSquare, int[] moveDelta, int endSquare) {
    assert list != null;
    assert Square.isValid(originSquare);
    assert moveDelta != null;
    assert Square.isValid(endSquare) || endSquare == Square.NOSQUARE;

    int originPiece = board.board[originSquare];
    assert Piece.isValid(originPiece);
    boolean sliding = PieceType.isSliding(Piece.getChessman(originPiece));
    int oppositeColor = Color.opposite(Piece.getColor(originPiece));

    for (int delta : moveDelta) {
      int square = originSquare + delta;

      // Get moves to empty squares
      while (Square.isLegal(square)) {
        int targetPiece = board.board[square];
        if (targetPiece == Piece.NOPIECE) {
          if (endSquare == Square.NOSQUARE || square == endSquare) {
            list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, originSquare, square, originPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
          }

          if (!sliding) {
            break;
          }

          square += delta;
        } else {
          if (endSquare == Square.NOSQUARE || square == endSquare) {
            // Get the move to the square the next chessman is standing on
            if (Piece.getColor(targetPiece) == oppositeColor
              && Piece.getChessman(targetPiece) != PieceType.KING) {
              list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, originSquare, square, originPiece, targetPiece, PieceType.NOCHESSMAN);
            }
          }
          break;
        }
      }
    }
  }

  private void addPawnNonCaptureMoves(MoveList list, int pawnSquare) {
    assert list != null;
    assert Square.isValid(pawnSquare);

    int pawnPiece = board.board[pawnSquare];
    assert Piece.isValid(pawnPiece);
    assert Piece.getChessman(pawnPiece) == PieceType.PAWN;
    int pawnColor = Piece.getColor(pawnPiece);

    int delta = moveDeltaPawn[pawnColor][0];

    // Move one square forward
    int targetSquare = pawnSquare + delta;
    if (Square.isLegal(targetSquare) && board.board[targetSquare] == Piece.NOPIECE) {
      if ((pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R8)
        || (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R1)) {
        list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.QUEEN);
        list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.ROOK);
        list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.BISHOP);
        list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.KNIGHT);
      } else {
        list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);

        // Move two squares forward
        targetSquare += delta;
        if (Square.isLegal(targetSquare) && board.board[targetSquare] == Piece.NOPIECE) {
          if ((pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R4)
            || (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R5)) {
            assert (pawnColor == Color.WHITE && Square.getRank(pawnSquare) == Rank.R2 && Square.getRank(targetSquare) == Rank.R4)
              || (pawnColor == Color.BLACK && Square.getRank(pawnSquare) == Rank.R7 && Square.getRank(targetSquare) == Rank.R5);

            list.moves[list.size++] = Move.valueOf(Move.Type.PAWNDOUBLE, pawnSquare, targetSquare, pawnPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
          }
        }
      }
    }
  }

  private void addPawnNonCaptureMovesToTarget(MoveList list, int pawnColor, int targetSquare) {
    assert list != null;
    assert Color.isValid(pawnColor);
    assert Square.isValid(targetSquare);
    assert board.board[targetSquare] == Piece.NOPIECE;

    int delta = -moveDeltaPawn[pawnColor][0];
    int pawnPiece = Piece.valueOf(PieceType.PAWN, pawnColor);

    // Move one square backward
    int pawnSquare = targetSquare + delta;
    if (Square.isLegal(pawnSquare)) {
      int piece = board.board[pawnSquare];
      if (piece != Piece.NOPIECE) {
        if (piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnSquare, pawnColor)) {
            if ((pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R8)
              || (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R1)) {
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.QUEEN);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.ROOK);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.BISHOP);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.KNIGHT);
            } else {
              list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.NOCHESSMAN);
            }
          }
        }
      } else {
        // Move two squares backward
        pawnSquare += delta;
        if (Square.isLegal(pawnSquare)) {
          if ((pawnColor == Color.WHITE && Square.getRank(pawnSquare) == Rank.R2)
            || (pawnColor == Color.BLACK && Square.getRank(pawnSquare) == Rank.R7)) {
            assert (pawnColor == Color.WHITE && Square.getRank(pawnSquare) == Rank.R2 && Square.getRank(targetSquare) == Rank.R4)
              || (pawnColor == Color.BLACK && Square.getRank(pawnSquare) == Rank.R7 && Square.getRank(targetSquare) == Rank.R5);

            piece = board.board[pawnSquare];
            if (piece != Piece.NOPIECE && piece == pawnPiece) {
              if (!isPinned(pawnSquare, pawnColor)) {
                list.moves[list.size++] = Move.valueOf(Move.Type.PAWNDOUBLE, pawnSquare, targetSquare, piece, Piece.NOPIECE, PieceType.NOCHESSMAN);
              }
            }
          }
        }
      }
    }
  }

  private void addPawnCaptureMoves(MoveList list, int pawnSquare) {
    assert list != null;
    assert Square.isValid(pawnSquare);

    int pawnPiece = board.board[pawnSquare];
    assert Piece.isValid(pawnPiece);
    assert Piece.getChessman(pawnPiece) == PieceType.PAWN;
    int pawnColor = Piece.getColor(pawnPiece);

    for (int i = 1; i < moveDeltaPawn[pawnColor].length; ++i) {
      int delta = moveDeltaPawn[pawnColor][i];

      int targetSquare = pawnSquare + delta;
      if (Square.isLegal(targetSquare)) {
        int targetPiece = board.board[targetSquare];
        if (targetPiece != Piece.NOPIECE) {
          if (Color.opposite(Piece.getColor(targetPiece)) == pawnColor
            && Piece.getChessman(targetPiece) != PieceType.KING) {
            // Capturing move

            if ((pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R8)
              || (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R1)) {
              // Pawn promotion capturing move

              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.QUEEN);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.ROOK);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.BISHOP);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.KNIGHT);
            } else {
              // Normal capturing move

              list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.NOCHESSMAN);
            }
          }
        } else if (targetSquare == board.enPassant) {
          // En passant move
          assert (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R3)
            || (pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R6);

          int captureSquare = targetSquare + (pawnColor == Color.WHITE ? Square.deltaS : Square.deltaN);
          targetPiece = board.board[captureSquare];
          assert Piece.getChessman(targetPiece) == PieceType.PAWN;
          assert Piece.getColor(targetPiece) == Color.opposite(pawnColor);

          list.moves[list.size++] = Move.valueOf(Move.Type.ENPASSANT, pawnSquare, targetSquare, pawnPiece, targetPiece, PieceType.NOCHESSMAN);
        }
      }
    }
  }

  private void addPawnCaptureMovesToTarget(MoveList list, int pawnColor, int targetSquare) {
    assert list != null;
    assert Color.isValid(pawnColor);
    assert Square.isValid(targetSquare);
    assert Piece.getChessman(board.board[targetSquare]) != PieceType.KING;

    int targetPiece = board.board[targetSquare];
    assert Piece.isValid(targetPiece);
    assert Piece.getColor(targetPiece) == Color.opposite(pawnColor);
    int pawnPiece = Piece.valueOf(PieceType.PAWN, pawnColor);

    int enPassantDelta = (pawnColor == Color.WHITE ? Square.deltaN : Square.deltaS);
    int enPassantSquare = targetSquare + enPassantDelta;

    for (int i = 1; i < moveDeltaPawn[pawnColor].length; i++) {
      int delta = -moveDeltaPawn[pawnColor][i];

      int pawnSquare = targetSquare + delta;
      if (Square.isLegal(pawnSquare)) {
        int piece = board.board[pawnSquare];
        if (piece != Piece.NOPIECE && piece == pawnPiece) {
          // We found a valid pawn

          if (!isPinned(pawnSquare, pawnColor)) {
            if ((pawnColor == Color.WHITE && Square.getRank(targetSquare) == Rank.R8)
              || (pawnColor == Color.BLACK && Square.getRank(targetSquare) == Rank.R1)) {
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, targetPiece, PieceType.QUEEN);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, targetPiece, PieceType.ROOK);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, targetPiece, PieceType.BISHOP);
              list.moves[list.size++] = Move.valueOf(Move.Type.PAWNPROMOTION, pawnSquare, targetSquare, piece, targetPiece, PieceType.KNIGHT);
            } else {
              list.moves[list.size++] = Move.valueOf(Move.Type.NORMAL, pawnSquare, targetSquare, piece, targetPiece, PieceType.NOCHESSMAN);
            }
          }
        }
        if (enPassantSquare == board.enPassant) {
          // En passant move
          pawnSquare = pawnSquare + enPassantDelta;
          assert Square.isValid(enPassantSquare);
          assert Square.isValid(pawnSquare);

          piece = board.board[pawnSquare];
          if (piece != Piece.NOPIECE && piece == pawnPiece) {
            // We found a valid pawn which can do a en passant move

            if (!isPinned(pawnSquare, pawnColor)) {
              assert (pawnColor == Color.BLACK && Square.getRank(enPassantSquare) == Rank.R3)
                || (pawnColor == Color.WHITE && Square.getRank(enPassantSquare) == Rank.R6);
              assert Piece.getChessman(targetPiece) == PieceType.PAWN;

              list.moves[list.size++] = Move.valueOf(Move.Type.ENPASSANT, pawnSquare, enPassantSquare, piece, targetPiece, PieceType.NOCHESSMAN);
            }
          }
        }
      }
    }
  }

  private void addCastlingMoves(MoveList list, int kingSquare) {
    assert list != null;
    assert Square.isValid(kingSquare);

    int kingPiece = board.board[kingSquare];
    assert Piece.isValid(kingPiece);
    assert Piece.getChessman(kingPiece) == PieceType.KING;

    if (Piece.getColor(kingPiece) == Color.WHITE) {
      // Do not test g1 whether it is attacked as we will test it in isLegal()
      if (board.castling[Color.WHITE][Castling.KINGSIDE] != File.NOFILE
        && board.board[Square.f1] == Piece.NOPIECE
        && board.board[Square.g1] == Piece.NOPIECE
        && !isAttacked(Square.f1, Color.BLACK)) {
        assert board.board[Square.e1] == Piece.WHITEKING;
        assert board.board[Square.h1] == Piece.WHITEROOK;

        list.moves[list.size++] = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.g1, kingPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
      }
      // Do not test c1 whether it is attacked as we will test it in isLegal()
      if (board.castling[Color.WHITE][Castling.QUEENSIDE] != File.NOFILE
        && board.board[Square.b1] == Piece.NOPIECE
        && board.board[Square.c1] == Piece.NOPIECE
        && board.board[Square.d1] == Piece.NOPIECE
        && !isAttacked(Square.d1, Color.BLACK)) {
        assert board.board[Square.e1] == Piece.WHITEKING;
        assert board.board[Square.a1] == Piece.WHITEROOK;

        list.moves[list.size++] = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.c1, kingPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
      }
    } else {
      // Do not test g8 whether it is attacked as we will test it in isLegal()
      if (board.castling[Color.BLACK][Castling.KINGSIDE] != File.NOFILE
        && board.board[Square.f8] == Piece.NOPIECE
        && board.board[Square.g8] == Piece.NOPIECE
        && !isAttacked(Square.f8, Color.WHITE)) {
        assert board.board[Square.e8] == Piece.BLACKKING;
        assert board.board[Square.h8] == Piece.BLACKROOK;

        list.moves[list.size++] = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.g8, kingPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
      }
      // Do not test c8 whether it is attacked as we will test it in isLegal()
      if (board.castling[Color.BLACK][Castling.QUEENSIDE] != File.NOFILE
        && board.board[Square.b8] == Piece.NOPIECE
        && board.board[Square.c8] == Piece.NOPIECE
        && board.board[Square.d8] == Piece.NOPIECE
        && !isAttacked(Square.d8, Color.WHITE)) {
        assert board.board[Square.e8] == Piece.BLACKKING;
        assert board.board[Square.a8] == Piece.BLACKROOK;

        list.moves[list.size++] = Move.valueOf(Move.Type.CASTLING, kingSquare, Square.c8, kingPiece, Piece.NOPIECE, PieceType.NOCHESSMAN);
      }
    }
  }

  private boolean isLegal(int move) {
    // Slow test for en passant
    if (Move.getType(move) == Move.Type.ENPASSANT) {
      int activeColor = board.activeColor;
      board.makeMove(move);
      boolean isCheck = isAttacked(Square.toX88Square(Long.numberOfTrailingZeros(board.kings[activeColor])), Color.opposite(activeColor));
      board.undoMove(move);

      return !isCheck;
    }

    int originColor = Piece.getColor(Move.getOriginPiece(move));

    // Special test for king
    if (Piece.getChessman(Move.getOriginPiece(move)) == PieceType.KING) {
      return !isAttacked(Move.getTargetSquare(move), Color.opposite(originColor));
    }

    assert Long.bitCount(board.kings[originColor]) == 1;
    if (isPinned(Move.getOriginSquare(move), originColor)) {
      // We are pinned. Test if we move on the line.
      int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[originColor]));
      int attackDeltaOrigin = Attack.deltas[Attack.index(kingSquare, Move.getOriginSquare(move))];
      int attackDeltaTarget = Attack.deltas[Attack.index(kingSquare, Move.getTargetSquare(move))];
      return attackDeltaOrigin == attackDeltaTarget;
    }

    return true;
  }

  private boolean isPinned(int originSquare, int kingColor) {
    assert Square.isValid(originSquare);
    assert Color.isValid(kingColor);

    int kingSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[kingColor]));

    // We can only be pinned on an attack line
    int attackVector = Attack.vector[Attack.index(kingSquare, originSquare)];
    if (attackVector == Attack.N || attackVector == Attack.K) {
      // No line
      return false;
    }

    int delta = Attack.deltas[Attack.index(kingSquare, originSquare)];

    // Walk towards the king
    int square = originSquare + delta;
    assert Square.isValid(square);
    while (board.board[square] == Piece.NOPIECE) {
      square += delta;
      assert Square.isValid(square);
    }
    if (square != kingSquare) {
      // There's a blocker between me and the king
      return false;
    }

    // Walk away from the king
    square = originSquare - delta;
    while (Square.isLegal(square)) {
      int attacker = board.board[square];
      if (attacker != Piece.NOPIECE) {
        int attackerColor = Piece.getColor(attacker);

        return kingColor != attackerColor && canSliderPseudoAttack(attacker, square, kingSquare);
      } else {
        square -= delta;
      }
    }

    return false;
  }

  private boolean isAttacked(int targetSquare, int attackerColor) {
    assert Square.isValid(targetSquare);
    assert Color.isValid(attackerColor);

    return getAttack(new Attack(), targetSquare, attackerColor, true);
  }

  private boolean getAttack(Attack attack, int targetSquare, int attackerColor, boolean stop) {
    assert attack != null;
    assert Square.isValid(targetSquare);
    assert Color.isValid(attackerColor);

    attack.count = 0;

    // Pawn attacks
    int pawnPiece = Piece.valueOf(PieceType.PAWN, attackerColor);
    int pawnAttackerSquare = targetSquare - moveDeltaPawn[attackerColor][2];
    if (Square.isLegal(pawnAttackerSquare)) {
      int pawn = board.board[pawnAttackerSquare];
      if (pawn != Piece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[Attack.index(targetSquare, pawnAttackerSquare)] == moveDeltaPawn[attackerColor][2];
        attack.square[attack.count] = pawnAttackerSquare;
        attack.delta[attack.count] = moveDeltaPawn[attackerColor][2];
        ++attack.count;
      }
    }
    pawnAttackerSquare = targetSquare - moveDeltaPawn[attackerColor][1];
    if (Square.isLegal(pawnAttackerSquare)) {
      int pawn = board.board[pawnAttackerSquare];
      if (pawn != Piece.NOPIECE && pawn == pawnPiece) {
        if (stop) {
          return true;
        }
        assert Attack.deltas[Attack.index(targetSquare, pawnAttackerSquare)] == moveDeltaPawn[attackerColor][1];
        attack.square[attack.count] = pawnAttackerSquare;
        attack.delta[attack.count] = moveDeltaPawn[attackerColor][1];
        ++attack.count;
      }
    }
    for (long squares = board.knights[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert Piece.getChessman(board.board[attackerSquare]) == PieceType.KNIGHT;
      assert attackerSquare != Square.NOSQUARE;
      assert Piece.isValid(board.board[attackerSquare]);
      assert attackerColor == Piece.getColor(board.board[attackerSquare]);
      if (canAttack(PieceType.KNIGHT, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.bishops[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert Piece.getChessman(board.board[attackerSquare]) == PieceType.BISHOP;
      assert attackerSquare != Square.NOSQUARE;
      assert Piece.isValid(board.board[attackerSquare]);
      assert attackerColor == Piece.getColor(board.board[attackerSquare]);
      if (canAttack(PieceType.BISHOP, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.rooks[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert Piece.getChessman(board.board[attackerSquare]) == PieceType.ROOK;
      assert attackerSquare != Square.NOSQUARE;
      assert Piece.isValid(board.board[attackerSquare]);
      assert attackerColor == Piece.getColor(board.board[attackerSquare]);
      if (canAttack(PieceType.ROOK, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        ++attack.count;
      }
    }
    for (long squares = board.queens[attackerColor]; squares != 0; squares &= squares - 1) {
      int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(squares));
      assert Piece.getChessman(board.board[attackerSquare]) == PieceType.QUEEN;
      assert attackerSquare != Square.NOSQUARE;
      assert Piece.isValid(board.board[attackerSquare]);
      assert attackerColor == Piece.getColor(board.board[attackerSquare]);
      if (canAttack(PieceType.QUEEN, attackerColor, attackerSquare, targetSquare)) {
        if (stop) {
          return true;
        }
        int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];
        assert attackDelta != 0;
        attack.square[attack.count] = attackerSquare;
        attack.delta[attack.count] = attackDelta;
        attack.count++;
      }
    }
    assert Long.bitCount(board.kings[attackerColor]) == 1;
    int attackerSquare = Square.toX88Square(Long.numberOfTrailingZeros(board.kings[attackerColor]));
    assert Piece.getChessman(board.board[attackerSquare]) == PieceType.KING;
    assert attackerSquare != Square.NOSQUARE;
    assert Piece.isValid(board.board[attackerSquare]);
    assert attackerColor == Piece.getColor(board.board[attackerSquare]);
    if (canAttack(PieceType.KING, attackerColor, attackerSquare, targetSquare)) {
      if (stop) {
        return true;
      }
      int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];
      assert attackDelta != 0;
      attack.square[attack.count] = attackerSquare;
      attack.delta[attack.count] = attackDelta;
      ++attack.count;
    }

    return false;
  }

  private boolean canAttack(int attackerChessman, int attackerColor, int attackerSquare, int targetSquare) {
    assert PieceType.isValid(attackerChessman);
    assert Color.isValid(attackerColor);
    assert Square.isValid(attackerSquare);
    assert Square.isValid(targetSquare);

    int attackVector = Attack.vector[Attack.index(targetSquare, attackerSquare)];

    switch (attackerChessman) {
      case PieceType.PAWN:
        if (attackVector == Attack.u && attackerColor == Color.WHITE) {
          return true;
        } else if (attackVector == Attack.d && attackerColor == Color.BLACK) {
          return true;
        }
        break;
      case PieceType.KNIGHT:
        if (attackVector == Attack.K) {
          return true;
        }
        break;
      case PieceType.BISHOP:
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
      case PieceType.ROOK:
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
      case PieceType.QUEEN:
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
      case PieceType.KING:
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
    assert Square.isValid(attackerSquare);
    assert Square.isValid(targetSquare);

    int attackDelta = Attack.deltas[Attack.index(targetSquare, attackerSquare)];

    int square = attackerSquare + attackDelta;
    while (Square.isLegal(square) && square != targetSquare && board.board[square] == Piece.NOPIECE) {
      square += attackDelta;
    }

    return square == targetSquare;
  }

  private boolean canSliderPseudoAttack(int attacker, int attackerSquare, int targetSquare) {
    assert Piece.isValid(attacker);
    assert Square.isValid(attackerSquare);
    assert Square.isValid(targetSquare);

    int attackVector;

    switch (Piece.getChessman(attacker)) {
      case PieceType.PAWN:
        break;
      case PieceType.KNIGHT:
        break;
      case PieceType.BISHOP:
        attackVector = Attack.vector[Attack.index(targetSquare, attackerSquare)];
        switch (attackVector) {
          case Attack.u:
          case Attack.d:
          case Attack.D:
            return true;
          default:
            break;
        }
        break;
      case PieceType.ROOK:
        attackVector = Attack.vector[Attack.index(targetSquare, attackerSquare)];
        switch (attackVector) {
          case Attack.s:
          case Attack.S:
            return true;
          default:
            break;
        }
        break;
      case PieceType.QUEEN:
        attackVector = Attack.vector[Attack.index(targetSquare, attackerSquare)];
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
      case PieceType.KING:
        break;
      default:
        assert false : Piece.getChessman(attacker);
        break;
    }

    return false;
  }

}
