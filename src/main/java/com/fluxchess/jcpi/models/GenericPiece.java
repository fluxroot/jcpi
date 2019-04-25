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

	private static final Map<GenericColor, Map<GenericChessman, GenericPiece>> allPieces = new EnumMap<GenericColor, Map<GenericChessman, GenericPiece>>(GenericColor.class);

	static {
		for (GenericColor color : GenericColor.values()) {
			allPieces.put(color, new EnumMap<GenericChessman, GenericPiece>(GenericChessman.class));
		}
		for (GenericPiece piece : values()) {
			allPieces.get(piece.color).put(piece.chessman, piece);
		}
	}

	public final GenericColor color;
	public final GenericChessman chessman;

	private GenericPiece(GenericColor color, GenericChessman chessman) {
		this.color = color;
		this.chessman = chessman;
	}

	public static GenericPiece valueOf(GenericColor color, GenericChessman chessman) {
		if (color == null) throw new IllegalArgumentException();
		if (chessman == null) throw new IllegalArgumentException();

		return allPieces.get(color).get(chessman);
	}

	public static GenericPiece valueOf(char token) {
		GenericColor color = GenericColor.colorOf(token);
		GenericChessman chessman = GenericChessman.valueOf(token);

		return valueOf(color, chessman);
	}

	public static boolean isValid(char token) {
		return GenericChessman.isValid(token);
	}

	public char toChar() {
		return this.chessman.toChar(this.color);
	}

}
