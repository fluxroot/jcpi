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

public class GenericBoardTest {

  @Test
  public void testToString() throws IllegalNotationException {
    GenericBoard board1 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", board1.toString());

    GenericBoard board2 = new GenericBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
    assertEquals("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", board2.toString());

    GenericBoard board3 = new GenericBoard("8/1n4N1/2k5/8/8/5K2/1N4n1/8 b - - 0 1");
    assertEquals("8/1n4N1/2k5/8/8/5K2/1N4n1/8 b - - 0 1", board3.toString());

    GenericBoard board4 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
    assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", board4.toString());

    GenericBoard board5 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3");
    assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", board5.toString());

    GenericBoard board6 = new GenericBoard(328);
    assertEquals("nbrqbkrn/pppppppp/8/8/8/8/PPPPPPPP/NBRQBKRN w GCgc - 0 1", board6.toString());
  }

  @Test
  public void testEqualsHashCode() throws IllegalNotationException {
    // Empty board test
    GenericBoard board1 = new GenericBoard();
    GenericBoard board2 = new GenericBoard();
    assertEquals(board1, board2);
    assertEquals(board1.hashCode(), board2.hashCode());

    // Standard setup test
    board1 = new GenericBoard(GenericBoard.STANDARDSETUP);
    board2 = new GenericBoard(GenericBoard.STANDARDSETUP);

    // reflexive test
    assertEquals(board1, board1);

    // symmetric test
    assertEquals(board1, board2);
    assertEquals(board2, board1);

    assertEquals(board1.hashCode(), board2.hashCode());

    // null value test
    assertFalse(board1.equals(null));

    // inheritance test
    assertFalse(board1.equals(new Object()));

    // FEN test
    GenericBoard board3 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    assertEquals(board1, board3);
    assertEquals(board1.hashCode(), board3.hashCode());

    GenericBoard board4 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
    GenericBoard board5 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
    GenericBoard board6 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1");
    GenericBoard board7 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 1");
    GenericBoard board8 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 2");

    assertFalse(board1.equals(board4));
    assertFalse(board1.equals(board5));
    assertFalse(board1.equals(board6));
    assertFalse(board1.equals(board7));
    assertFalse(board1.equals(board8));
  }

  @Test(expected = IllegalNotationException.class)
  public void testInvalidEnPassant() throws IllegalNotationException {
    GenericBoard board = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq x9 0 1");
  }

}
