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

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.fluxchess.test.AssertUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.assertEquals;

public class MoveTest {

	@Test
	public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		assertUtilityClassWellDefined(Move.class);
	}

	@Test
	public void testCreation() {
		int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.a7, Square.b8, Piece.WHITEPAWN, Piece.BLACKQUEEN, PieceType.KNIGHT);

		assertEquals(Move.Type.PAWNPROMOTION, Move.getType(move));
		assertEquals(Square.a7, Move.getOriginSquare(move));
		assertEquals(Square.b8, Move.getTargetSquare(move));
		assertEquals(Piece.WHITEPAWN, Move.getOriginPiece(move));
		assertEquals(Piece.BLACKQUEEN, Move.getTargetPiece(move));
		assertEquals(PieceType.KNIGHT, Move.getPromotion(move));
	}

	@Test
	public void testPromotion() {
		int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.b7, Square.c8, Piece.WHITEPAWN, Piece.BLACKQUEEN, PieceType.KNIGHT);

		assertEquals(PieceType.KNIGHT, Move.getPromotion(move));
	}

}
