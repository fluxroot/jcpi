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

import java.util.EnumSet;

public enum GenericChessman {

  PAWN('P'),
  KNIGHT('N'),
  BISHOP('B'),
  ROOK('R'),
  QUEEN('Q'),
  KING('K');

  public static final EnumSet<GenericChessman> promotions = EnumSet.of(KNIGHT, BISHOP, ROOK, QUEEN);
  public static final EnumSet<GenericChessman> sliders = EnumSet.of(BISHOP, ROOK, QUEEN);

  private final char token;

  private GenericChessman(char token) {
    this.token = token;
  }

  private static GenericChessman _valueOf(char token) {
    for (GenericChessman chessman : values()) {
      if (Character.toLowerCase(token) == Character.toLowerCase(chessman.token)) {
        return chessman;
      }
    }

    return null;
  }

  public static GenericChessman valueOf(char token) {
    GenericChessman chessman = _valueOf(token);
    if (chessman != null) {
      return chessman;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(char token) {
    return _valueOf(token) != null;
  }

  private static GenericChessman _valueOfPromotion(char token) {
    for (GenericChessman chessman : promotions) {
      if (Character.toLowerCase(token) == Character.toLowerCase(chessman.token)) {
        return chessman;
      }
    }

    return null;
  }

  public static GenericChessman valueOfPromotion(char token) {
    GenericChessman chessman = _valueOfPromotion(token);
    if (chessman != null) {
      return chessman;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isValidPromotion(char token) {
    return _valueOfPromotion(token) != null;
  }

  public boolean isLegalPromotion() {
    return promotions.contains(this);
  }

  public boolean isSliding() {
    return sliders.contains(this);
  }

  public char toCharAlgebraic() {
    if (this == PAWN) {
      throw new UnsupportedOperationException();
    }

    return this.token;
  }

  public char toChar(GenericColor color) {
    if (color == null) throw new IllegalArgumentException();

    return color.transform(this.token);
  }

}
