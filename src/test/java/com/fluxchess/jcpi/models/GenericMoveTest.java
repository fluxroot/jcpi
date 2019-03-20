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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class GenericMoveTest {

	@Test
	public void testParse() throws IllegalNotationException {
		GenericMove move = new GenericMove("a1 e3");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isNull();

		move = new GenericMove("a1 x e3");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isNull();

		move = new GenericMove("a1 : e3");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isNull();

		move = new GenericMove("a1 x e3 = q");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isEqualTo(GenericChessman.QUEEN);

		move = new GenericMove("a1 x e3 = q #");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isEqualTo(GenericChessman.QUEEN);

		move = new GenericMove("a1 - e3 = q");
		assertThat(move.from).isEqualTo(GenericPosition.a1);
		assertThat(move.to).isEqualTo(GenericPosition.e3);
		assertThat(move.promotion).isEqualTo(GenericChessman.QUEEN);
	}

	@Test
	public void testInvalidMove1() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericMove("a1 - e3 = z"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

	@Test
	public void testInvalidMove2() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericMove("n1 e3"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

	@Test
	public void testInvalidMove3() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericMove("a9 e3"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

	@Test
	public void testInvalidMove4() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericMove("a1 n3"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

	@Test
	public void testInvalidMove5() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericMove("a1 e9"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

	@Test
	public void testToString() {
		GenericMove move = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.ROOK);
		assertThat(move.toString()).isEqualTo("a1e3r");
	}

	@Test
	public void testEqualsHashCode() {
		GenericMove move1 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.QUEEN);
		GenericMove move2 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.QUEEN);

		GenericMove move3 = new GenericMove(GenericPosition.a1, GenericPosition.e3, GenericChessman.ROOK);
		GenericMove move4 = new GenericMove(GenericPosition.a1, GenericPosition.e4, GenericChessman.QUEEN);
		GenericMove move5 = new GenericMove(GenericPosition.a2, GenericPosition.e3, GenericChessman.QUEEN);
		GenericMove move6 = new GenericMove(GenericPosition.a1, GenericPosition.e3);

		// reflexive test
		assertThat(move1.equals(move1)).isTrue();

		// symmetric test
		assertThat(move1.equals(move2)).isTrue();
		assertThat(move2.equals(move1)).isTrue();

		// null value test
		assertThat(move1.equals(null)).isFalse();

		// inheritance test
		assertThat(move1.equals(new Object())).isFalse();

		// attributes test
		assertThat(move1.equals(move3)).isFalse();
		assertThat(move1.equals(move4)).isFalse();
		assertThat(move1.equals(move5)).isFalse();
		assertThat(move1.equals(move6)).isFalse();

		// hash code test
		assertThat(move2.hashCode()).isEqualTo(move1.hashCode());
		assertThat(move3.hashCode()).isNotEqualTo(move1.hashCode());
		assertThat(move4.hashCode()).isNotEqualTo(move1.hashCode());
		assertThat(move5.hashCode()).isNotEqualTo(move1.hashCode());
		assertThat(move6.hashCode()).isNotEqualTo(move1.hashCode());
	}

}
