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

public final class GenericMove {

  public final GenericPosition from;
  public final GenericPosition to;
  public final GenericChessman promotion;

  public GenericMove(GenericPosition from, GenericPosition to) {
    this(from, to, null);
  }

  public GenericMove(GenericPosition from, GenericPosition to, GenericChessman promotion) {
    if (from == null) throw new IllegalArgumentException();
    if (to == null) throw new IllegalArgumentException();
    if (promotion != null && !promotion.isLegalPromotion()) throw new IllegalArgumentException();

    this.from = from;
    this.to = to;
    this.promotion = promotion;
  }

  public GenericMove(String notation) throws IllegalNotationException {
    if (notation == null) throw new IllegalArgumentException();

    // Clean whitespace at the beginning and at the end
    notation = notation.trim();

    // Clean spaces in the notation
    notation = notation.replaceAll(" ", "");

    // Clean capturing notation
    notation = notation.replaceAll("x", "");
    notation = notation.replaceAll(":", "");

    // Clean pawn promotion notation
    notation = notation.replaceAll("=", "");

    // Clean check notation
    notation = notation.replaceAll("\\+", "");

    // Clean checkmate notation
    notation = notation.replaceAll("#", "");

    // Clean hyphen in long algebraic notation
    notation = notation.replaceAll("-", "");

    // Parse promotion
    if (notation.length() == 5) {
      if (GenericChessman.isValidPromotion(notation.charAt(4))) {
        this.promotion = GenericChessman.valueOfPromotion(notation.charAt(4));
      } else {
        throw new IllegalNotationException();
      }

      notation = notation.substring(0, 4);
    } else {
      this.promotion = null;
    }

    if (notation.length() == 4) {
      GenericFile file;
      if (GenericFile.isValid(notation.charAt(0))) {
        file = GenericFile.valueOf(notation.charAt(0));
      } else {
        throw new IllegalNotationException();
      }

      GenericRank rank;
      if (GenericRank.isValid(notation.charAt(1))) {
        rank = GenericRank.valueOf(notation.charAt(1));
      } else {
        throw new IllegalNotationException();
      }

      this.from = GenericPosition.valueOf(file, rank);

      if (GenericFile.isValid(notation.charAt(2))) {
        file = GenericFile.valueOf(notation.charAt(2));
      } else {
        throw new IllegalNotationException();
      }

      if (GenericRank.isValid(notation.charAt(3))) {
        rank = GenericRank.valueOf(notation.charAt(3));
      } else {
        throw new IllegalNotationException();
      }

      this.to = GenericPosition.valueOf(file, rank);
    } else {
      throw new IllegalNotationException();
    }
  }

  public String toString() {
    String result = this.from.toString() + this.to.toString();

    if (this.promotion != null) {
      result += Character.toLowerCase(this.promotion.toCharAlgebraic());
    }

    return result;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof GenericMove)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    GenericMove rhs = (GenericMove) obj;

    // Test from
    if (this.from != rhs.from) {
      return false;
    }

    // Test to
    if (this.to != rhs.to) {
      return false;
    }

    // Test promotion
    if (this.promotion != rhs.promotion) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = 31 * result + from.hashCode();

    result = 31 * result + to.hashCode();

    result = 31 * result + (promotion == null ? 0 : promotion.hashCode());

    return result;
  }

}
