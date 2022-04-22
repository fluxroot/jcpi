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

public class GenericChessmanTest {

	@Test
	public void testValueOf() {
		assertThat(GenericChessman.valueOf('q')).isEqualTo(GenericChessman.QUEEN);
		assertThat(GenericChessman.valueOf('Q')).isEqualTo(GenericChessman.QUEEN);
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> GenericChessman.valueOf('x'));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		assertThat(GenericChessman.isValid('x')).isFalse();
	}

	@Test
	public void testValueOfPromotion() {
		assertThat(GenericChessman.valueOfPromotion('q')).isEqualTo(GenericChessman.QUEEN);
		assertThat(GenericChessman.valueOfPromotion('Q')).isEqualTo(GenericChessman.QUEEN);
	}

	@Test
	public void testInvalidValueOfPromotion() {
		Throwable thrown = catchThrowable(() -> GenericChessman.valueOfPromotion('p'));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValidPromotion() {
		assertThat(GenericChessman.isValidPromotion('p')).isFalse();
	}

	@Test
	public void testIsLegalPromotion() {
		assertThat(GenericChessman.QUEEN.isLegalPromotion()).isTrue();
		assertThat(GenericChessman.PAWN.isLegalPromotion()).isFalse();
	}

	@Test
	public void testIsSliding() {
		assertThat(GenericChessman.BISHOP.isSliding()).isTrue();
		assertThat(GenericChessman.ROOK.isSliding()).isTrue();
		assertThat(GenericChessman.QUEEN.isSliding()).isTrue();
		assertThat(GenericChessman.PAWN.isSliding()).isFalse();
		assertThat(GenericChessman.KNIGHT.isSliding()).isFalse();
		assertThat(GenericChessman.KING.isSliding()).isFalse();
	}

	@Test
	public void testToCharAlgebraic() {
		assertThat('Q').isEqualTo(GenericChessman.QUEEN.toCharAlgebraic());
	}

	@Test
	public void testInvalidToCharAlgebraic() {
		Throwable thrown = catchThrowable(() -> GenericChessman.PAWN.toCharAlgebraic());
		assertThat(thrown).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void testToChar() {
		assertThat(GenericChessman.QUEEN.toChar(GenericColor.BLACK)).isEqualTo('q');
	}

}
