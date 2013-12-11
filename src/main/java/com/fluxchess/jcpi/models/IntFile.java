/*
 * Copyright 2007-2013 the original author or authors.
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

/**
 * This class encodes file information as an int value. The data is
 * encoded as follows:<br/>
 * <br/>
 * <code>Bit 0 - 3</code>: the file (required)<br/>
 */
public final class IntFile {

  public static final int MASK = 0xF;

  public static final int NOFILE = 14;

  public static final int Fa = 4;
  public static final int Fb = 5;
  public static final int Fc = 6;
  public static final int Fd = 7;
  public static final int Fe = 8;
  public static final int Ff = 9;
  public static final int Fg = 10;
  public static final int Fh = 11;

  public static final int[] values = {
    Fa, Fb, Fc, Fd, Fe, Ff, Fg, Fh
  };

  private IntFile() {
  }

  public static int valueOf(GenericFile genericFile) {
    if (genericFile == null) throw new IllegalArgumentException();

    switch (genericFile) {
      case Fa:
        return Fa;
      case Fb:
        return Fb;
      case Fc:
        return Fc;
      case Fd:
        return Fd;
      case Fe:
        return Fe;
      case Ff:
        return Ff;
      case Fg:
        return Fg;
      case Fh:
        return Fh;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static GenericFile toGenericFile(int file) {
    switch (file) {
      case Fa:
        return GenericFile.Fa;
      case Fb:
        return GenericFile.Fb;
      case Fc:
        return GenericFile.Fc;
      case Fd:
        return GenericFile.Fd;
      case Fe:
        return GenericFile.Fe;
      case Ff:
        return GenericFile.Ff;
      case Fg:
        return GenericFile.Fg;
      case Fh:
        return GenericFile.Fh;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int ordinal(int file) {
    switch (file) {
      case Fa:
        return 0;
      case Fb:
        return 1;
      case Fc:
        return 2;
      case Fd:
        return 3;
      case Fe:
        return 4;
      case Ff:
        return 5;
      case Fg:
        return 6;
      case Fh:
        return 7;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(int file) {
    switch (file) {
      case Fa:
      case Fb:
      case Fc:
      case Fd:
      case Fe:
      case Ff:
      case Fg:
      case Fh:
        return true;
      default:
        return false;
    }
  }

}
