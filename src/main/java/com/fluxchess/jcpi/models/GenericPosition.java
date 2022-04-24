/*
 * Copyright 2007-2022 The Java Chess Protocol Interface Project Authors
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

public enum GenericPosition {

	a1(GenericFile.A, GenericRank._1),
	b1(GenericFile.B, GenericRank._1),
	c1(GenericFile.C, GenericRank._1),
	d1(GenericFile.D, GenericRank._1),
	e1(GenericFile.E, GenericRank._1),
	f1(GenericFile.F, GenericRank._1),
	g1(GenericFile.G, GenericRank._1),
	h1(GenericFile.H, GenericRank._1),
	a2(GenericFile.A, GenericRank._2),
	b2(GenericFile.B, GenericRank._2),
	c2(GenericFile.C, GenericRank._2),
	d2(GenericFile.D, GenericRank._2),
	e2(GenericFile.E, GenericRank._2),
	f2(GenericFile.F, GenericRank._2),
	g2(GenericFile.G, GenericRank._2),
	h2(GenericFile.H, GenericRank._2),
	a3(GenericFile.A, GenericRank._3),
	b3(GenericFile.B, GenericRank._3),
	c3(GenericFile.C, GenericRank._3),
	d3(GenericFile.D, GenericRank._3),
	e3(GenericFile.E, GenericRank._3),
	f3(GenericFile.F, GenericRank._3),
	g3(GenericFile.G, GenericRank._3),
	h3(GenericFile.H, GenericRank._3),
	a4(GenericFile.A, GenericRank._4),
	b4(GenericFile.B, GenericRank._4),
	c4(GenericFile.C, GenericRank._4),
	d4(GenericFile.D, GenericRank._4),
	e4(GenericFile.E, GenericRank._4),
	f4(GenericFile.F, GenericRank._4),
	g4(GenericFile.G, GenericRank._4),
	h4(GenericFile.H, GenericRank._4),
	a5(GenericFile.A, GenericRank._5),
	b5(GenericFile.B, GenericRank._5),
	c5(GenericFile.C, GenericRank._5),
	d5(GenericFile.D, GenericRank._5),
	e5(GenericFile.E, GenericRank._5),
	f5(GenericFile.F, GenericRank._5),
	g5(GenericFile.G, GenericRank._5),
	h5(GenericFile.H, GenericRank._5),
	a6(GenericFile.A, GenericRank._6),
	b6(GenericFile.B, GenericRank._6),
	c6(GenericFile.C, GenericRank._6),
	d6(GenericFile.D, GenericRank._6),
	e6(GenericFile.E, GenericRank._6),
	f6(GenericFile.F, GenericRank._6),
	g6(GenericFile.G, GenericRank._6),
	h6(GenericFile.H, GenericRank._6),
	a7(GenericFile.A, GenericRank._7),
	b7(GenericFile.B, GenericRank._7),
	c7(GenericFile.C, GenericRank._7),
	d7(GenericFile.D, GenericRank._7),
	e7(GenericFile.E, GenericRank._7),
	f7(GenericFile.F, GenericRank._7),
	g7(GenericFile.G, GenericRank._7),
	h7(GenericFile.H, GenericRank._7),
	a8(GenericFile.A, GenericRank._8),
	b8(GenericFile.B, GenericRank._8),
	c8(GenericFile.C, GenericRank._8),
	d8(GenericFile.D, GenericRank._8),
	e8(GenericFile.E, GenericRank._8),
	f8(GenericFile.F, GenericRank._8),
	g8(GenericFile.G, GenericRank._8),
	h8(GenericFile.H, GenericRank._8);

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
