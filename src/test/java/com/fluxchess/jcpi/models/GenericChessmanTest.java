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

import static org.junit.Assert.*;

public class GenericChessmanTest {

  @Test
  public void testValueOf() {
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('q'));
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('Q'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    GenericChessman.valueOf('x');
  }

  @Test
  public void testIsValid() {
    assertFalse(GenericChessman.isValid('x'));
  }

  @Test
  public void testValueOfPromotion() {
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('q'));
    assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('Q'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOfPromotion() {
    GenericChessman.valueOfPromotion('p');
  }

  @Test
  public void testIsValidPromotion() {
    assertFalse(GenericChessman.isValidPromotion('p'));
  }

  @Test
  public void testIsLegalPromotion() {
    assertTrue(GenericChessman.QUEEN.isLegalPromotion());
    assertFalse(GenericChessman.PAWN.isLegalPromotion());
  }

  @Test
  public void testIsSliding() {
    assertTrue(GenericChessman.BISHOP.isSliding());
    assertTrue(GenericChessman.ROOK.isSliding());
    assertTrue(GenericChessman.QUEEN.isSliding());
    assertFalse(GenericChessman.PAWN.isSliding());
    assertFalse(GenericChessman.KNIGHT.isSliding());
    assertFalse(GenericChessman.KING.isSliding());
  }

  @Test
  public void testToCharAlgebraic() {
    assertEquals(GenericChessman.QUEEN.toCharAlgebraic(), 'Q');
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testInvalidToCharAlgebraic() {
    GenericChessman.PAWN.toCharAlgebraic();
  }

  @Test
  public void testToChar() {
    assertEquals('q', GenericChessman.QUEEN.toChar(GenericColor.BLACK));
  }

}
