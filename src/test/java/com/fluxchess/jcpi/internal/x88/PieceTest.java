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

import com.fluxchess.jcpi.models.GenericPiece;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PieceTest {

	@Test
	public void testValues() {
		for (GenericPiece genericPiece : GenericPiece.values()) {
			assertThat(Piece.toGenericPiece(Piece.valueOf(genericPiece))).isEqualTo(genericPiece);
			assertThat(Piece.toGenericPiece(Piece.valueOf(PieceType.valueOf(genericPiece.chessman), Color.valueOf(genericPiece.color)))).isEqualTo(genericPiece);
		}
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> Piece.valueOf(null));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testInvalidToGenericPiece() {
		Throwable thrown = catchThrowable(() -> Piece.toGenericPiece(Piece.NOPIECE));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testOrdinal() {
		for (int piece : Piece.values) {
			assertThat(Piece.toGenericPiece(piece).ordinal()).isEqualTo(Piece.ordinal(piece));
			assertThat(Piece.values[Piece.ordinal(piece)]).isEqualTo(piece);
		}
	}

	@Test
	public void testInvalidOrdinal() {
		Throwable thrown = catchThrowable(() -> Piece.ordinal(Piece.NOPIECE));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		for (int piece : Piece.values) {
			assertThat(Piece.isValid(piece)).isTrue();
			assertThat(piece & Piece.MASK).isEqualTo(piece);
		}

		assertThat(Piece.isValid(Piece.NOPIECE)).isFalse();
	}

	@Test
	public void testInvalidIsValid() {
		Throwable thrown = catchThrowable(() -> Piece.isValid(-1));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testGetChessman() {
		assertThat(Piece.getChessman(Piece.WHITEPAWN)).isEqualTo(PieceType.PAWN);
		assertThat(Piece.getChessman(Piece.BLACKPAWN)).isEqualTo(PieceType.PAWN);
		assertThat(Piece.getChessman(Piece.WHITEKNIGHT)).isEqualTo(PieceType.KNIGHT);
		assertThat(Piece.getChessman(Piece.BLACKKNIGHT)).isEqualTo(PieceType.KNIGHT);
		assertThat(Piece.getChessman(Piece.WHITEBISHOP)).isEqualTo(PieceType.BISHOP);
		assertThat(Piece.getChessman(Piece.BLACKBISHOP)).isEqualTo(PieceType.BISHOP);
		assertThat(Piece.getChessman(Piece.WHITEROOK)).isEqualTo(PieceType.ROOK);
		assertThat(Piece.getChessman(Piece.BLACKROOK)).isEqualTo(PieceType.ROOK);
		assertThat(Piece.getChessman(Piece.WHITEQUEEN)).isEqualTo(PieceType.QUEEN);
		assertThat(Piece.getChessman(Piece.BLACKQUEEN)).isEqualTo(PieceType.QUEEN);
		assertThat(Piece.getChessman(Piece.WHITEKING)).isEqualTo(PieceType.KING);
		assertThat(Piece.getChessman(Piece.BLACKKING)).isEqualTo(PieceType.KING);
	}

	@Test
	public void testInvalidGetChessman() {
		Throwable thrown = catchThrowable(() -> Piece.getChessman(Piece.NOPIECE));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testGetColor() {
		assertThat(Piece.getColor(Piece.WHITEPAWN)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKPAWN)).isEqualTo(Color.BLACK);
		assertThat(Piece.getColor(Piece.WHITEKNIGHT)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKKNIGHT)).isEqualTo(Color.BLACK);
		assertThat(Piece.getColor(Piece.WHITEBISHOP)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKBISHOP)).isEqualTo(Color.BLACK);
		assertThat(Piece.getColor(Piece.WHITEROOK)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKROOK)).isEqualTo(Color.BLACK);
		assertThat(Piece.getColor(Piece.WHITEQUEEN)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKQUEEN)).isEqualTo(Color.BLACK);
		assertThat(Piece.getColor(Piece.WHITEKING)).isEqualTo(Color.WHITE);
		assertThat(Piece.getColor(Piece.BLACKKING)).isEqualTo(Color.BLACK);
	}

	@Test
	public void testInvalidGetColor() {
		Throwable thrown = catchThrowable(() -> Piece.getColor(Piece.NOPIECE));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
