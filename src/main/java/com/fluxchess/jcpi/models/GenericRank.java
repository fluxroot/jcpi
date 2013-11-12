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

public enum GenericRank {

  R1('1'),
  R2('2'),
  R3('3'),
  R4('4'),
  R5('5'),
  R6('6'),
  R7('7'),
  R8('8');

  private final char token;

  private GenericRank(char token) {
    this.token = token;
  }

  public static GenericRank valueOf(char input) {
    for (GenericRank rank : values()) {
      if (Character.toLowerCase(input) == Character.toLowerCase(rank.token)) {
        return rank;
      }
    }

    return null;
  }

  public GenericRank prev() {
    return prev(1);
  }

  public GenericRank prev(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() - i;
    if (position >= 0) {
      return values()[position];
    } else {
      return null;
    }
  }

  public GenericRank next() {
    return next(1);
  }

  public GenericRank next(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() + i;
    if (position < values().length) {
      return values()[position];
    } else {
      return null;
    }
  }

  public char toChar() {
    return this.token;
  }

  public String toString() {
    return Character.toString(this.token);
  }

}
