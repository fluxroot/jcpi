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

import com.fluxchess.jcpi.models.GenericFile;

/**
 * This class encodes file information as an int value. The data is
 * encoded as follows:<br/>
 * <br/>
 * <code>Bit 0 - 3</code>: the file (required)<br/>
 */
final class File {

	public static final int MASK = 0xF;

	public static final int a = 0;
	public static final int b = 1;
	public static final int c = 2;
	public static final int d = 3;
	public static final int e = 4;
	public static final int f = 5;
	public static final int g = 6;
	public static final int h = 7;
	public static final int NOFILE = 8;

	public static final int[] values = {
			a, b, c, d, e, f, g, h
	};

	private File() {
	}

	public static int valueOf(GenericFile genericFile) {
		if (genericFile == null) throw new IllegalArgumentException();

		switch (genericFile) {
			case a:
				return a;
			case b:
				return b;
			case c:
				return c;
			case d:
				return d;
			case e:
				return e;
			case f:
				return f;
			case g:
				return g;
			case h:
				return h;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static GenericFile toGenericFile(int file) {
		switch (file) {
			case a:
				return GenericFile.a;
			case b:
				return GenericFile.b;
			case c:
				return GenericFile.c;
			case d:
				return GenericFile.d;
			case e:
				return GenericFile.e;
			case f:
				return GenericFile.f;
			case g:
				return GenericFile.g;
			case h:
				return GenericFile.h;
			case NOFILE:
			default:
				throw new IllegalArgumentException();
		}
	}

	public static boolean isValid(int file) {
		switch (file) {
			case a:
			case b:
			case c:
			case d:
			case e:
			case f:
			case g:
			case h:
				return true;
			case NOFILE:
				return false;
			default:
				throw new IllegalArgumentException();
		}
	}

}
