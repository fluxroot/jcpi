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

public class GenericBoardTest {

	@Test
	public void testToString() throws IllegalNotationException {
		GenericBoard board1 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertThat(board1.toString()).isEqualTo("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

		GenericBoard board2 = new GenericBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
		assertThat(board2.toString()).isEqualTo("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

		GenericBoard board3 = new GenericBoard("8/1n4N1/2k5/8/8/5K2/1N4n1/8 b - - 0 1");
		assertThat(board3.toString()).isEqualTo("8/1n4N1/2k5/8/8/5K2/1N4n1/8 b - - 0 1");

		GenericBoard board4 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
		assertThat(board4.toString()).isEqualTo("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

		GenericBoard board5 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3");
		assertThat(board5.toString()).isEqualTo("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

		GenericBoard board6 = new GenericBoard(328);
		assertThat(board6.toString()).isEqualTo("nbrqbkrn/pppppppp/8/8/8/8/PPPPPPPP/NBRQBKRN w GCgc - 0 1");
	}

	@Test
	public void testEqualsHashCode() throws IllegalNotationException {
		// Empty board test
		GenericBoard board1 = new GenericBoard();
		GenericBoard board2 = new GenericBoard();
		assertThat(board2).isEqualTo(board1);
		assertThat(board2.hashCode()).isEqualTo(board1.hashCode());

		// Standard setup test
		board1 = new GenericBoard(GenericBoard.STANDARDSETUP);
		board2 = new GenericBoard(GenericBoard.STANDARDSETUP);

		// reflexive test
		assertThat(board1).isEqualTo(board1);

		// symmetric test
		assertThat(board2).isEqualTo(board1);
		assertThat(board1).isEqualTo(board2);

		assertThat(board2.hashCode()).isEqualTo(board1.hashCode());

		// null value test
		assertThat(board1.equals(null)).isFalse();

		// inheritance test
		assertThat(board1.equals(new Object())).isFalse();

		// FEN test
		GenericBoard board3 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertThat(board3).isEqualTo(board1);
		assertThat(board3.hashCode()).isEqualTo(board1.hashCode());

		GenericBoard board4 = new GenericBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
		GenericBoard board5 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
		GenericBoard board6 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1");
		GenericBoard board7 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 1");
		GenericBoard board8 = new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 2");

		assertThat(board1.equals(board4)).isFalse();
		assertThat(board1.equals(board5)).isFalse();
		assertThat(board1.equals(board6)).isFalse();
		assertThat(board1.equals(board7)).isFalse();
		assertThat(board1.equals(board8)).isFalse();
	}

	@Test
	public void testInvalidEnPassant() throws IllegalNotationException {
		Throwable thrown = catchThrowable(() -> new GenericBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq x9 0 1"));
		assertThat(thrown).isInstanceOf(IllegalNotationException.class);
	}

}
