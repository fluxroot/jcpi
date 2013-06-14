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
import jcpi.data.GenericChessman;
import jcpi.data.GenericMove;
import jcpi.data.GenericPosition;
import jcpi.data.IllegalNotationException;

import org.junit.Test;

public class GenericMoveTest {

    @Test
    public void testParse() {
        try {
            GenericMove move = new GenericMove("a1 e3");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertNull(move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }

        try {
            GenericMove move = new GenericMove("a1 x e3");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertNull(move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }

        try {
            GenericMove move = new GenericMove("a1 : e3");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertNull(move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }

        try {
            GenericMove move = new GenericMove("a1 x e3 = q");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertEquals(GenericChessman.QUEEN, move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }

        try {
            GenericMove move = new GenericMove("a1 x e3 = q #");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertEquals(GenericChessman.QUEEN, move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }

        try {
            GenericMove move = new GenericMove("a1 - e3 = q");
            assertEquals(GenericPosition.a1, move.from);
            assertEquals(GenericPosition.e3, move.to);
            assertEquals(GenericChessman.QUEEN, move.promotion);
        } catch (IllegalNotationException e) {
            fail();
        }
    }

}
