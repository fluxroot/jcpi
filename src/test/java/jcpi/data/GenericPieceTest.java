/*
** Copyright 2007-2012 Phokham Nonava
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
package jcpi.data;

import static org.junit.Assert.*;

import org.junit.Test;

import jcpi.data.GenericChessman;

/**
 * GenericPieceTest
 *
 * @author Phokham Nonava
 */
public class GenericPieceTest {

    @Test
    public void testValueOf() {
        assertEquals(GenericPiece.WHITEPAWN, GenericPiece.valueOf(GenericColor.WHITE, GenericChessman.PAWN));
        assertEquals(GenericPiece.WHITEPAWN, GenericPiece.valueOf('P'));
        assertNull(GenericPiece.valueOf('x'));
    }

    @Test
    public void testToChar() {
        assertEquals('q', GenericPiece.BLACKQUEEN.toChar());
    }

}
