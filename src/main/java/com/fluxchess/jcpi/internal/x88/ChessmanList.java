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
package com.fluxchess.jcpi.internal.x88;

final class ChessmanList {

  public long positions = 0;

  static int toX88Position(int position) {
    assert position >= 0 && position < Long.SIZE;

    return ((position & ~7) << 1) | (position & 7);
  }

  static int toBitPosition(int position) {
    assert (position & 0x88) == 0;

    return ((position & ~7) >>> 1) | (position & 7);
  }

  public static int next(long positions) {
    return ChessmanList.toX88Position(Long.numberOfTrailingZeros(positions));
  }

  public int size() {
    return Long.bitCount(positions);
  }

  public void add(int position) {
    assert (position & 0x88) == 0;
    assert (positions & (1L << toBitPosition(position))) == 0 : String.format("positions = %d, 0x88 position = %d, bit position = %d", positions, position, toBitPosition(position));

    positions |= 1L << toBitPosition(position);
  }

  public void remove(int position) {
    assert (position & 0x88) == 0;
    assert (positions & (1L << toBitPosition(position))) != 0 : String.format("positions = %d, 0x88 position = %d, bit position = %d", positions, position, toBitPosition(position));

    positions &= ~(1L << toBitPosition(position));
  }

}
