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

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public enum GenericChessman {

	PAWN('P'),
	KNIGHT('N'),
	BISHOP('B'),
	ROOK('R'),
	QUEEN('Q'),
	KING('K');

	private static final Set<GenericChessman> promotions = EnumSet.of(KNIGHT, BISHOP, ROOK, QUEEN);

	private final char token;

	private GenericChessman(char token) {
		this.token = token;
	}

	/**
	 * Tries to convert the specified token to a {@link GenericChessman}. The token can be lowercase or uppercase.
	 *
	 * @param token a character to convert to a {@link GenericChessman}
	 * @return an {@link Optional} containing the {@link GenericChessman} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericChessman> from(char token) {
		for (GenericChessman chessman : values()) {
			if (Character.toUpperCase(token) == chessman.token) {
				return Optional.of(chessman);
			}
		}
		return Optional.empty();
	}

	/**
	 * Tries to convert the specified token to a valid promoted {@link GenericChessman}. The token can be lowercase
	 * or uppercase.
	 *
	 * @param token a character to convert to a valid promoted {@link GenericChessman}
	 * @return an {@link Optional} containing the valid promoted {@link GenericChessman} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericChessman> promotionFrom(char token) {
		for (GenericChessman chessman : promotions) {
			if (Character.toUpperCase(token) == chessman.token) {
				return Optional.of(chessman);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns whether this {@link GenericChessman} is a valid promoted {@link GenericChessman}.
	 *
	 * @return true if this {@link GenericChessman} is a valid promoted {@link GenericChessman}, false otherwise
	 */
	public boolean isPromotion() {
		return promotions.contains(this);
	}

	/**
	 * Returns the algebraic character representing this {@link GenericChessman}. Please note,
	 * for {@link GenericChessman#PAWN} there exists no algebraic character and {@link Optional#empty()} is returned
	 * instead.
	 *
	 * @return an {@link Optional} containing the algebraic character representing this {@link GenericChessman},
	 * {@link Optional#empty()} if this {@link GenericChessman} is a {@link GenericChessman#PAWN}.
	 */
	public Optional<Character> toAlgebraicChar() {
		if (this == PAWN) {
			return Optional.empty();
		}
		return Optional.of(token);
	}

	/**
	 * Returns the notation representing this {@link GenericChessman} using the specified {@link GenericColor}
	 * for transformation.
	 *
	 * @param color a {@link GenericColor} for transformation
	 * @return an uppercase string if the specified {@link GenericColor} is {@link GenericColor#WHITE}, a lowercase
	 * string if the specified {@link GenericColor} is {@link GenericColor#BLACK}
	 */
	public String toNotation(GenericColor color) {
		if (color == null) throw new IllegalArgumentException();

		return String.valueOf(color.transform(token));
	}

}
