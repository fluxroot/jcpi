/*
 * Copyright 2007-2019 The Java Chess Protocol Interface Project Authors
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
package com.fluxchess.jcpi.utils;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveGeneratorTest {

	@Test
	public void testGetGenericMoves() {
		GenericMove[] genericMoves = MoveGenerator.getGenericMoves(new GenericBoard(GenericBoard.STANDARDSETUP));

		assertThat(genericMoves.length).isEqualTo(20);
	}

	@Test
	public void testPerft() {
		long result = MoveGenerator.perft(new GenericBoard(GenericBoard.STANDARDSETUP), 4);

		assertThat(result).isEqualTo(197281);
	}

}
