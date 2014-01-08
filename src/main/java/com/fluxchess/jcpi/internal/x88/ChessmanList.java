/*
 * Copyright 2007-2014 the original author or authors.
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

final class ChessmanList {

  public long squares = 0;

  static int toX88Square(int square) {
    assert square >= 0 && square < Long.SIZE;

    return ((square & ~7) << 1) | (square & 7);
  }

  static int toBitSquare(int square) {
    assert (square & 0x88) == 0;

    return ((square & ~7) >>> 1) | (square & 7);
  }

  public static int next(long squares) {
    return ChessmanList.toX88Square(Long.numberOfTrailingZeros(squares));
  }

  public int size() {
    return Long.bitCount(squares);
  }

  public void add(int square) {
    assert (square & 0x88) == 0;
    assert (squares & (1L << toBitSquare(square))) == 0 : String.format("squares = %d, 0x88 square = %d, bit square = %d", squares, square, toBitSquare(square));

    squares |= 1L << toBitSquare(square);
  }

  public void remove(int square) {
    assert (square & 0x88) == 0;
    assert (squares & (1L << toBitSquare(square))) != 0 : String.format("squares = %d, 0x88 square = %d, bit square = %d", squares, square, toBitSquare(square));

    squares &= ~(1L << toBitSquare(square));
  }

}
