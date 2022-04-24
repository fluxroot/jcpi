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

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class GenericFileTest {

	@Test
	public void testValueOf() {
		assertThat(GenericFile.valueOf('a')).isEqualTo(GenericFile.A);
		assertThat(GenericFile.valueOf('A')).isEqualTo(GenericFile.A);
	}

	@Test
	public void testInvalidValueOf() {
		Throwable thrown = catchThrowable(() -> GenericFile.valueOf('i'));
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testIsValid() {
		assertThat(GenericFile.isValid('i')).isFalse();
	}

	@Test
	public void testPrev() {
		assertThat(GenericFile.H.prev()).isEqualTo(GenericFile.G);
		assertThat(GenericFile.H.prev(7)).isEqualTo(GenericFile.A);
	}

	@Test
	public void testIllegalPrev1() {
		Throwable thrown = catchThrowable(() -> GenericFile.A.prev());
		assertThat(thrown).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void testIllegalPrev2() {
		Throwable thrown = catchThrowable(() -> GenericFile.G.prev(7));
		assertThat(thrown).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void testHasPrev1() {
		assertThat(GenericFile.A.hasPrev()).isFalse();
	}

	@Test
	public void testHasPrev2() {
		assertThat(GenericFile.G.hasPrev(7)).isFalse();
	}

	@Test
	public void testNext() {
		assertThat(GenericFile.A.next()).isEqualTo(GenericFile.B);
		assertThat(GenericFile.A.next(7)).isEqualTo(GenericFile.H);
	}

	@Test
	public void testIllegalNext1() {
		Throwable thrown = catchThrowable(() -> GenericFile.H.next());
		assertThat(thrown).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void testIllegalNext2() {
		Throwable thrown = catchThrowable(() -> GenericFile.B.next(7));
		assertThat(thrown).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void testHasNext1() {
		assertThat(GenericFile.H.hasNext()).isFalse();
	}

	@Test
	public void testHasNext2() {
		assertThat(GenericFile.B.hasNext(7)).isFalse();
	}

	@Test
	public void testToChar() {
		assertThat(GenericFile.A.toChar()).isEqualTo('a');
	}

	@Test
	public void testToString() {
		assertThat(GenericFile.A.toString()).isEqualTo("a");
	}

}
