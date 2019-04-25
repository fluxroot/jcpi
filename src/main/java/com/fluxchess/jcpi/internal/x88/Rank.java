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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericRank;

/**
 * This class encodes rank information as an int value. The data is
 * encoded as follows:<br/>
 * <br/>
 * <code>Bit 0 - 3</code>: the rank (required)<br/>
 */
final class Rank {

	public static final int MASK = 0xF;

	public static final int _1 = 0;
	public static final int _2 = 1;
	public static final int _3 = 2;
	public static final int _4 = 3;
	public static final int _5 = 4;
	public static final int _6 = 5;
	public static final int _7 = 6;
	public static final int _8 = 7;
	public static final int NORANK = 8;

	public static final int[] values = {
			_1, _2, _3, _4, _5, _6, _7, _8
	};

	private Rank() {
	}

	public static int valueOf(GenericRank genericRank) {
		if (genericRank == null) throw new IllegalArgumentException();

		switch (genericRank) {
			case _1:
				return _1;
			case _2:
				return _2;
			case _3:
				return _3;
			case _4:
				return _4;
			case _5:
				return _5;
			case _6:
				return _6;
			case _7:
				return _7;
			case _8:
				return _8;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static GenericRank toGenericRank(int rank) {
		switch (rank) {
			case _1:
				return GenericRank._1;
			case _2:
				return GenericRank._2;
			case _3:
				return GenericRank._3;
			case _4:
				return GenericRank._4;
			case _5:
				return GenericRank._5;
			case _6:
				return GenericRank._6;
			case _7:
				return GenericRank._7;
			case _8:
				return GenericRank._8;
			case NORANK:
			default:
				throw new IllegalArgumentException();
		}
	}

	public static boolean isValid(int rank) {
		switch (rank) {
			case _1:
			case _2:
			case _3:
			case _4:
			case _5:
			case _6:
			case _7:
			case _8:
				return true;
			case NORANK:
				return false;
			default:
				throw new IllegalArgumentException();
		}
	}

}
