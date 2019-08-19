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

import java.util.Optional;

public enum GenericColor {

	WHITE('w'),
	BLACK('b');

	private final char token;

	private GenericColor(char token) {
		this.token = token;
	}

	/**
	 * Tries to convert the specified token to a {@link GenericColor}. The token can be lowercase or uppercase.
	 *
	 * @param token a character to convert to a {@link GenericColor}
	 * @return an {@link Optional} containing the {@link GenericColor} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericColor> from(char token) {
		for (GenericColor color : values()) {
			if (Character.toLowerCase(token) == color.token) {
				return Optional.of(color);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns {@link GenericColor#WHITE} if the specified character c is uppercase,
	 * {@link GenericColor#BLACK} if c is lowercase.
	 *
	 * @param c a character to get the {@link GenericColor} from
	 * @return {@link GenericColor#WHITE} if c is uppercase, {@link GenericColor#BLACK} if c is lowercase
	 */
	public static GenericColor colorOf(char c) {
		return Character.isUpperCase(c) ? WHITE : BLACK;
	}

	char transform(char token) {
		return this == WHITE ? Character.toUpperCase(token) : Character.toLowerCase(token);
	}

	/**
	 * Returns the opposite color of this {@link GenericColor}.
	 *
	 * @return the opposite color of this {@link GenericColor}
	 */
	public GenericColor opposite() {
		return this == WHITE ? BLACK : WHITE;
	}

	/**
	 * Returns the notation representing this {@link GenericColor}.
	 *
	 * @return the notation representing this {@link GenericColor}
	 */
	public String toNotation() {
		return String.valueOf(token);
	}

}
