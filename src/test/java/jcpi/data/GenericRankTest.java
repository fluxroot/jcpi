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
import jcpi.data.GenericRank;


import org.junit.Test;

/**
 * GenericRankTest
 *
 * @author Phokham Nonava
 */
public class GenericRankTest {

    @Test
    public void testValueOf() {
        assertEquals(GenericRank.R1, GenericRank.valueOf('1'));
        assertNull(GenericRank.valueOf('9'));
    }

    @Test
    public void testPrev() {
        assertEquals(GenericRank.R7, GenericRank.R8.prev());
        assertNull(GenericRank.R1.prev());

        assertEquals(GenericRank.R1, GenericRank.R8.prev(7));
        assertNull(GenericRank.R7.prev(7));
    }

    @Test
    public void testNext() {
        assertEquals(GenericRank.R2, GenericRank.R1.next());
        assertNull(GenericRank.R8.next());

        assertEquals(GenericRank.R8, GenericRank.R1.next(7));
        assertNull(GenericRank.R2.next(7));
    }

    @Test
    public void testToChar() {
        assertEquals('1', GenericRank.R1.toChar());
    }

    @Test
    public void testToString() {
        assertEquals("1", GenericRank.R1.toString());
    }

}
