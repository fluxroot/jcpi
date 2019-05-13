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
package com.fluxchess.jcpi.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static com.fluxchess.jcpi.models.GenericChessman.*;
import static com.fluxchess.jcpi.models.GenericColor.BLACK;
import static com.fluxchess.jcpi.models.GenericColor.WHITE;

public enum GenericPiece {

	WHITEPAWN(WHITE, PAWN),
	WHITEKNIGHT(WHITE, KNIGHT),
	WHITEBISHOP(WHITE, BISHOP),
	WHITEROOK(WHITE, ROOK),
	WHITEQUEEN(WHITE, QUEEN),
	WHITEKING(WHITE, KING),
	BLACKPAWN(BLACK, PAWN),
	BLACKKNIGHT(BLACK, KNIGHT),
	BLACKBISHOP(BLACK, BISHOP),
	BLACKROOK(BLACK, ROOK),
	BLACKQUEEN(BLACK, QUEEN),
	BLACKKING(BLACK, KING);

	private static final Map<GenericColor, Map<GenericChessman, GenericPiece>> pieces = new EnumMap<>(GenericColor.class);

	static {
		for (GenericColor color : GenericColor.values()) {
			pieces.put(color, new EnumMap<>(GenericChessman.class));
		}
		for (GenericPiece piece : values()) {
			pieces.get(piece.color).put(piece.chessman, piece);
		}
	}

	public final GenericColor color;
	public final GenericChessman chessman;

	private GenericPiece(GenericColor color, GenericChessman chessman) {
		this.color = color;
		this.chessman = chessman;
	}

	/**
	 * Returns the {@link GenericPiece} for the specified {@link GenericColor} and {@link GenericChessman}.
	 *
	 * @param color    a {@link GenericColor}
	 * @param chessman a {@link GenericChessman}
	 * @return the {@link GenericPiece} for the specified {@link GenericColor} and {@link GenericChessman}
	 */
	public static GenericPiece of(GenericColor color, GenericChessman chessman) {
		if (color == null) throw new IllegalArgumentException();
		if (chessman == null) throw new IllegalArgumentException();

		return pieces.get(color).get(chessman);
	}

	/**
	 * Tries to convert the specified token to a {@link GenericPiece}. The token can be lowercase or uppercase.
	 *
	 * @param token a character to convert to a {@link GenericPiece}
	 * @return an {@link Optional} containing the {@link GenericPiece} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericPiece> from(char token) {
		GenericColor color = GenericColor.colorOf(token);
		return GenericChessman.from(token).map(chessman -> of(color, chessman));
	}

	/**
	 * Returns the notation representing this {@link GenericPiece}.
	 *
	 * @return the notation representing this {@link GenericPiece}
	 */
	public String toNotation() {
		return chessman.toNotation(color);
	}

}
