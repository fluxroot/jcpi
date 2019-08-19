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

import static com.fluxchess.jcpi.models.GenericColor.BLACK;
import static com.fluxchess.jcpi.models.GenericColor.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

class GenericColorTest {

	@Test
	public void valueFromLowercaseTokenShouldReturnCorrectColor() {
		assertThat(GenericColor.from('w')).hasValue(WHITE);
	}

	@Test
	public void valueFromUppercaseTokenShouldReturnCorrectColor() {
		assertThat(GenericColor.from('W')).hasValue(WHITE);
	}

	@Test
	public void valueFromInvalidTokenShouldReturnNoColor() {
		assertThat(GenericColor.from('a')).isEmpty();
	}

	@Test
	public void colorOfUppercaseCharShouldReturnWhite() {
		assertThat(GenericColor.colorOf('A')).isEqualTo(WHITE);
	}

	@Test
	public void colorOfLowercaseCharShouldReturnBlack() {
		assertThat(GenericColor.colorOf('a')).isEqualTo(BLACK);
	}

	@Test
	public void transformingUppercaseCharToWhiteShouldReturnUppercaseChar() {
		assertThat(WHITE.transform('A')).isEqualTo('A');
	}

	@Test
	public void transformingLowercaseCharToWhiteShouldReturnUppercaseChar() {
		assertThat(WHITE.transform('a')).isEqualTo('A');
	}

	@Test
	public void transformingUppercaseCharToBlackShouldReturnLowercaseChar() {
		assertThat(BLACK.transform('A')).isEqualTo('a');
	}

	@Test
	public void oppositeOfWhiteShouldBeBlack() {
		assertThat(WHITE.opposite()).isEqualTo(BLACK);
	}

	@Test
	public void oppositeOfBlackShouldBeWhite() {
		assertThat(BLACK.opposite()).isEqualTo(WHITE);
	}

	@Test
	public void toNotationShouldReturnCorrectNotation() {
		assertThat(WHITE.toNotation()).isEqualTo("w");
	}

}
