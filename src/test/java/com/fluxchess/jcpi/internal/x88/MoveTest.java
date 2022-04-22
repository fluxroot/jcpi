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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveTest {

	@Test
	public void testCreation() {
		int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.a7, Square.b8, Piece.WHITEPAWN, Piece.BLACKQUEEN, PieceType.KNIGHT);

		assertThat(Move.getType(move)).isEqualTo(Move.Type.PAWNPROMOTION);
		assertThat(Move.getOriginSquare(move)).isEqualTo(Square.a7);
		assertThat(Move.getTargetSquare(move)).isEqualTo(Square.b8);
		assertThat(Move.getOriginPiece(move)).isEqualTo(Piece.WHITEPAWN);
		assertThat(Move.getTargetPiece(move)).isEqualTo(Piece.BLACKQUEEN);
		assertThat(Move.getPromotion(move)).isEqualTo(PieceType.KNIGHT);
	}

	@Test
	public void testPromotion() {
		int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.b7, Square.c8, Piece.WHITEPAWN, Piece.BLACKQUEEN, PieceType.KNIGHT);

		assertThat(Move.getPromotion(move)).isEqualTo(PieceType.KNIGHT);
	}

}
