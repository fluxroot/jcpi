/*
 * Copyright (C) 2007-2014 Phokham Nonava
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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericPiece;
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
      assertEquals(genericPiece, IntPiece.toGenericPiece(IntPiece.valueOf(PieceType.valueOf(genericPiece.chessman), Color.valueOf(genericPiece.color))));
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
    assertEquals(PieceType.PAWN, IntPiece.getChessman(IntPiece.WHITEPAWN));
    assertEquals(PieceType.PAWN, IntPiece.getChessman(IntPiece.BLACKPAWN));
    assertEquals(PieceType.KNIGHT, IntPiece.getChessman(IntPiece.WHITEKNIGHT));
    assertEquals(PieceType.KNIGHT, IntPiece.getChessman(IntPiece.BLACKKNIGHT));
    assertEquals(PieceType.BISHOP, IntPiece.getChessman(IntPiece.WHITEBISHOP));
    assertEquals(PieceType.BISHOP, IntPiece.getChessman(IntPiece.BLACKBISHOP));
    assertEquals(PieceType.ROOK, IntPiece.getChessman(IntPiece.WHITEROOK));
    assertEquals(PieceType.ROOK, IntPiece.getChessman(IntPiece.BLACKROOK));
    assertEquals(PieceType.QUEEN, IntPiece.getChessman(IntPiece.WHITEQUEEN));
    assertEquals(PieceType.QUEEN, IntPiece.getChessman(IntPiece.BLACKQUEEN));
    assertEquals(PieceType.KING, IntPiece.getChessman(IntPiece.WHITEKING));
    assertEquals(PieceType.KING, IntPiece.getChessman(IntPiece.BLACKKING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetChessman() {
    IntPiece.getChessman(IntPiece.NOPIECE);
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEPAWN));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKPAWN));
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEKNIGHT));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKKNIGHT));
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEBISHOP));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKBISHOP));
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEROOK));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKROOK));
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEQUEEN));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKQUEEN));
    assertEquals(Color.WHITE, IntPiece.getColor(IntPiece.WHITEKING));
    assertEquals(Color.BLACK, IntPiece.getColor(IntPiece.BLACKKING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetColor() {
    IntPiece.getColor(IntPiece.NOPIECE);
  }

}
