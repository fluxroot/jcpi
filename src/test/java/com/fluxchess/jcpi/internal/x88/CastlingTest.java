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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericCastling;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class CastlingTest {

	@Test
	void testValues() {
		for (GenericCastling genericCastling : GenericCastling.values()) {
			assertThat(Castling.toGenericCastling(Castling.valueOf(genericCastling))).isEqualTo(genericCastling);
			assertThat(Castling.valueOf(genericCastling)).isEqualTo(genericCastling.ordinal());
			assertThat(Castling.values[Castling.valueOf(genericCastling)]).isEqualTo(Castling.valueOf(genericCastling));
		}
	}

	@Test
	void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> Castling.valueOf(null));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testInvalidToGenericCastling() {
		Throwable thrown = catchThrowable(() -> Castling.toGenericCastling(Castling.NOCASTLING));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testIsValid() {
		for (int castling : Castling.values) {
			assertThat(Castling.isValid(castling)).isTrue();
			assertThat(castling & Castling.MASK).isEqualTo(castling);
		}

		assertThat(Castling.isValid(Castling.NOCASTLING)).isFalse();
	}

	@Test
	void testInvalidIsValid() {
		Throwable thrown = catchThrowable(() -> Castling.isValid(-1));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
