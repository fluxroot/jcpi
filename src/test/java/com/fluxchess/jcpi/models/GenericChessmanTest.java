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

import static com.fluxchess.jcpi.models.GenericChessman.PAWN;
import static com.fluxchess.jcpi.models.GenericChessman.QUEEN;
import static com.fluxchess.jcpi.models.GenericColor.BLACK;
import static com.fluxchess.jcpi.models.GenericColor.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

class GenericChessmanTest {

	@Test
	public void valueFromLowercaseTokenShouldReturnCorrectChessman() {
		assertThat(GenericChessman.from('q')).hasValue(QUEEN);
	}

	@Test
	public void valueFromUppercaseTokenShouldReturnCorrectChessman() {
		assertThat(GenericChessman.from('Q')).hasValue(QUEEN);
	}

	@Test
	public void valueFromInvalidTokenShouldReturnNoChessman() {
		assertThat(GenericChessman.from('a')).isEmpty();
	}

	@Test
	public void valueFromLowercaseTokenForPromotionShouldReturnValidPromotedChessman() {
		assertThat(GenericChessman.promotionFrom('q')).hasValue(QUEEN);
	}

	@Test
	public void valueFromUppercaseTokenForPromotionShouldReturnValidPromotedChessman() {
		assertThat(GenericChessman.promotionFrom('Q')).hasValue(QUEEN);
	}

	@Test
	public void valueFromInvalidTokenForPromotionShouldReturnNoChessman() {
		assertThat(GenericChessman.promotionFrom('a')).isEmpty();
	}

	@Test
	public void isPromotionShouldReturnTrueForValidPromotedChessman() {
		assertThat(QUEEN.isPromotion()).isTrue();
	}

	@Test
	public void isPromotionShouldReturnFalseForInvalidPromotedChessman() {
		assertThat(PAWN.isPromotion()).isFalse();
	}

	@Test
	public void toAlgebraicCharShouldReturnToken() {
		assertThat(QUEEN.toAlgebraicChar()).hasValue('Q');
	}

	@Test
	public void toAlgebraicCharShouldReturnNoTokenForPawn() {
		assertThat(PAWN.toAlgebraicChar()).isEmpty();
	}

	@Test
	public void toNotationShouldReturnLowercaseNotationForBlack() {
		assertThat(QUEEN.toNotation(BLACK)).isEqualTo("q");
	}

	@Test
	public void toNotationShouldReturnUppercaseNotationForWhite() {
		assertThat(QUEEN.toNotation(WHITE)).isEqualTo("Q");
	}

}
