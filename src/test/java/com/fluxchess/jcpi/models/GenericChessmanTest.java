/*
 * Copyright 2007-2013 the original author or authors.
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

import static org.junit.Assert.*;

public class GenericChessmanTest {

  @Test
  public void testValueOf() {
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('q'));
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('Q'));
    assertNull(GenericChessman.valueOf('x'));
  }

  @Test
  public void testValueOfPromotion() {
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('q'));
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('Q'));
    assertNull(GenericChessman.valueOfPromotion('p'));
  }

  @Test
  public void testIsValidPromotion() {
    assertTrue(GenericChessman.QUEEN.isValidPromotion());
    assertFalse(GenericChessman.PAWN.isValidPromotion());
  }

  @Test
  public void testToCharAlgebraic() {
    assertEquals(GenericChessman.QUEEN.toCharAlgebraic(), 'Q');
    try {
      GenericChessman.PAWN.toCharAlgebraic();
      fail();
    } catch (UnsupportedOperationException e) {
    }
  }

  @Test
  public void testToChar() {
    assertEquals('q', GenericChessman.QUEEN.toChar(GenericColor.BLACK));
  }

}
