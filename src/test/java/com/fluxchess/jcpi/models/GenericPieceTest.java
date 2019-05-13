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
import static com.fluxchess.jcpi.models.GenericColor.WHITE;
import static com.fluxchess.jcpi.models.GenericPiece.*;
import static org.assertj.core.api.Assertions.assertThat;

class GenericPieceTest {

	@Test
	public void valueOfColorAndChessmanShouldReturnCorrectPiece() {
		assertThat(GenericPiece.of(WHITE, PAWN)).isEqualTo(WHITEPAWN);
	}

	@Test
	public void valueFromLowercaseTokenShouldReturnCorrectPiece() {
		assertThat(GenericPiece.from('p')).hasValue(BLACKPAWN);
	}

	@Test
	public void valueFromUppercaseTokenShouldReturnCorrectPiece() {
		assertThat(GenericPiece.from('P')).hasValue(WHITEPAWN);
	}

	@Test
	public void valueFromInvalidTokenShouldReturnNoChessman() {
		assertThat(GenericPiece.from('a')).isEmpty();
	}

	@Test
	public void toNotationShouldReturnLowercaseNotationForBlackPiece() {
		assertThat(BLACKQUEEN.toNotation()).isEqualTo("q");
	}

	@Test
	public void toNotationShouldReturnUppercaseNotationForWhitePiece() {
		assertThat(WHITEQUEEN.toNotation()).isEqualTo("Q");
	}

}
