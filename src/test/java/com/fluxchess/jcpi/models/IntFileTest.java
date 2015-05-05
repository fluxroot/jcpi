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

public class IntFileTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntFile.class);
  }

  @Test
  public void testValues() {
    for (GenericFile genericFile : GenericFile.values()) {
      assertEquals(genericFile, IntFile.toGenericFile(IntFile.valueOf(genericFile)));
      assertEquals(genericFile.ordinal(), IntFile.valueOf(genericFile));
      assertEquals(IntFile.valueOf(genericFile), IntFile.values[IntFile.valueOf(genericFile)]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntFile.valueOf(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericFile() {
    IntFile.toGenericFile(IntFile.NOFILE);
  }

  @Test
  public void testIsValid() {
    for (int file : IntFile.values) {
      assertTrue(IntFile.isValid(file));
      assertEquals(file, file & IntFile.MASK);
    }

    assertFalse(IntFile.isValid(IntFile.NOFILE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsValid() {
    IntFile.isValid(-1);
  }

}
