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

import com.fluxchess.jcpi.models.GenericPosition;
import com.fluxchess.jcpi.models.IntFile;
import com.fluxchess.jcpi.models.IntRank;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.fluxchess.test.AssertUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.*;

public class SquareTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(Square.class);
  }

  @Test
  public void testValues() {
    for (GenericPosition genericPosition : GenericPosition.values()) {
      int square = Square.valueOf(genericPosition);
      assertTrue(Square.isLegal(square));
      assertTrue(Square.isValid(square));
      assertEquals(genericPosition, Square.toGenericPosition(square));

      int file = Square.getFile(square);
      assertTrue(IntFile.isValid(file));
      assertEquals(genericPosition.file.ordinal(), file);
      assertEquals(genericPosition.file, IntFile.toGenericFile(file));

      int rank = Square.getRank(square);
      assertTrue(IntRank.isValid(rank));
      assertEquals(genericPosition.rank.ordinal(), rank);
      assertEquals(genericPosition.rank, IntRank.toGenericRank(rank));
    }

    assertFalse(Square.isLegal(Square.NOSQUARE));
    assertFalse(Square.isValid(Square.NOSQUARE));
  }

  @Test
  public void testX88Squares() {
    int bitSquare = 0;
    for (int x88Square : Square.values) {
      assertEquals(bitSquare, Square.toBitSquare(x88Square));
      assertEquals(x88Square, Square.toX88Square(bitSquare));
      ++bitSquare;
    }
  }

}
