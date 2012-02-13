/**
 * GenericChessmanTest.java
 * 
 * Copyright 2007 Java Chess Protocol Interface Project
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
package jcpi.data;

import static org.junit.Assert.*;
import jcpi.data.GenericChessman;


import org.junit.Test;

/**
 * GenericChessmanTest
 *
 * @author Phokham Nonava
 */
public class GenericChessmanTest {

	@Test
	public void testValueOf() {
		assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('q'));
		assertEquals(GenericChessman.QUEEN, GenericChessman.valueOf('Q'));
		assertNull(GenericChessman.valueOf('x'));
	}

	@Test
	public void testGetPromotionChessman() {
		assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('q'));
		assertEquals(GenericChessman.QUEEN, GenericChessman.valueOfPromotion('Q'));
		assertNull(GenericChessman.valueOfPromotion('p'));
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

}
