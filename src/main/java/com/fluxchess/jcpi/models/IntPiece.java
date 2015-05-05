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

/**
 * This class encodes piece information as an int value. The data is
 * encoded as follows:<br/>
 * <br/>
 * <code>Bit 0 - 2</code>: the chessman (required)<br/>
 * <code>Bit 3 - 4</code>: the color (required)<br/>
 */
public final class IntPiece {

  public static final int MASK = 0x1F;

  private static final int CHESSMAN_SHIFT = 0;
  private static final int CHESSMAN_MASK = IntChessman.MASK << CHESSMAN_SHIFT;
  private static final int COLOR_SHIFT = 3;
  private static final int COLOR_MASK = IntColor.MASK << COLOR_SHIFT;

  public static final int NOPIECE = (IntChessman.NOCHESSMAN << CHESSMAN_SHIFT) | (IntColor.NOCOLOR << COLOR_SHIFT);

  public static final int WHITEPAWN = (IntChessman.PAWN << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int WHITEKNIGHT = (IntChessman.KNIGHT << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int WHITEBISHOP = (IntChessman.BISHOP << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int WHITEROOK = (IntChessman.ROOK << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int WHITEQUEEN = (IntChessman.QUEEN << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int WHITEKING = (IntChessman.KING << CHESSMAN_SHIFT) | (IntColor.WHITE << COLOR_SHIFT);
  public static final int BLACKPAWN = (IntChessman.PAWN << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);
  public static final int BLACKKNIGHT = (IntChessman.KNIGHT << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);
  public static final int BLACKBISHOP = (IntChessman.BISHOP << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);
  public static final int BLACKROOK = (IntChessman.ROOK << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);
  public static final int BLACKQUEEN = (IntChessman.QUEEN << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);
  public static final int BLACKKING = (IntChessman.KING << CHESSMAN_SHIFT) | (IntColor.BLACK << COLOR_SHIFT);

  public static final int[] values = {
    WHITEPAWN, WHITEKNIGHT, WHITEBISHOP, WHITEROOK, WHITEQUEEN, WHITEKING,
    BLACKPAWN, BLACKKNIGHT, BLACKBISHOP, BLACKROOK, BLACKQUEEN, BLACKKING
  };

  private IntPiece() {
  }

  public static int valueOf(GenericPiece genericPiece) {
    if (genericPiece == null) throw new IllegalArgumentException();

    switch (genericPiece) {
      case WHITEPAWN:
        return WHITEPAWN;
      case WHITEKNIGHT:
        return WHITEKNIGHT;
      case WHITEBISHOP:
        return WHITEBISHOP;
      case WHITEROOK:
        return WHITEROOK;
      case WHITEQUEEN:
        return WHITEQUEEN;
      case WHITEKING:
        return WHITEKING;
      case BLACKPAWN:
        return BLACKPAWN;
      case BLACKKNIGHT:
        return BLACKKNIGHT;
      case BLACKBISHOP:
        return BLACKBISHOP;
      case BLACKROOK:
        return BLACKROOK;
      case BLACKQUEEN:
        return BLACKQUEEN;
      case BLACKKING:
        return BLACKKING;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int valueOf(int chessman, int color) {
    switch (color) {
      case IntColor.WHITE:
        switch (chessman) {
          case IntChessman.PAWN:
            return WHITEPAWN;
          case IntChessman.KNIGHT:
            return WHITEKNIGHT;
          case IntChessman.BISHOP:
            return WHITEBISHOP;
          case IntChessman.ROOK:
            return WHITEROOK;
          case IntChessman.QUEEN:
            return WHITEQUEEN;
          case IntChessman.KING:
            return WHITEKING;
          case IntChessman.NOCHESSMAN:
          default:
            throw new IllegalArgumentException();
        }
      case IntColor.BLACK:
        switch (chessman) {
          case IntChessman.PAWN:
            return BLACKPAWN;
          case IntChessman.KNIGHT:
            return BLACKKNIGHT;
          case IntChessman.BISHOP:
            return BLACKBISHOP;
          case IntChessman.ROOK:
            return BLACKROOK;
          case IntChessman.QUEEN:
            return BLACKQUEEN;
          case IntChessman.KING:
            return BLACKKING;
          case IntChessman.NOCHESSMAN:
          default:
            throw new IllegalArgumentException();
        }
      case IntColor.NOCOLOR:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static GenericPiece toGenericPiece(int piece) {
    switch (piece) {
      case WHITEPAWN:
        return GenericPiece.WHITEPAWN;
      case WHITEKNIGHT:
        return GenericPiece.WHITEKNIGHT;
      case WHITEBISHOP:
        return GenericPiece.WHITEBISHOP;
      case WHITEROOK:
        return GenericPiece.WHITEROOK;
      case WHITEQUEEN:
        return GenericPiece.WHITEQUEEN;
      case WHITEKING:
        return GenericPiece.WHITEKING;
      case BLACKPAWN:
        return GenericPiece.BLACKPAWN;
      case BLACKKNIGHT:
        return GenericPiece.BLACKKNIGHT;
      case BLACKBISHOP:
        return GenericPiece.BLACKBISHOP;
      case BLACKROOK:
        return GenericPiece.BLACKROOK;
      case BLACKQUEEN:
        return GenericPiece.BLACKQUEEN;
      case BLACKKING:
        return GenericPiece.BLACKKING;
      case NOPIECE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int ordinal(int piece) {
    switch (piece) {
      case WHITEPAWN:
        return 0;
      case WHITEKNIGHT:
        return 1;
      case WHITEBISHOP:
        return 2;
      case WHITEROOK:
        return 3;
      case WHITEQUEEN:
        return 4;
      case WHITEKING:
        return 5;
      case BLACKPAWN:
        return 6;
      case BLACKKNIGHT:
        return 7;
      case BLACKBISHOP:
        return 8;
      case BLACKROOK:
        return 9;
      case BLACKQUEEN:
        return 10;
      case BLACKKING:
        return 11;
      case NOPIECE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static boolean isValid(int piece) {
    switch (piece) {
      case WHITEPAWN:
      case WHITEKNIGHT:
      case WHITEBISHOP:
      case WHITEROOK:
      case WHITEQUEEN:
      case WHITEKING:
      case BLACKPAWN:
      case BLACKKNIGHT:
      case BLACKBISHOP:
      case BLACKROOK:
      case BLACKQUEEN:
      case BLACKKING:
        return true;
      case NOPIECE:
        return false;
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int getChessman(int piece) {
    switch (piece) {
      case WHITEPAWN:
      case BLACKPAWN:
        return IntChessman.PAWN;
      case WHITEKNIGHT:
      case BLACKKNIGHT:
        return IntChessman.KNIGHT;
      case WHITEBISHOP:
      case BLACKBISHOP:
        return IntChessman.BISHOP;
      case WHITEROOK:
      case BLACKROOK:
        return IntChessman.ROOK;
      case WHITEQUEEN:
      case BLACKQUEEN:
        return IntChessman.QUEEN;
      case WHITEKING:
      case BLACKKING:
        return IntChessman.KING;
      case NOPIECE:
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int getColor(int piece) {
    switch (piece) {
      case WHITEPAWN:
      case WHITEKNIGHT:
      case WHITEBISHOP:
      case WHITEROOK:
      case WHITEQUEEN:
      case WHITEKING:
        return IntColor.WHITE;
      case BLACKPAWN:
      case BLACKKNIGHT:
      case BLACKBISHOP:
      case BLACKROOK:
      case BLACKQUEEN:
      case BLACKKING:
        return IntColor.BLACK;
      case NOPIECE:
      default:
        throw new IllegalArgumentException();
    }
  }

}
