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

import java.util.NoSuchElementException;

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

  private static GenericRank _valueOf(char token) {
    for (GenericRank rank : values()) {
      if (Character.toLowerCase(token) == Character.toLowerCase(rank.token)) {
        return rank;
      }
    }

    return null;
  }

  public static GenericRank valueOf(char token) {
    GenericRank rank = _valueOf(token);
    if (rank != null) {
      return rank;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(char token) {
    return _valueOf(token) != null;
  }

  private GenericRank _prev(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() - i;
    if (position >= 0) {
      return values()[position];
    } else {
      return null;
    }
  }

  public GenericRank prev() {
    return prev(1);
  }

  public GenericRank prev(int i) {
    GenericRank rank = _prev(i);
    if (rank != null) {
      return rank;
    } else {
      throw new NoSuchElementException();
    }
  }

  public boolean hasPrev() {
    return hasPrev(1);
  }

  public boolean hasPrev(int i) {
    return _prev(i) != null;
  }

  private GenericRank _next(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() + i;
    if (position < values().length) {
      return values()[position];
    } else {
      return null;
    }
  }

  public GenericRank next() {
    return next(1);
  }

  public GenericRank next(int i) {
    GenericRank rank = _next(i);
    if (rank != null) {
      return rank;
    } else {
      throw new NoSuchElementException();
    }
  }

  public boolean hasNext() {
    return hasNext(1);
  }

  public boolean hasNext(int i) {
    return _next(i) != null;
  }

  public char toChar() {
    return this.token;
  }

  public String toString() {
    return Character.toString(this.token);
  }

}
