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

import com.fluxchess.jcpi.models.GenericFile;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FileTest {

	@Test
	public void testValues() {
		for (GenericFile genericFile : GenericFile.values()) {
			assertThat(File.toGenericFile(File.valueOf(genericFile))).isEqualTo(genericFile);
			assertThat(File.valueOf(genericFile)).isEqualTo(genericFile.ordinal());
			assertThat(File.values[File.valueOf(genericFile)]).isEqualTo(File.valueOf(genericFile));
		}
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> File.valueOf(null));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testInvalidToGenericFile() {
		Throwable thrown = catchThrowable(() -> File.toGenericFile(File.NOFILE));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		for (int file : File.values) {
			assertThat(File.isValid(file)).isTrue();
			assertThat(file & File.MASK).isEqualTo(file);
		}

		assertThat(File.isValid(File.NOFILE)).isFalse();
	}

	@Test
	public void testInvalidIsValid() {
		Throwable thrown = catchThrowable(() -> File.isValid(-1));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

}
