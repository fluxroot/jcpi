/*
 * Copyright 2007-2014 the original author or authors.
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

import com.fluxchess.jcpi.models.IntChessman;
import com.fluxchess.jcpi.models.IntPiece;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.fluxchess.test.AssertUtil.assertUtilityClassWellDefined;
import static org.junit.Assert.assertEquals;

public class MoveTest {

  @Test
  public void testUtilityClass() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertUtilityClassWellDefined(Move.class);
  }

  @Test
  public void testCreation() {
    int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.a7, Square.b8, IntPiece.WHITEPAWN, IntPiece.BLACKQUEEN, IntChessman.KNIGHT);

    assertEquals(Move.Type.PAWNPROMOTION, Move.getType(move));
    assertEquals(Square.a7, Move.getOriginSquare(move));
    assertEquals(Square.b8, Move.getTargetSquare(move));
    assertEquals(IntPiece.WHITEPAWN, Move.getOriginPiece(move));
    assertEquals(IntPiece.BLACKQUEEN, Move.getTargetPiece(move));
    assertEquals(IntChessman.KNIGHT, Move.getPromotion(move));
  }

  @Test
  public void testSetTargetPosition() {
    int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.b7, Square.c8, IntPiece.WHITEPAWN, IntPiece.BLACKQUEEN, IntChessman.KNIGHT);

    assertEquals(Square.c8, Move.getTargetSquare(move));

    move = Move.setTargetSquare(move, Square.a8);

    assertEquals(Square.a8, Move.getTargetSquare(move));

    move = Move.setTargetSquareAndPiece(move, Square.c8, IntPiece.BLACKKNIGHT);

    assertEquals(Square.c8, Move.getTargetSquare(move));
    assertEquals(IntPiece.BLACKKNIGHT, Move.getTargetPiece(move));
  }

  @Test
  public void testPromotion() {
    int move = Move.valueOf(Move.Type.PAWNPROMOTION, Square.b7, Square.c8, IntPiece.WHITEPAWN, IntPiece.BLACKQUEEN, IntChessman.KNIGHT);

    assertEquals(IntChessman.KNIGHT, Move.getPromotion(move));

    move = Move.setPromotion(move, IntChessman.QUEEN);

    assertEquals(IntChessman.QUEEN, Move.getPromotion(move));
  }

}
