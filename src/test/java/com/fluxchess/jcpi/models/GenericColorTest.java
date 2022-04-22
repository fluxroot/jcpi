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

public class GenericColorTest {

	@Test
	public final void testValueOf() {
		assertThat(GenericColor.valueOf('w')).isEqualTo(GenericColor.WHITE);
		assertThat(GenericColor.valueOf('b')).isEqualTo(GenericColor.BLACK);
	}

	@Test
	public final void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> GenericColor.valueOf('a'));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public final void testIsValid() {
		assertThat(GenericColor.isValid('a')).isFalse();
	}

	@Test
	public final void testColorOf() {
		assertThat(GenericColor.colorOf('A')).isEqualTo(GenericColor.WHITE);
		assertThat(GenericColor.colorOf('a')).isEqualTo(GenericColor.BLACK);
	}

	@Test
	public final void testTransform() {
		assertThat(GenericColor.WHITE.transform('A')).isEqualTo('A');
		assertThat(GenericColor.WHITE.transform('a')).isEqualTo('A');
		assertThat(GenericColor.BLACK.transform('A')).isEqualTo('a');
	}

	@Test
	public final void testOpposite() {
		assertThat(GenericColor.BLACK.opposite()).isEqualTo(GenericColor.WHITE);
		assertThat(GenericColor.WHITE.opposite()).isEqualTo(GenericColor.BLACK);
	}

	@Test
	public final void testToChar() {
		assertThat(GenericColor.WHITE.toChar()).isEqualTo('w');
		assertThat(GenericColor.BLACK.toChar()).isEqualTo('b');
	}

}
