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

public enum GenericRank {

	_1('1'),
	_2('2'),
	_3('3'),
	_4('4'),
	_5('5'),
	_6('6'),
	_7('7'),
	_8('8');

	private final char token;

	private GenericRank(char token) {
		this.token = token;
	}

	/**
	 * Tries to convert the specified token to a {@link GenericRank}.
	 *
	 * @param token a character to convert to a {@link GenericRank}
	 * @return an {@link Optional} containing the {@link GenericRank} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericRank> of(char token) {
		for (GenericRank rank : values()) {
			if (token == rank.token) {
				return Optional.of(rank);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns the previous {@link GenericRank} if it exists.
	 *
	 * @return the previous {@link GenericRank} if it exists, {@link Optional#empty()} otherwise
	 */
	public Optional<GenericRank> prev() {
		return prev(1);
	}

	/**
	 * Return a previous {@link GenericRank} if it exists by skipping i number of ranks.
	 *
	 * @param i number of ranks to skip. Must be greater than zero.
	 * @return a previous {@link GenericRank} if it exists by skipping i number of ranks,
	 * {@link Optional#empty()} otherwise
	 */
	public Optional<GenericRank> prev(int i) {
		if (i < 1) return Optional.empty();

		int position = ordinal() - i;
		if (position >= 0) {
			return Optional.of(values()[position]);
		}
		return Optional.empty();
	}

	/**
	 * Returns the next {@link GenericRank} if it exists.
	 *
	 * @return the next {@link GenericRank} if it exists, {@link Optional#empty()} otherwise
	 */
	public Optional<GenericRank> next() {
		return next(1);
	}

	/**
	 * Returns a next {@link GenericRank} if it exists by skipping i number of ranks.
	 *
	 * @param i number of ranks to skip. Must be greater than zero.
	 * @return a next {@link GenericRank} if it exists by skipping i number of ranks,
	 * {@link Optional#empty()} otherwise
	 */
	public Optional<GenericRank> next(int i) {
		if (i < 1) return Optional.empty();

		int position = ordinal() + i;
		if (position < values().length) {
			return Optional.of(values()[position]);
		}
		return Optional.empty();
	}

	/**
	 * Returns the character representing this {@link GenericRank}.
	 *
	 * @return the character representing this {@link GenericRank}
	 */
	public char toChar() {
		return token;
	}

	/**
	 * Returns the character representing this {@link GenericRank} as string.
	 *
	 * @return the character representing this {@link GenericRank} as string
	 */
	@Override
	public String toString() {
		return Character.toString(token);
	}

}
