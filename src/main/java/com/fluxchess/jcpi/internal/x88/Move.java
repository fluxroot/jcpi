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

import com.fluxchess.jcpi.models.GenericMove;

/**
 * This class represents a move as a int value. The fields are represented by
 * the following bits.
 * <p/>
 * 0 -  2: type
 * 3 -  9: origin square
 * 10 - 16: target square
 * 17 - 21: origin piece
 * 22 - 26: target piece
 * 27 - 29: promotion chessman
 */
final class Move {

	public static final class Type {

		public static final int MASK = 0x7;

		public static final int NORMAL = 0;
		public static final int PAWNDOUBLE = 1;
		public static final int PAWNPROMOTION = 2;
		public static final int ENPASSANT = 3;
		public static final int CASTLING = 4;

		public static final int[] values = {
				NORMAL,
				PAWNDOUBLE,
				PAWNPROMOTION,
				ENPASSANT,
				CASTLING
		};

		private Type() {
		}
	}

	private static final int TYPE_SHIFT = 0;
	private static final int TYPE_MASK = Type.MASK << TYPE_SHIFT;
	private static final int ORIGINSQUARE_SHIFT = 3;
	private static final int ORIGINSQUARE_MASK = Square.MASK << ORIGINSQUARE_SHIFT;
	private static final int TARGETSQUARE_SHIFT = 10;
	private static final int TARGETSQUARE_MASK = Square.MASK << TARGETSQUARE_SHIFT;
	private static final int ORIGINPIECE_SHIFT = 17;
	private static final int ORIGINPIECE_MASK = Piece.MASK << ORIGINPIECE_SHIFT;
	private static final int TARGETPIECE_SHIFT = 22;
	private static final int TARGETPIECE_MASK = Piece.MASK << TARGETPIECE_SHIFT;
	private static final int PROMOTION_SHIFT = 27;
	private static final int PROMOTION_MASK = PieceType.MASK << PROMOTION_SHIFT;

	private Move() {
	}

	public static int valueOf(int type, int originSquare, int targetSquare, int originPiece, int targetPiece, int promotion) {
		int move = 0;

		// Encode type
		move |= type << TYPE_SHIFT;

		// Encode origin square
		move |= originSquare << ORIGINSQUARE_SHIFT;

		// Encode target square
		move |= targetSquare << TARGETSQUARE_SHIFT;

		// Encode origin piece
		move |= originPiece << ORIGINPIECE_SHIFT;

		// Encode target piece
		move |= targetPiece << TARGETPIECE_SHIFT;

		// Encode promotion
		move |= promotion << PROMOTION_SHIFT;

		return move;
	}

	public static GenericMove toGenericMove(int move) {
		int type = getType(move);
		int originSquare = getOriginSquare(move);
		int targetSquare = getTargetSquare(move);

		switch (type) {
			case Type.NORMAL:
			case Type.PAWNDOUBLE:
			case Type.ENPASSANT:
			case Type.CASTLING:
				return new GenericMove(Square.toGenericPosition(originSquare), Square.toGenericPosition(targetSquare));
			case Type.PAWNPROMOTION:
				return new GenericMove(Square.toGenericPosition(originSquare), Square.toGenericPosition(targetSquare), PieceType.toGenericChessman(getPromotion(move)));
			default:
				throw new IllegalArgumentException();
		}
	}

	public static int getType(int move) {
		return (move & TYPE_MASK) >>> TYPE_SHIFT;
	}

	public static int getOriginSquare(int move) {
		return (move & ORIGINSQUARE_MASK) >>> ORIGINSQUARE_SHIFT;
	}

	public static int getTargetSquare(int move) {
		return (move & TARGETSQUARE_MASK) >>> TARGETSQUARE_SHIFT;
	}

	public static int getOriginPiece(int move) {
		return (move & ORIGINPIECE_MASK) >>> ORIGINPIECE_SHIFT;
	}

	public static int getTargetPiece(int move) {
		return (move & TARGETPIECE_MASK) >>> TARGETPIECE_SHIFT;
	}

	public static int getPromotion(int move) {
		return (move & PROMOTION_MASK) >>> PROMOTION_SHIFT;
	}

}
