/*
 * Copyright 2007-2022 The Java Chess Protocol Interface Project Authors
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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

	@Test
	void testConstructor() {
		// Setup a new board
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		// Test pieces setup
		for (GenericPosition genericPosition : GenericPosition.values()) {
			GenericPiece genericPiece = genericBoard.getPiece(genericPosition);
			int piece = board.board[Square.valueOf(genericPosition)];
			if (genericPiece == null) {
				assertThat(piece).isEqualTo(Piece.NOPIECE);
			} else {
				assertThat(Piece.toGenericPiece(piece)).isEqualTo(genericPiece);
			}
		}

		// Test castling
		for (GenericColor genericColor : GenericColor.values()) {
			for (GenericCastling genericCastling : GenericCastling.values()) {
				GenericFile genericFile = genericBoard.getCastling(genericColor, genericCastling);
				int file = board.castling[Color.valueOf(genericColor)][Castling.valueOf(genericCastling)];
				if (genericFile == null) {
					assertThat(file).isEqualTo(File.NOFILE);
				} else {
					assertThat(File.toGenericFile(file)).isEqualTo(genericFile);
				}
			}
		}

		// Test en passant
		if (genericBoard.getEnPassant() == null) {
			assertThat(board.enPassant).isEqualTo(Square.NOSQUARE);
		} else {
			assertThat(Square.toGenericPosition(board.enPassant)).isEqualTo(genericBoard.getEnPassant());
		}

		// Test active color
		assertThat(Color.toGenericColor(board.activeColor)).isEqualTo(genericBoard.getActiveColor());

		// Test half move clock
		assertThat(board.halfMoveClock).isEqualTo(genericBoard.getHalfMoveClock());

		// Test full move number
		assertThat(board.getFullMoveNumber()).isEqualTo(genericBoard.getFullMoveNumber());
	}

	@Test
	void testToGenericBoard() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

	@Test
	void testToString() throws IllegalNotationException {
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		GenericBoard genericBoard = new GenericBoard(fen);
		Board board = new Board(genericBoard);

		assertThat(board.toString()).isEqualTo(fen);
	}

	@Test
	void testActiveColor() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		// Move white pawn
		int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.activeColor).isEqualTo(Color.BLACK);

		// Move black pawn
		move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, Piece.BLACKPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.activeColor).isEqualTo(Color.WHITE);
	}

	@Test
	void testHalfMoveClock() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		// Move white pawn
		int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.halfMoveClock).isZero();

		// Move black pawn
		move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, Piece.BLACKPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);

		// Move white knight
		move = Move.valueOf(Move.Type.NORMAL, Square.b1, Square.c3, Piece.WHITEKNIGHT, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.halfMoveClock).isOne();
	}

	@Test
	void testFullMoveNumber() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		// Move white pawn
		int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.getFullMoveNumber()).isOne();

		// Move black pawn
		move = Move.valueOf(Move.Type.NORMAL, Square.b7, Square.b6, Piece.BLACKPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		assertThat(board.getFullMoveNumber()).isEqualTo(2);
	}

	@Test
	void testNormalMove() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		int move = Move.valueOf(Move.Type.NORMAL, Square.a2, Square.a3, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);
		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

	@Test
	void testPawnDoubleMove() {
		GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
		Board board = new Board(genericBoard);

		int move = Move.valueOf(Move.Type.PAWNDOUBLE, Square.a2, Square.a4, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);

		assertThat(board.enPassant).isEqualTo(Square.a3);

		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

	@Test
	void testPawnPromotionMove() throws IllegalNotationException {
		GenericBoard genericBoard = new GenericBoard("8/P5k1/8/8/2K5/8/8/8 w - - 0 1");
		Board board = new Board(genericBoard);

		int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.a7, Square.a8, Piece.WHITEPAWN, Piece.NOPIECE, PieceType.QUEEN);
		board.makeMove(move);

		assertThat(board.board[Square.a8]).isEqualTo(Piece.WHITEQUEEN);

		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

	@Test
	void testEnPassantMove() throws IllegalNotationException {
		GenericBoard genericBoard = new GenericBoard("5k2/8/8/8/3Pp3/8/8/3K4 b - d3 0 1");
		Board board = new Board(genericBoard);

		// Make en passant move
		int move = Move.valueOf(Move.Type.ENPASSANT, Square.e4, Square.d3, Piece.BLACKPAWN, Piece.WHITEPAWN, PieceType.NOCHESSMAN);
		board.makeMove(move);

		assertThat(board.board[Square.d4]).isEqualTo(Piece.NOPIECE);
		assertThat(board.board[Square.d3]).isEqualTo(Piece.BLACKPAWN);
		assertThat(board.enPassant).isEqualTo(Square.NOSQUARE);

		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

	@Test
	void testCastlingMove() throws IllegalNotationException {
		GenericBoard genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		Board board = new Board(genericBoard);

		int move = Move.valueOf(Move.Type.CASTLING, Square.e1, Square.c1, Piece.WHITEKING, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);

		assertThat(board.castling[Color.WHITE][Castling.QUEENSIDE]).isEqualTo(File.NOFILE);

		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);

		genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		board = new Board(genericBoard);

		move = Move.valueOf(Move.Type.CASTLING, Square.e1, Square.g1, Piece.WHITEKING, Piece.NOPIECE, PieceType.NOCHESSMAN);
		board.makeMove(move);

		assertThat(board.castling[Color.WHITE][Castling.KINGSIDE]).isEqualTo(File.NOFILE);

		board.undoMove(move);

		assertThat(board.toGenericBoard()).isEqualTo(genericBoard);
	}

}
