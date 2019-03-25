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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericPosition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SquareTest {

	@Test
	public void testValues() {
		for (GenericPosition genericPosition : GenericPosition.values()) {
			int square = Square.valueOf(genericPosition);
			assertThat(Square.isLegal(square)).isTrue();
			assertThat(Square.isValid(square)).isTrue();
			assertThat(Square.toGenericPosition(square)).isEqualTo(genericPosition);

			int file = Square.getFile(square);
			assertThat(File.isValid(file)).isTrue();
			assertThat(file).isEqualTo(genericPosition.file.ordinal());
			assertThat(File.toGenericFile(file)).isEqualTo(genericPosition.file);

			int rank = Square.getRank(square);
			assertThat(Rank.isValid(rank)).isTrue();
			assertThat(rank).isEqualTo(genericPosition.rank.ordinal());
			assertThat(Rank.toGenericRank(rank)).isEqualTo(genericPosition.rank);
		}

		assertThat(Square.isLegal(Square.NOSQUARE)).isFalse();
		assertThat(Square.isValid(Square.NOSQUARE)).isFalse();
	}

	@Test
	public void testX88Squares() {
		int bitSquare = 0;
		for (int x88Square : Square.values) {
			assertThat(Square.toBitSquare(x88Square)).isEqualTo(bitSquare);
			assertThat(Square.toX88Square(bitSquare)).isEqualTo(x88Square);
			++bitSquare;
		}
	}

}
