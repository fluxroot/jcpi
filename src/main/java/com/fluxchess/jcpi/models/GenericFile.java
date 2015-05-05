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

public enum GenericFile {

  Fa('a'),
  Fb('b'),
  Fc('c'),
  Fd('d'),
  Fe('e'),
  Ff('f'),
  Fg('g'),
  Fh('h');

  private final char token;

  private GenericFile(char token) {
    this.token = token;
  }

  private static GenericFile _valueOf(char token) {
    for (GenericFile file : values()) {
      if (Character.toLowerCase(token) == Character.toLowerCase(file.token)) {
        return file;
      }
    }

    return null;
  }

  public static GenericFile valueOf(char token) {
    GenericFile file = _valueOf(token);
    if (file != null) {
      return file;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(char token) {
    return _valueOf(token) != null;
  }

  private GenericFile _prev(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() - i;
    if (position >= 0) {
      return values()[position];
    } else {
      return null;
    }
  }

  public GenericFile prev() {
    return prev(1);
  }

  public GenericFile prev(int i) {
    GenericFile file = _prev(i);
    if (file != null) {
      return file;
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

  private GenericFile _next(int i) {
    if (i < 0) throw new IllegalArgumentException();

    int position = this.ordinal() + i;
    if (position < values().length) {
      return values()[position];
    } else {
      return null;
    }
  }

  public GenericFile next() {
    return next(1);
  }

  public GenericFile next(int i) {
    GenericFile file = _next(i);
    if (file != null) {
      return file;
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
