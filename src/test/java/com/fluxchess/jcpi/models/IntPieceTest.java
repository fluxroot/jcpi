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

public class IntPieceTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(IntPiece.class);
  }

  @Test
  public void testValues() {
    for (GenericPiece genericPiece : GenericPiece.values()) {
      assertEquals(genericPiece, IntPiece.toGenericPiece(IntPiece.valueOf(genericPiece)));
      assertEquals(genericPiece, IntPiece.toGenericPiece(IntPiece.valueOf(IntChessman.valueOf(genericPiece.chessman), IntColor.valueOf(genericPiece.color))));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueOf() {
    IntPiece.valueOf(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidToGenericPiece() {
    IntPiece.toGenericPiece(IntPiece.NOPIECE);
  }

  @Test
  public void testOrdinal() {
    for (int piece : IntPiece.values) {
      assertEquals(IntPiece.ordinal(piece), IntPiece.toGenericPiece(piece).ordinal());
      assertEquals(piece, IntPiece.values[IntPiece.ordinal(piece)]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidOrdinal() {
    IntPiece.ordinal(IntPiece.NOPIECE);
  }

  @Test
  public void testIsValid() {
    for (int piece : IntPiece.values) {
      assertTrue(IntPiece.isValid(piece));
      assertEquals(piece, piece & IntPiece.MASK);
    }

    assertFalse(IntPiece.isValid(IntPiece.NOPIECE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidIsValid() {
    IntPiece.isValid(-1);
  }

  @Test
  public void testGetChessman() {
    assertEquals(IntChessman.PAWN, IntPiece.getChessman(IntPiece.WHITEPAWN));
    assertEquals(IntChessman.PAWN, IntPiece.getChessman(IntPiece.BLACKPAWN));
    assertEquals(IntChessman.KNIGHT, IntPiece.getChessman(IntPiece.WHITEKNIGHT));
    assertEquals(IntChessman.KNIGHT, IntPiece.getChessman(IntPiece.BLACKKNIGHT));
    assertEquals(IntChessman.BISHOP, IntPiece.getChessman(IntPiece.WHITEBISHOP));
    assertEquals(IntChessman.BISHOP, IntPiece.getChessman(IntPiece.BLACKBISHOP));
    assertEquals(IntChessman.ROOK, IntPiece.getChessman(IntPiece.WHITEROOK));
    assertEquals(IntChessman.ROOK, IntPiece.getChessman(IntPiece.BLACKROOK));
    assertEquals(IntChessman.QUEEN, IntPiece.getChessman(IntPiece.WHITEQUEEN));
    assertEquals(IntChessman.QUEEN, IntPiece.getChessman(IntPiece.BLACKQUEEN));
    assertEquals(IntChessman.KING, IntPiece.getChessman(IntPiece.WHITEKING));
    assertEquals(IntChessman.KING, IntPiece.getChessman(IntPiece.BLACKKING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetChessman() {
    IntPiece.getChessman(IntPiece.NOPIECE);
  }

  @Test
  public void testGetColor() {
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEPAWN));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKPAWN));
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEKNIGHT));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKKNIGHT));
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEBISHOP));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKBISHOP));
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEROOK));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKROOK));
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEQUEEN));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKQUEEN));
    assertEquals(IntColor.WHITE, IntPiece.getColor(IntPiece.WHITEKING));
    assertEquals(IntColor.BLACK, IntPiece.getColor(IntPiece.BLACKKING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetColor() {
    IntPiece.getColor(IntPiece.NOPIECE);
  }

}
