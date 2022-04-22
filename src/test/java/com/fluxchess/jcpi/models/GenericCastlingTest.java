/*
 * Copyright 2007-2022 The Java Chess Protocol Interface Project Authors
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class GenericCastlingTest {

	@Test
	public final void testValueOf() {
		assertThat(GenericCastling.valueOf('k')).isEqualTo(GenericCastling.KINGSIDE);
		assertThat(GenericCastling.valueOf('K')).isEqualTo(GenericCastling.KINGSIDE);
	}

	@Test
	public final void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> GenericCastling.valueOf('a'));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public final void testIsValid() {
		assertThat(GenericCastling.isValid('a')).isFalse();
	}

	@Test
	public final void testValueOfLongToken() {
		assertThat(GenericCastling.valueOfLongToken("o-o")).isEqualTo(GenericCastling.KINGSIDE);
		assertThat(GenericCastling.valueOfLongToken("O-O")).isEqualTo(GenericCastling.KINGSIDE);
	}

	@Test
	public final void testInvalidValueOfLongToken() {
		Throwable thrown = catchThrowable(() -> GenericCastling.valueOfLongToken("x-x"));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public final void testIsValidLongToken() {
		assertThat(GenericCastling.isValidLongToken("x-x")).isFalse();
	}

	@Test
	public final void testToLongToken() {
		assertThat(GenericCastling.KINGSIDE.toLongToken()).isEqualTo("O-O");
	}

	@Test
	public final void testToChar() {
		assertThat(GenericCastling.KINGSIDE.toChar(GenericColor.BLACK)).isEqualTo('k');
	}

}
