/*
 * Copyright 2007-2015 the original author or authors.
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

public enum GenericColor {

  WHITE('w'),
  BLACK('b');

  private final char token;

  private GenericColor(char token) {
    this.token = token;
  }

  private static GenericColor _valueOf(char token) {
    for (GenericColor color : values()) {
      if (Character.toLowerCase(token) == Character.toLowerCase(color.token)) {
        return color;
      }
    }

    return null;
  }

  public static GenericColor valueOf(char token) {
    GenericColor color = _valueOf(token);
    if (color != null) {
      return color;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(char token) {
    return _valueOf(token) != null;
  }

  public static GenericColor colorOf(char c) {
    if (Character.isLowerCase(c)) {
      return BLACK;
    } else {
      return WHITE;
    }
  }

  public char transform(char c) {
    if (this == WHITE) {
      return Character.toUpperCase(c);
    } else {
      assert this == BLACK;

      return Character.toLowerCase(c);
    }
  }

  public GenericColor opposite() {
    if (this == WHITE) {
      return BLACK;
    } else {
      assert this == BLACK;

      return WHITE;
    }
  }

  public char toChar() {
    return this.token;
  }

}
