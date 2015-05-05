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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GenericColorTest {

  @Test
  public final void testValueOf() {
    assertEquals(GenericColor.WHITE, GenericColor.valueOf('w'));
    assertEquals(GenericColor.BLACK, GenericColor.valueOf('b'));
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testInvalidValueOf() {
    GenericColor.valueOf('a');
  }

  @Test
  public final void testIsValid() {
    assertFalse(GenericColor.isValid('a'));
  }

  @Test
  public final void testColorOf() {
    assertEquals(GenericColor.WHITE, GenericColor.colorOf('A'));
    assertEquals(GenericColor.BLACK, GenericColor.colorOf('a'));
  }

  @Test
  public final void testTransform() {
    assertEquals('A', GenericColor.WHITE.transform('A'));
    assertEquals('A', GenericColor.WHITE.transform('a'));
    assertEquals('a', GenericColor.BLACK.transform('A'));
  }

  @Test
  public final void testOpposite() {
    assertEquals(GenericColor.WHITE, GenericColor.BLACK.opposite());
    assertEquals(GenericColor.BLACK, GenericColor.WHITE.opposite());
  }

  @Test
  public final void testToChar() {
    assertEquals('w', GenericColor.WHITE.toChar());
    assertEquals('b', GenericColor.BLACK.toChar());
  }

}
