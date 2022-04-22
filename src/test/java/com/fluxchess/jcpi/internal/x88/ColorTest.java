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

import com.fluxchess.jcpi.models.GenericColor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ColorTest {

	@Test
	public void testValues() {
		for (GenericColor genericColor : GenericColor.values()) {
			assertThat(Color.toGenericColor(Color.valueOf(genericColor))).isEqualTo(genericColor);
			assertThat(Color.valueOf(genericColor)).isEqualTo(genericColor.ordinal());
			assertThat(Color.values[Color.valueOf(genericColor)]).isEqualTo(Color.valueOf(genericColor));
		}
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> Color.valueOf(null));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testInvalidToGenericColor() {
		Throwable thrown = catchThrowable(() -> Color.toGenericColor(Color.NOCOLOR));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		for (int color : Color.values) {
			assertThat(Color.isValid(color)).isTrue();
			assertThat(color & Color.MASK).isEqualTo(color);
		}

		assertThat(Color.isValid(Color.NOCOLOR)).isFalse();
	}

	@Test
	public void testInvalidIsValid() {
		Throwable thrown = catchThrowable(() -> Color.isValid(-1));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testOpposite() {
		assertThat(Color.opposite(Color.BLACK)).isEqualTo(Color.WHITE);
		assertThat(Color.opposite(Color.WHITE)).isEqualTo(Color.BLACK);
	}

	@Test
	public void testInvalidOpposite() {
		Throwable thrown = catchThrowable(() -> Color.opposite(Color.NOCOLOR));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
