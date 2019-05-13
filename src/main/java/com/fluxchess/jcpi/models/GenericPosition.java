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

import static com.fluxchess.jcpi.models.GenericFile.*;
import static com.fluxchess.jcpi.models.GenericRank.*;

public enum GenericPosition {

	a1(a, _1),
	b1(b, _1),
	c1(c, _1),
	d1(d, _1),
	e1(e, _1),
	f1(f, _1),
	g1(g, _1),
	h1(h, _1),
	a2(a, _2),
	b2(b, _2),
	c2(c, _2),
	d2(d, _2),
	e2(e, _2),
	f2(f, _2),
	g2(g, _2),
	h2(h, _2),
	a3(a, _3),
	b3(b, _3),
	c3(c, _3),
	d3(d, _3),
	e3(e, _3),
	f3(f, _3),
	g3(g, _3),
	h3(h, _3),
	a4(a, _4),
	b4(b, _4),
	c4(c, _4),
	d4(d, _4),
	e4(e, _4),
	f4(f, _4),
	g4(g, _4),
	h4(h, _4),
	a5(a, _5),
	b5(b, _5),
	c5(c, _5),
	d5(d, _5),
	e5(e, _5),
	f5(f, _5),
	g5(g, _5),
	h5(h, _5),
	a6(a, _6),
	b6(b, _6),
	c6(c, _6),
	d6(d, _6),
	e6(e, _6),
	f6(f, _6),
	g6(g, _6),
	h6(h, _6),
	a7(a, _7),
	b7(b, _7),
	c7(c, _7),
	d7(d, _7),
	e7(e, _7),
	f7(f, _7),
	g7(g, _7),
	h7(h, _7),
	a8(a, _8),
	b8(b, _8),
	c8(c, _8),
	d8(d, _8),
	e8(e, _8),
	f8(f, _8),
	g8(g, _8),
	h8(h, _8);

	private static final Map<GenericFile, Map<GenericRank, GenericPosition>> positions = new EnumMap<>(GenericFile.class);

	static {
		for (GenericFile file : GenericFile.values()) {
			positions.put(file, new EnumMap<>(GenericRank.class));
		}
		for (GenericPosition position : values()) {
			positions.get(position.file).put(position.rank, position);
		}
	}

	public final GenericFile file;
	public final GenericRank rank;

	private GenericPosition(GenericFile file, GenericRank rank) {
		this.file = file;
		this.rank = rank;
	}

	/**
	 * Returns the {@link GenericPosition} for the specified {@link GenericFile} and {@link GenericRank}.
	 *
	 * @param file a {@link GenericFile}
	 * @param rank a {@link GenericRank}
	 * @return the {@link GenericPosition} for the specified {@link GenericFile} and {@link GenericRank}
	 */
	public static GenericPosition of(GenericFile file, GenericRank rank) {
		if (file == null) throw new IllegalArgumentException();
		if (rank == null) throw new IllegalArgumentException();

		return positions.get(file).get(rank);
	}

	/**
	 * Tries to convert the specified token to a {@link GenericPosition}. The token can be lowercase or uppercase.
	 *
	 * @param token a string of length 2 to convert to a {@link GenericPosition}
	 * @return an {@link Optional} containing the {@link GenericPosition} if conversion was successful,
	 * {@link Optional#empty()} otherwise
	 */
	public static Optional<GenericPosition> from(String token) {
		if (token == null) throw new IllegalArgumentException();
		if (token.length() != 2) throw new IllegalArgumentException();

		Optional<GenericFile> file = GenericFile.of(token.charAt(0));
		Optional<GenericRank> rank = GenericRank.of(token.charAt(1));

		if (file.isPresent() && rank.isPresent()) {
			return Optional.of(of(file.get(), rank.get()));
		}
		return Optional.empty();
	}

	/**
	 * Returns the string representing this {@link GenericPosition}.
	 *
	 * @return the string representing this {@link GenericPosition}
	 */
	@Override
	public String toString() {
		return file.toString() + rank.toString();
	}

}
