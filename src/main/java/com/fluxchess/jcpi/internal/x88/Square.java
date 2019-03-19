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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericPosition;

final class Square {

  public static final int MASK = 0x7F;

  public static final int a1 = 0;   public static final int a2 = 16;
  public static final int b1 = 1;   public static final int b2 = 17;
  public static final int c1 = 2;   public static final int c2 = 18;
  public static final int d1 = 3;   public static final int d2 = 19;
  public static final int e1 = 4;   public static final int e2 = 20;
  public static final int f1 = 5;   public static final int f2 = 21;
  public static final int g1 = 6;   public static final int g2 = 22;
  public static final int h1 = 7;   public static final int h2 = 23;

  public static final int a3 = 32;  public static final int a4 = 48;
  public static final int b3 = 33;  public static final int b4 = 49;
  public static final int c3 = 34;  public static final int c4 = 50;
  public static final int d3 = 35;  public static final int d4 = 51;
  public static final int e3 = 36;  public static final int e4 = 52;
  public static final int f3 = 37;  public static final int f4 = 53;
  public static final int g3 = 38;  public static final int g4 = 54;
  public static final int h3 = 39;  public static final int h4 = 55;

  public static final int a5 = 64;  public static final int a6 = 80;
  public static final int b5 = 65;  public static final int b6 = 81;
  public static final int c5 = 66;  public static final int c6 = 82;
  public static final int d5 = 67;  public static final int d6 = 83;
  public static final int e5 = 68;  public static final int e6 = 84;
  public static final int f5 = 69;  public static final int f6 = 85;
  public static final int g5 = 70;  public static final int g6 = 86;
  public static final int h5 = 71;  public static final int h6 = 87;

  public static final int a7 = 96;  public static final int a8 = 112;
  public static final int b7 = 97;  public static final int b8 = 113;
  public static final int c7 = 98;  public static final int c8 = 114;
  public static final int d7 = 99;  public static final int d8 = 115;
  public static final int e7 = 100; public static final int e8 = 116;
  public static final int f7 = 101; public static final int f8 = 117;
  public static final int g7 = 102; public static final int g8 = 118;
  public static final int h7 = 103; public static final int h8 = 119;

  public static final int NOSQUARE = 127;

  public static final int[] values = {
    a1, b1, c1, d1, e1, f1, g1, h1,
    a2, b2, c2, d2, e2, f2, g2, h2,
    a3, b3, c3, d3, e3, f3, g3, h3,
    a4, b4, c4, d4, e4, f4, g4, h4,
    a5, b5, c5, d5, e5, f5, g5, h5,
    a6, b6, c6, d6, e6, f6, g6, h6,
    a7, b7, c7, d7, e7, f7, g7, h7,
    a8, b8, c8, d8, e8, f8, g8, h8
  };

  public static final int deltaN = 16;
  public static final int deltaE = 1;
  public static final int deltaS = -16;
  public static final int deltaW = -1;
  public static final int deltaNE = deltaN + deltaE;
  public static final int deltaSE = deltaS + deltaE;
  public static final int deltaSW = deltaS + deltaW;
  public static final int deltaNW = deltaN + deltaW;

  private Square() {
  }

  public static int valueOf(GenericPosition genericPosition) {
    assert genericPosition != null;

    int square = Rank.valueOf(genericPosition.rank) * 16 + File.valueOf(genericPosition.file);
    assert isValid(square);

    return square;
  }

  public static GenericPosition toGenericPosition(int square) {
    assert isValid(square);

    return GenericPosition.valueOf(File.toGenericFile(getFile(square)), Rank.toGenericRank(getRank(square)));
  }

  public static int toX88Square(int square) {
    assert square >= 0 && square < Long.SIZE;

    return ((square & ~7) << 1) | (square & 7);
  }

  public static int toBitSquare(int square) {
    assert isValid(square);

    return ((square & ~7) >>> 1) | (square & 7);
  }

  public static long toBitboard(int square) {
    assert isValid(square);

    return 1L << toBitSquare(square);
  }

  public static boolean isValid(int square) {
    if (isLegal(square)) {
      return true;
    } else if (square == NOSQUARE) {
      return false;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isLegal(int square) {
    return (square & 0x88) == 0;
  }

  public static int getFile(int square) {
    assert isValid(square);

    int file = square % 16;
    assert File.isValid(file);

    return file;
  }

  public static int getRank(int square) {
    assert isValid(square);

    int rank = square >>> 4;
    assert Rank.isValid(rank);

    return rank;
  }

}
