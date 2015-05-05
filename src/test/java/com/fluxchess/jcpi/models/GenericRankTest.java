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

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GenericRankTest {

  @Test
  public void testValueOf() {
    assertEquals(GenericRank.R1, GenericRank.valueOf('1'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    GenericRank.valueOf('9');
  }

  @Test
  public void testIsValid() {
    assertFalse(GenericRank.isValid('9'));
  }

  @Test
  public void testPrev() {
    assertEquals(GenericRank.R7, GenericRank.R8.prev());
    assertEquals(GenericRank.R1, GenericRank.R8.prev(7));
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalPrev1() {
    GenericRank.R1.prev();
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalPrev2() {
    GenericRank.R7.prev(7);
  }

  @Test
  public void testHasPrev1() {
    assertFalse(GenericRank.R1.hasPrev());
  }

  @Test
  public void testHasPrev2() {
    assertFalse(GenericRank.R7.hasPrev(7));
  }

  @Test
  public void testNext() {
    assertEquals(GenericRank.R2, GenericRank.R1.next());
    assertEquals(GenericRank.R8, GenericRank.R1.next(7));
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalNext1() {
    GenericRank.R8.next();
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalNext2() {
    GenericRank.R2.next(7);
  }

  @Test
  public void testHasNext1() {
    assertFalse(GenericRank.R8.hasNext());
  }

  @Test
  public void testHasNext2() {
    assertFalse(GenericRank.R2.hasNext(7));
  }

  @Test
  public void testToChar() {
    assertEquals('1', GenericRank.R1.toChar());
  }

  @Test
  public void testToString() {
    assertEquals("1", GenericRank.R1.toString());
  }

}
