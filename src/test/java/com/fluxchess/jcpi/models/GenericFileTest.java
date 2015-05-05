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

public class GenericFileTest {

  @Test
  public void testValueOf() {
    assertEquals(GenericFile.Fa, GenericFile.valueOf('a'));
    assertEquals(GenericFile.Fa, GenericFile.valueOf('A'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    GenericFile.valueOf('i');
  }

  @Test
  public void testIsValid() {
    assertFalse(GenericFile.isValid('i'));
  }

  @Test
  public void testPrev() {
    assertEquals(GenericFile.Fg, GenericFile.Fh.prev());
    assertEquals(GenericFile.Fa, GenericFile.Fh.prev(7));
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalPrev1() {
    GenericFile.Fa.prev();
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalPrev2() {
    GenericFile.Fg.prev(7);
  }

  @Test
  public void testHasPrev1() {
    assertFalse(GenericFile.Fa.hasPrev());
  }

  @Test
  public void testHasPrev2() {
    assertFalse(GenericFile.Fg.hasPrev(7));
  }

  @Test
  public void testNext() {
    assertEquals(GenericFile.Fb, GenericFile.Fa.next());
    assertEquals(GenericFile.Fh, GenericFile.Fa.next(7));
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalNext1() {
    GenericFile.Fh.next();
  }

  @Test(expected = NoSuchElementException.class)
  public void testIllegalNext2() {
    GenericFile.Fb.next(7);
  }

  @Test
  public void testHasNext1() {
    assertFalse(GenericFile.Fh.hasNext());
  }

  @Test
  public void testHasNext2() {
    assertFalse(GenericFile.Fb.hasNext(7));
  }

  @Test
  public void testToChar() {
    assertEquals('a', GenericFile.Fa.toChar());
  }

  @Test
  public void testToString() {
    assertEquals("a", GenericFile.Fa.toString());
  }

}
