/*
 * Copyright 2007-2015 the original author or authors.
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

  a1(GenericFile.Fa, GenericRank.R1),
  b1(GenericFile.Fb, GenericRank.R1),
  c1(GenericFile.Fc, GenericRank.R1),
  d1(GenericFile.Fd, GenericRank.R1),
  e1(GenericFile.Fe, GenericRank.R1),
  f1(GenericFile.Ff, GenericRank.R1),
  g1(GenericFile.Fg, GenericRank.R1),
  h1(GenericFile.Fh, GenericRank.R1),
  a2(GenericFile.Fa, GenericRank.R2),
  b2(GenericFile.Fb, GenericRank.R2),
  c2(GenericFile.Fc, GenericRank.R2),
  d2(GenericFile.Fd, GenericRank.R2),
  e2(GenericFile.Fe, GenericRank.R2),
  f2(GenericFile.Ff, GenericRank.R2),
  g2(GenericFile.Fg, GenericRank.R2),
  h2(GenericFile.Fh, GenericRank.R2),
  a3(GenericFile.Fa, GenericRank.R3),
  b3(GenericFile.Fb, GenericRank.R3),
  c3(GenericFile.Fc, GenericRank.R3),
  d3(GenericFile.Fd, GenericRank.R3),
  e3(GenericFile.Fe, GenericRank.R3),
  f3(GenericFile.Ff, GenericRank.R3),
  g3(GenericFile.Fg, GenericRank.R3),
  h3(GenericFile.Fh, GenericRank.R3),
  a4(GenericFile.Fa, GenericRank.R4),
  b4(GenericFile.Fb, GenericRank.R4),
  c4(GenericFile.Fc, GenericRank.R4),
  d4(GenericFile.Fd, GenericRank.R4),
  e4(GenericFile.Fe, GenericRank.R4),
  f4(GenericFile.Ff, GenericRank.R4),
  g4(GenericFile.Fg, GenericRank.R4),
  h4(GenericFile.Fh, GenericRank.R4),
  a5(GenericFile.Fa, GenericRank.R5),
  b5(GenericFile.Fb, GenericRank.R5),
  c5(GenericFile.Fc, GenericRank.R5),
  d5(GenericFile.Fd, GenericRank.R5),
  e5(GenericFile.Fe, GenericRank.R5),
  f5(GenericFile.Ff, GenericRank.R5),
  g5(GenericFile.Fg, GenericRank.R5),
  h5(GenericFile.Fh, GenericRank.R5),
  a6(GenericFile.Fa, GenericRank.R6),
  b6(GenericFile.Fb, GenericRank.R6),
  c6(GenericFile.Fc, GenericRank.R6),
  d6(GenericFile.Fd, GenericRank.R6),
  e6(GenericFile.Fe, GenericRank.R6),
  f6(GenericFile.Ff, GenericRank.R6),
  g6(GenericFile.Fg, GenericRank.R6),
  h6(GenericFile.Fh, GenericRank.R6),
  a7(GenericFile.Fa, GenericRank.R7),
  b7(GenericFile.Fb, GenericRank.R7),
  c7(GenericFile.Fc, GenericRank.R7),
  d7(GenericFile.Fd, GenericRank.R7),
  e7(GenericFile.Fe, GenericRank.R7),
  f7(GenericFile.Ff, GenericRank.R7),
  g7(GenericFile.Fg, GenericRank.R7),
  h7(GenericFile.Fh, GenericRank.R7),
  a8(GenericFile.Fa, GenericRank.R8),
  b8(GenericFile.Fb, GenericRank.R8),
  c8(GenericFile.Fc, GenericRank.R8),
  d8(GenericFile.Fd, GenericRank.R8),
  e8(GenericFile.Fe, GenericRank.R8),
  f8(GenericFile.Ff, GenericRank.R8),
  g8(GenericFile.Fg, GenericRank.R8),
  h8(GenericFile.Fh, GenericRank.R8);

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
