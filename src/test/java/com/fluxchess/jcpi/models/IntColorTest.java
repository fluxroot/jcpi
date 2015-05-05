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

public class IntColorTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntColor.class);
  }

  @Test
  public void testValues() {
    for (GenericColor genericColor : GenericColor.values()) {
      assertEquals(genericColor, IntColor.toGenericColor(IntColor.valueOf(genericColor)));
      assertEquals(genericColor.ordinal(), IntColor.valueOf(genericColor));
      assertEquals(IntColor.valueOf(genericColor), IntColor.values[IntColor.valueOf(genericColor)]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntColor.valueOf(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericColor() {
    IntColor.toGenericColor(IntColor.NOCOLOR);
  }

  @Test
  public void testIsValid() {
    for (int color : IntColor.values) {
      assertTrue(IntColor.isValid(color));
      assertEquals(color, color & IntColor.MASK);
    }

    assertFalse(IntColor.isValid(IntColor.NOCOLOR));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsValid() {
    IntColor.isValid(-1);
  }

  @Test
  public void testOpposite() {
    assertEquals(IntColor.WHITE, IntColor.opposite(IntColor.BLACK));
    assertEquals(IntColor.BLACK, IntColor.opposite(IntColor.WHITE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidOpposite() {
    IntColor.opposite(IntColor.NOCOLOR);
  }

}
