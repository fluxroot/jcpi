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

public class GenericMoveTest {

  @Test
  public void testParse() throws IllegalNotationException {
    GenericMove move = new GenericMove("a1 e3");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertNull(move.promotion);

    move = new GenericMove("a1 x e3");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertNull(move.promotion);

    move = new GenericMove("a1 : e3");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertNull(move.promotion);

    move = new GenericMove("a1 x e3 = q");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertEquals(GenericChessman.QUEEN, move.promotion);

    move = new GenericMove("a1 x e3 = q #");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertEquals(GenericChessman.QUEEN, move.promotion);

    move = new GenericMove("a1 - e3 = q");
    assertEquals(GenericPosition.a1, move.from);
    assertEquals(GenericPosition.e3, move.to);
    assertEquals(GenericChessman.QUEEN, move.promotion);
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidMove1() throws IllegalNotationException {
    GenericMove move = new GenericMove("a1 - e3 = z");
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidMove2() throws IllegalNotationException {
    GenericMove move = new GenericMove("n1 e3");
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidMove3() throws IllegalNotationException {
    GenericMove move = new GenericMove("a9 e3");
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidMove4() throws IllegalNotationException {
    GenericMove move = new GenericMove("a1 n3");
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidMove5() throws IllegalNotationException {
    GenericMove move = new GenericMove("a1 e9");
  }

  @Test
  public void testToString() {
    GenericMove move = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.ROOK);
    assertEquals("a1e3r", move.toString());
  }

  @Test
  public void testEqualsHashCode() {
    GenericMove move1 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.QUEEN);
    GenericMove move2 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.QUEEN);

    GenericMove move3 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.ROOK);
    GenericMove move4 = new GenericMove(GenericPosition.a1, GenericPosition.e4, GenericChessman.QUEEN);
    GenericMove move5 = new GenericMove(GenericPosition.a2, GenericPosition.e3, GenericChessman.QUEEN);
    GenericMove move6 = new GenericMove(GenericPosition.a1, GenericPosition.e3);

    // reflexive test
    assertTrue(move1.equals(move1));

    // symmetric test
    assertTrue(move1.equals(move2));
    assertTrue(move2.equals(move1));

    // null value test
    assertFalse(move1.equals(null));

    // inheritance test
    assertFalse(move1.equals(new Object()));

    // attributes test
    assertFalse(move1.equals(move3));
    assertFalse(move1.equals(move4));
    assertFalse(move1.equals(move5));
    assertFalse(move1.equals(move6));

    // hash code test
    assertEquals(move1.hashCode(), move2.hashCode());
    assertNotEquals(move1.hashCode(), move3.hashCode());
    assertNotEquals(move1.hashCode(), move4.hashCode());
    assertNotEquals(move1.hashCode(), move5.hashCode());
    assertNotEquals(move1.hashCode(), move6.hashCode());
  }

}
