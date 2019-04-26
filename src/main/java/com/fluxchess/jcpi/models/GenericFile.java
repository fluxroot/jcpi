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

public enum GenericFile {

	a('a'),
	b('b'),
	c('c'),
	d('d'),
	e('e'),
	f('f'),
	g('g'),
	h('h');

	private final char token;

	private GenericFile(char token) {
		this.token = token;
	}

	/**
	 * Tries to convert the specified token to a {@link GenericFile}. The token can be lowercase or uppercase.
	 *
	 * @param token a character to convert to a {@link GenericFile}
	 * @return an {@link Optional} containing the {@link GenericFile} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericFile> of(char token) {
		for (GenericFile file : values()) {
			if (Character.toLowerCase(token) == file.token) {
				return Optional.of(file);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns the previous {@link GenericFile} if it exists.
	 *
	 * @return the previous {@link GenericFile} if it exists, {@link Optional#empty()} otherwise
	 */
	public Optional<GenericFile> prev() {
		return prev(1);
	}

	/**
	 * Return a previous {@link GenericFile} if it exists by skipping i number of files.
	 *
	 * @param i number of files to skip. Must be greater than zero.
	 * @return a previous {@link GenericFile} if it exists by skipping i number of files,
	 * {@link Optional#empty()} otherwise
	 */
	public Optional<GenericFile> prev(int i) {
		if (i < 1) return Optional.empty();

		int position = ordinal() - i;
		if (position >= 0) {
			return Optional.of(values()[position]);
		}
		return Optional.empty();
	}

	/**
	 * Returns the next {@link GenericFile} if it exists.
	 *
	 * @return the next {@link GenericFile} if it exists, {@link Optional#empty()} otherwise
	 */
	public Optional<GenericFile> next() {
		return next(1);
	}

	/**
	 * Returns a next {@link GenericFile} if it exists by skipping i number of files.
	 *
	 * @param i number of files to skip. Must be greater than zero.
	 * @return a next {@link GenericFile} if it exists by skipping i number of files,
	 * {@link Optional#empty()} otherwise
	 */
	public Optional<GenericFile> next(int i) {
		if (i < 1) return Optional.empty();

		int position = ordinal() + i;
		if (position < values().length) {
			return Optional.of(values()[position]);
		}
		return Optional.empty();
	}

	/**
	 * Returns the character representing this {@link GenericFile}.
	 *
	 * @return the character representing this {@link GenericFile}
	 */
	public char toChar() {
		return token;
	}

	/**
	 * Returns the character representing this {@link GenericFile} as string.
	 *
	 * @return the character representing this {@link GenericFile} as string
	 */
	@Override
	public String toString() {
		return Character.toString(token);
	}

}
