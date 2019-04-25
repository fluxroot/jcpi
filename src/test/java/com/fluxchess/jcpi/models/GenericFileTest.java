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

import static com.fluxchess.jcpi.models.GenericFile.*;
import static org.assertj.core.api.Assertions.assertThat;

class GenericFileTest {

	@Test
	void allTokensShouldBeLowercase() {
		for (GenericFile file : GenericFile.values()) {
			assertThat(file.toChar()).isLowerCase();
		}
	}

	@Test
	void valueOfLowercaseCharShouldReturnCorrectFile() {
		assertThat(GenericFile.of('a')).hasValue(a);
	}

	@Test
	void valueOfUppercaseCharShouldReturnCorrectFile() {
		assertThat(GenericFile.of('A')).hasValue(a);
	}

	@Test
	void valueOfInvalidCharShouldReturnNoFile() {
		assertThat(GenericFile.of('i')).isEmpty();
	}

	@Test
	void prevShouldReturnCorrectFileIfExists() {
		assertThat(h.prev()).hasValue(g);
	}

	@Test
	void prevWithNumberShouldReturnCorrectFileIfExists() {
		assertThat(h.prev(7)).hasValue(a);
	}

	@Test
	void prevShouldReturnNoFileIfNotExists() {
		assertThat(a.prev()).isEmpty();
	}

	@Test
	void prevWithNumberShouldReturnNoFileIfNotExists() {
		assertThat(g.prev(7)).isEmpty();
	}

	@Test
	void prevWithNumberShouldReturnNoFileIfNumberIsInvalid() {
		assertThat(g.prev(-1)).isEmpty();
	}

	@Test
	void nextShouldReturnCorrectFileIfExists() {
		assertThat(a.next()).hasValue(b);
	}

	@Test
	void nextWithNumberShouldReturnCorrectFileIfExists() {
		assertThat(a.next(7)).hasValue(h);
	}

	@Test
	void nextShouldReturnNoFileIfNotExists() {
		assertThat(h.next()).isEmpty();
	}

	@Test
	void nextWithNumberShouldReturnNoFileIfNotExists() {
		assertThat(b.next(7)).isEmpty();
	}

	@Test
	void nextWithNumberShouldReturnNoFileIfNumberIsInvalid() {
		assertThat(b.next(-1)).isEmpty();
	}

	@Test
	void toCharShouldReturnTokenAsCharacter() {
		assertThat(a.toChar()).isEqualTo('a');
	}

	@Test
	void toStringShouldReturnTokenAsString() {
		assertThat(a.toString()).isEqualTo("a");
	}

}
