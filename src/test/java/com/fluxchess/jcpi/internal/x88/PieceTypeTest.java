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

import com.fluxchess.jcpi.models.GenericChessman;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PieceTypeTest {

	@Test
	public void testValues() {
		for (GenericChessman genericChessman : GenericChessman.values()) {
			assertThat(PieceType.toGenericChessman(PieceType.valueOf(genericChessman))).isEqualTo(genericChessman);
			assertThat(PieceType.valueOf(genericChessman)).isEqualTo(genericChessman.ordinal());
			assertThat(PieceType.values[PieceType.valueOf(genericChessman)]).isEqualTo(PieceType.valueOf(genericChessman));
		}
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> PieceType.valueOf(null));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testInvalidToGenericChessman() {
		Throwable thrown = catchThrowable(() -> PieceType.toGenericChessman(PieceType.NOCHESSMAN));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testPromotionValues() {
		for (GenericChessman genericChessman : GenericChessman.promotions) {
			assertThat(PieceType.toGenericChessman(PieceType.valueOfPromotion(genericChessman))).isEqualTo(genericChessman);
		}
	}

	@Test
	public void testInvalidValueOfPromotion() {
		Throwable thrown = catchThrowable(() -> PieceType.valueOfPromotion(GenericChessman.PAWN));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		for (int chessman : PieceType.values) {
			assertThat(PieceType.isValid(chessman)).isTrue();
			assertThat(chessman & PieceType.MASK).isEqualTo(chessman);
		}

		assertThat(PieceType.isValid(PieceType.NOCHESSMAN)).isFalse();
	}

	@Test
	public void testInvalidIsValid() {
		Throwable thrown = catchThrowable(() -> PieceType.isValid(-1));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValidPromotion() {
		for (int chessman : PieceType.promotions) {
			assertThat(PieceType.isValidPromotion(chessman)).isTrue();
			assertThat(chessman & PieceType.MASK).isEqualTo(chessman);
		}

		assertThat(PieceType.isValidPromotion(PieceType.PAWN)).isFalse();
		assertThat(PieceType.isValidPromotion(PieceType.KING)).isFalse();
	}

	@Test
	public void testInvalidIsValidPromotion() {
		Throwable thrown = catchThrowable(() -> PieceType.isValidPromotion(PieceType.NOCHESSMAN));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsSliding() {
		assertThat(PieceType.isSliding(PieceType.BISHOP)).isTrue();
		assertThat(PieceType.isSliding(PieceType.ROOK)).isTrue();
		assertThat(PieceType.isSliding(PieceType.QUEEN)).isTrue();
		assertThat(PieceType.isSliding(PieceType.PAWN)).isFalse();
		assertThat(PieceType.isSliding(PieceType.KNIGHT)).isFalse();
		assertThat(PieceType.isSliding(PieceType.KING)).isFalse();
	}

	@Test
	public void testInvalidIsSliding() {
		Throwable thrown = catchThrowable(() -> PieceType.isSliding(PieceType.NOCHESSMAN));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
