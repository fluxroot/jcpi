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

import static com.fluxchess.jcpi.models.GenericFile.*;
import static com.fluxchess.jcpi.models.GenericRank.*;

public enum GenericPosition {

	a1(A, _1),
	b1(B, _1),
	c1(C, _1),
	d1(D, _1),
	e1(E, _1),
	f1(F, _1),
	g1(G, _1),
	h1(H, _1),
	a2(A, _2),
	b2(B, _2),
	c2(C, _2),
	d2(D, _2),
	e2(E, _2),
	f2(F, _2),
	g2(G, _2),
	h2(H, _2),
	a3(A, _3),
	b3(B, _3),
	c3(C, _3),
	d3(D, _3),
	e3(E, _3),
	f3(F, _3),
	g3(G, _3),
	h3(H, _3),
	a4(A, _4),
	b4(B, _4),
	c4(C, _4),
	d4(D, _4),
	e4(E, _4),
	f4(F, _4),
	g4(G, _4),
	h4(H, _4),
	a5(A, _5),
	b5(B, _5),
	c5(C, _5),
	d5(D, _5),
	e5(E, _5),
	f5(F, _5),
	g5(G, _5),
	h5(H, _5),
	a6(A, _6),
	b6(B, _6),
	c6(C, _6),
	d6(D, _6),
	e6(E, _6),
	f6(F, _6),
	g6(G, _6),
	h6(H, _6),
	a7(A, _7),
	b7(B, _7),
	c7(C, _7),
	d7(D, _7),
	e7(E, _7),
	f7(F, _7),
	g7(G, _7),
	h7(H, _7),
	a8(A, _8),
	b8(B, _8),
	c8(C, _8),
	d8(D, _8),
	e8(E, _8),
	f8(F, _8),
	g8(G, _8),
	h8(H, _8);

	private static final Map<GenericFile, Map<GenericRank, GenericPosition>> allPositions = new EnumMap<GenericFile, Map<GenericRank, GenericPosition>>(GenericFile.class);

	static {
		for (GenericFile file : GenericFile.values()) {
			allPositions.put(file, new EnumMap<GenericRank, GenericPosition>(GenericRank.class));
		}
		for (GenericPosition position : values()) {
			allPositions.get(position.file).put(position.rank, position);
		}
	}

	public final GenericFile file;
	public final GenericRank rank;

	private GenericPosition(GenericFile file, GenericRank rank) {
		this.file = file;
		this.rank = rank;
	}

	public static GenericPosition valueOf(GenericFile file, GenericRank rank) {
		if (file == null) throw new IllegalArgumentException();
		if (rank == null) throw new IllegalArgumentException();

		return allPositions.get(file).get(rank);
	}

	public String toString() {
		return this.file.toString() + this.rank.toString();
	}

}
