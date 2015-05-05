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

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChessmanListTest {

  private Random random = null;
  private LinkedList<Integer> pool = null;

  @Before
  public void setUp() {
    random = new Random();
    pool = new LinkedList<Integer>();

    while (pool.size() < Long.SIZE) {
      int value = random.nextInt(Long.SIZE);
      if (!pool.contains(Position.values[value])) {
        pool.add(Position.values[value]);
      }
    }
  }

  @Test
  public void testX88Positions() {
    int bitposition = 0;
    for (int x88position : Position.values) {
      assertEquals(bitposition, ChessmanList.toBitPosition(x88position));
      assertEquals(x88position, ChessmanList.toX88Position(bitposition));
      ++bitposition;
    }
  }

  @Test
  public void testAdd() {
    ChessmanList list = new ChessmanList();

    for (int x88position : pool) {
      list.add(x88position);
    }

    assertEquals(-1, list.positions);
  }

  @Test
  public void testRemove() {
    ChessmanList list = new ChessmanList();
    list.positions = -1;

    for (int x88position : pool) {
      list.remove(x88position);
    }

    assertEquals(0, list.positions);
  }

}
