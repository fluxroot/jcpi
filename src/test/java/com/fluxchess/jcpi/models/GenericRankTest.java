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

import static com.fluxchess.jcpi.models.GenericRank.*;
import static org.assertj.core.api.Assertions.assertThat;

class GenericRankTest {

	@Test
	void valueOfCharShouldReturnCorrectRank() {
		assertThat(GenericRank.of('1')).hasValue(_1);
	}

	@Test
	void valueOfInvalidCharShouldReturnNoRank() {
		assertThat(GenericRank.of('i')).isEmpty();
	}

	@Test
	void prevShouldReturnCorrectRankIfExists() {
		assertThat(_8.prev()).hasValue(_7);
	}

	@Test
	void prevWithNumberShouldReturnCorrectRankIfExists() {
		assertThat(_8.prev(7)).hasValue(_1);
	}

	@Test
	void prevShouldReturnNoRankIfNotExists() {
		assertThat(_1.prev()).isEmpty();
	}

	@Test
	void prevWithNumberShouldReturnNoRankIfNotExists() {
		assertThat(_7.prev(7)).isEmpty();
	}

	@Test
	void prevWithNumberShouldReturnNoRankIfNumberIsInvalid() {
		assertThat(_7.prev(-1)).isEmpty();
	}

	@Test
	void nextShouldReturnCorrectRankIfExists() {
		assertThat(_1.next()).hasValue(_2);
	}

	@Test
	void nextWithNumberShouldReturnCorrectRankIfExists() {
		assertThat(_1.next(7)).hasValue(_8);
	}

	@Test
	void nextShouldReturnNoRankIfNotExists() {
		assertThat(_8.next()).isEmpty();
	}

	@Test
	void nextWithNumberShouldReturnNoRankIfNotExists() {
		assertThat(_2.next(7)).isEmpty();
	}

	@Test
	void nextWithNumberShouldReturnNoRankIfNumberIsInvalid() {
		assertThat(_2.next(-1)).isEmpty();
	}

	@Test
	void toCharShouldReturnTokenAsCharacter() {
		assertThat(_1.toChar()).isEqualTo('1');
	}

	@Test
	void toStringShouldReturnTokenAsString() {
		assertThat(_1.toString()).isEqualTo("1");
	}

}
