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

public class IntRankTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntRank.class);
  }

  @Test
  public void testValues() {
    for (GenericRank genericRank : GenericRank.values()) {
      assertEquals(genericRank, IntRank.toGenericRank(IntRank.valueOf(genericRank)));
      assertEquals(genericRank.ordinal(), IntRank.valueOf(genericRank));
      assertEquals(IntRank.valueOf(genericRank), IntRank.values[IntRank.valueOf(genericRank)]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntRank.valueOf(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericRank() {
    IntRank.toGenericRank(IntRank.NORANK);
  }

  @Test
  public void testIsValid() {
    for (int rank : IntRank.values) {
      assertTrue(IntRank.isValid(rank));
      assertEquals(rank, rank & IntRank.MASK);
    }

    assertFalse(IntRank.isValid(IntRank.NORANK));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsValid() {
    IntRank.isValid(-1);
  }

}
