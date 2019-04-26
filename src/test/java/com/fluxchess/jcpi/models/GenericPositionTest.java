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
package com.fluxchess.jcpi.models;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static com.fluxchess.jcpi.models.GenericFile.a;
import static com.fluxchess.jcpi.models.GenericPosition.a1;
import static com.fluxchess.jcpi.models.GenericRank._1;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

class GenericPositionTest {

	@Test
	void valuesShouldContainCorrectFileAndRank() {
		Iterator<GenericPosition> iter = stream(GenericPosition.values()).iterator();
		for (GenericRank rank : GenericRank.values()) {
			for (GenericFile file : GenericFile.values()) {
				GenericPosition position = iter.next();

				assertThat(position.file).isEqualTo(file);
				assertThat(position.rank).isEqualTo(rank);
			}
		}
	}

	@Test
	void valueOfFileAndRankShouldReturnCorrectPosition() {
		assertThat(GenericPosition.of(a, _1)).isEqualTo(a1);
	}

	@Test
	void toStringShouldReturnCorrectString() {
		assertThat(a1.toString()).isEqualTo("a1");
	}

}
