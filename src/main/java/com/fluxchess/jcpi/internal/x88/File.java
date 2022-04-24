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

/**
 * This class encodes file information as an int value. The data is
 * encoded as follows:<br/>
 * <br/>
 * <code>Bit 0 - 3</code>: the file (required)<br/>
 */
final class File {

	public static final int MASK = 0xF;

	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;
	public static final int E = 4;
	public static final int F = 5;
	public static final int G = 6;
	public static final int H = 7;
	public static final int NOFILE = 8;

	public static final int[] values = {
			A, B, C, D, E, F, G, H
	};

	private File() {
	}

	public static int valueOf(GenericFile genericFile) {
		if (genericFile == null) throw new IllegalArgumentException();

		switch (genericFile) {
			case A:
				return A;
			case B:
				return B;
			case C:
				return C;
			case D:
				return D;
			case E:
				return E;
			case F:
				return F;
			case G:
				return G;
			case H:
				return H;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static GenericFile toGenericFile(int file) {
		switch (file) {
			case A:
				return GenericFile.A;
			case B:
				return GenericFile.B;
			case C:
				return GenericFile.C;
			case D:
				return GenericFile.D;
			case E:
				return GenericFile.E;
			case F:
				return GenericFile.F;
			case G:
				return GenericFile.G;
			case H:
				return GenericFile.H;
			case NOFILE:
			default:
				throw new IllegalArgumentException();
		}
	}

	public static boolean isValid(int file) {
		switch (file) {
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
			case G:
			case H:
				return true;
			case NOFILE:
				return false;
			default:
				throw new IllegalArgumentException();
		}
	}

}
