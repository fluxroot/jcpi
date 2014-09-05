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

import com.fluxchess.jcpi.models.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

  @Test
  public void testConstructor() {
    // Setup a new board
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Test pieces setup
    for (GenericPosition genericPosition : GenericPosition.values()) {
      GenericPiece genericPiece = genericBoard.getPiece(genericPosition);
      int piece = board.board[Square.valueOf(genericPosition)];
      if (genericPiece == null) {
        assertEquals(IntPiece.NOPIECE, piece);
      } else {
        assertEquals(genericPiece, IntPiece.toGenericPiece(piece));
      }
    }

    // Test castling
    for (GenericColor genericColor : GenericColor.values()) {
      for (GenericCastling genericCastling : GenericCastling.values()) {
        GenericFile genericFile = genericBoard.getCastling(genericColor, genericCastling);
        int file = board.castling[Color.valueOf(genericColor)][Castling.valueOf(genericCastling)];
        if (genericFile == null) {
          assertEquals(IntFile.NOFILE, file);
        } else {
          assertEquals(genericFile, IntFile.toGenericFile(file));
        }
      }
    }

    // Test en passant
    if (genericBoard.getEnPassant() == null) {
      assertEquals(Square.NOSQUARE, board.enPassant);
    } else {
      assertEquals(genericBoard.getEnPassant(), Square.toGenericPosition(board.enPassant));
    }

    // Test active color
    assertEquals(genericBoard.getActiveColor(), Color.toGenericColor(board.activeColor));

    // Test half move clock
    assertEquals(genericBoard.getHalfMoveClock(), board.halfMoveClock);

    // Test full move number
    assertEquals(genericBoard.getFullMoveNumber(), board.getFullMoveNumber());
  }

  @Test
  public void testToGenericBoard() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testToString() throws IllegalNotationException {
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    GenericBoard genericBoard = new GenericBoard(fen);
    Board board = new Board(genericBoard);

    assertEquals(fen, board.toString());
  }

  @Test
  public void testActiveColor() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(Color.BLACK, board.activeColor);

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(Color.WHITE, board.activeColor);
  }

  @Test
  public void testHalfMoveClock() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(0, board.halfMoveClock);

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);

    // Move white knight
    move = Move.valueOf(Move.Type.NORMAL, Square.b1, Square.c3, IntPiece.WHITEKNIGHT, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(1, board.halfMoveClock);
  }

  @Test
  public void testFullMoveNumber() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(1, board.getFullMoveNumber());

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(2, board.getFullMoveNumber());
  }

  @Test
  public void testNormalMove() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);
    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testPawnDoubleMove() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.PAWNDOUBLE, Square.a2, Square.a4, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(Square.a3, board.enPassant);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testPawnPromotionMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("8/P5k1/8/8/2K5/8/8/8 w - - 0 1");
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.a7, Square.a8, IntPiece.WHITEPAWN, IntPiece.NOPIECE, PieceType.QUEEN);
    board.makeMove(move);

    assertEquals(IntPiece.WHITEQUEEN, board.board[Square.a8]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testEnPassantMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("5k2/8/8/8/3Pp3/8/8/3K4 b - d3 0 1");
    Board board = new Board(genericBoard);

    // Make en passant move
    int move = Move.valueOf(Move.Type.ENPASSANT, Square.e4, Square.d3, IntPiece.BLACKPAWN, IntPiece.WHITEPAWN, PieceType.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntPiece.NOPIECE, board.board[Square.d4]);
    assertEquals(IntPiece.BLACKPAWN, board.board[Square.d3]);
    assertEquals(Square.NOSQUARE, board.enPassant);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testCastlingMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.CASTLING, Square.e1, Square.c1, IntPiece.WHITEKING, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntFile.NOFILE, board.castling[Color.WHITE][Castling.QUEENSIDE]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());

    genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    board = new Board(genericBoard);

    move = Move.valueOf(Move.Type.CASTLING, Square.e1, Square.g1, IntPiece.WHITEKING, IntPiece.NOPIECE, PieceType.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntFile.NOFILE, board.castling[Color.WHITE][Castling.KINGSIDE]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

}
