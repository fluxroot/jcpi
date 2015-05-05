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

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.fluxchess.test.AssertUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.*;

public class IntCastlingTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntCastling.class);
  }

  @Test
  public void testValues() {
    for (GenericCastling genericCastling : GenericCastling.values()) {
      assertEquals(genericCastling, IntCastling.toGenericCastling(IntCastling.valueOf(genericCastling)));
      assertEquals(genericCastling.ordinal(), IntCastling.valueOf(genericCastling));
      assertEquals(IntCastling.valueOf(genericCastling), IntCastling.values[IntCastling.valueOf(genericCastling)]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntCastling.valueOf(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericCastling() {
    IntCastling.toGenericCastling(IntCastling.NOCASTLING);
  }

  @Test
  public void testIsValid() {
    for (int castling : IntCastling.values) {
      assertTrue(IntCastling.isValid(castling));
      assertEquals(castling, castling & IntCastling.MASK);
    }

    assertFalse(IntCastling.isValid(IntCastling.NOCASTLING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsValid() {
    IntCastling.isValid(-1);
  }

}
