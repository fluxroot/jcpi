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
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IntChessman;
import com.fluxchess.jcpi.models.IntPiece;

/**
 * This class represents a move as a int value. The fields are represented by
 * the following bits.
 * <p/>
 *  0 -  2: the type (required)
 *  3 -  9: the origin position (required)
 * 10 - 16: the target position (required)
 * 17 - 21: the origin piece (required)
 * 22 - 26: the target piece (optional)
 * 27 - 29: the promotion chessman (optional)
 */
final class Move {

  public static final class Type {
    public static final int MASK = 0x7;

    public static final int NORMAL = 0;
    public static final int PAWNDOUBLE = 1;
    public static final int PAWNPROMOTION = 2;
    public static final int ENPASSANT = 3;
    public static final int CASTLING = 4;

    public static final int[] values = {
      NORMAL,
      PAWNDOUBLE,
      PAWNPROMOTION,
      ENPASSANT,
      CASTLING
    };

    private Type() {
    }
  }

  private static final int TYPE_SHIFT = 0;
  private static final int TYPE_MASK = Type.MASK << TYPE_SHIFT;
  private static final int ORIGINPOSITION_SHIFT = 3;
  private static final int ORIGINPOSITION_MASK = Position.MASK << ORIGINPOSITION_SHIFT;
  private static final int TARGETPOSITION_SHIFT = 10;
  private static final int TARGETPOSITION_MASK = Position.MASK << TARGETPOSITION_SHIFT;
  private static final int ORIGINPIECE_SHIFT = 17;
  private static final int ORIGINPIECE_MASK = IntPiece.MASK << ORIGINPIECE_SHIFT;
  private static final int TARGETPIECE_SHIFT = 22;
  private static final int TARGETPIECE_MASK = IntPiece.MASK << TARGETPIECE_SHIFT;
  private static final int PROMOTION_SHIFT = 27;
  private static final int PROMOTION_MASK = IntChessman.MASK << PROMOTION_SHIFT;

  private Move() {
  }

  public static int valueOf(int type, int originPosition, int targetPosition, int originPiece, int targetPiece, int promotion) {
    int move = 0;

    // Encode type
    assert type == Type.NORMAL
      || type == Type.PAWNDOUBLE
      || type == Type.PAWNPROMOTION
      || type == Type.ENPASSANT
      || type == Type.CASTLING;
    move |= type << TYPE_SHIFT;

    // Encode origin position
    assert (originPosition & 0x88) == 0;
    move |= originPosition << ORIGINPOSITION_SHIFT;

    // Encode target position
    assert (targetPosition & 0x88) == 0;
    move |= targetPosition << TARGETPOSITION_SHIFT;

    // Encode origin piece
    assert IntPiece.isValid(originPiece);
    move |= originPiece << ORIGINPIECE_SHIFT;

    // Encode target piece
    assert IntPiece.isValid(targetPiece) || targetPiece == IntPiece.NOPIECE;
    move |= targetPiece << TARGETPIECE_SHIFT;

    // Encode promotion
    assert (IntChessman.isValid(promotion) && IntChessman.isValidPromotion(promotion))
      || promotion == IntChessman.NOCHESSMAN;
    move |= promotion << PROMOTION_SHIFT;

    return move;
  }

  public static GenericMove toGenericMove(int move) {
    int type = getType(move);
    int originPosition = getOriginPosition(move);
    int targetPosition = getTargetPosition(move);

    switch (type) {
      case Type.NORMAL:
      case Type.PAWNDOUBLE:
      case Type.ENPASSANT:
      case Type.CASTLING:
        return new GenericMove(Position.toGenericPosition(originPosition), Position.toGenericPosition(targetPosition));
      case Type.PAWNPROMOTION:
        return new GenericMove(Position.toGenericPosition(originPosition), Position.toGenericPosition(targetPosition), IntChessman.toGenericChessman(getPromotion(move)));
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int getType(int move) {
    int type = (move & TYPE_MASK) >>> TYPE_SHIFT;
    assert type == Type.NORMAL
      || type == Type.PAWNDOUBLE
      || type == Type.PAWNPROMOTION
      || type == Type.ENPASSANT
      || type == Type.CASTLING;

    return type;
  }

  public static int getOriginPosition(int move) {
    int originPosition = (move & ORIGINPOSITION_MASK) >>> ORIGINPOSITION_SHIFT;
    assert (originPosition & 0x88) == 0;

    return originPosition;
  }

  public static int getTargetPosition(int move) {
    int targetPosition = (move & TARGETPOSITION_MASK) >>> TARGETPOSITION_SHIFT;
    assert (targetPosition & 0x88) == 0;

    return targetPosition;
  }

  public static int setTargetPosition(int move, int targetPosition) {
    // Zero out target position
    move &= ~TARGETPOSITION_MASK;

    // Encode target position
    assert (targetPosition & 0x88) == 0;
    move |= targetPosition << TARGETPOSITION_SHIFT;

    return move;
  }

  public static int setTargetPositionAndPiece(int move, int targetPosition, int targetPiece) {
    // Zero out target position and target piece
    move &= ~TARGETPOSITION_MASK;
    move &= ~TARGETPIECE_MASK;

    // Encode target position
    assert (targetPosition & 0x88) == 0;
    move |= targetPosition << TARGETPOSITION_SHIFT;

    // Encode target piece
    assert IntPiece.isValid(targetPiece) || targetPiece == IntPiece.NOPIECE;
    move |= targetPiece << TARGETPIECE_SHIFT;

    return move;
  }

  public static int getOriginPiece(int move) {
    int originPiece = (move & ORIGINPIECE_MASK) >>> ORIGINPIECE_SHIFT;
    assert IntPiece.isValid(originPiece);

    return originPiece;
  }

  public static int getTargetPiece(int move) {
    int targetPiece = (move & TARGETPIECE_MASK) >>> TARGETPIECE_SHIFT;
    assert IntPiece.isValid(targetPiece) || targetPiece == IntPiece.NOPIECE;

    return targetPiece;
  }

  public static int getPromotion(int move) {
    int promotion = (move & PROMOTION_MASK) >>> PROMOTION_SHIFT;
    assert (IntChessman.isValid(promotion) && IntChessman.isValidPromotion(promotion))
      || promotion == IntChessman.NOCHESSMAN;

    return promotion;
  }

  public static int setPromotion(int move, int promotion) {
    // Zero out promotion chessman
    move &= ~PROMOTION_MASK;

    // Encode promotion
    assert (IntChessman.isValid(promotion) && IntChessman.isValidPromotion(promotion))
      || promotion == IntChessman.NOCHESSMAN;
    move |= promotion << PROMOTION_SHIFT;

    return move;
  }

  public static String toString(int move) {
    String string = "";

    switch (getType(move)) {
      case Type.NORMAL:
        string += "NORMAL";
        break;
      case Type.PAWNDOUBLE:
        string += "PAWNDOUBLE";
        break;
      case Type.PAWNPROMOTION:
        string += "PAWNPROMOTION";
        break;
      case Type.ENPASSANT:
        string += "ENPASSANT";
        break;
      case Type.CASTLING:
        string += "CASTLING";
        break;
      default:
        throw new IllegalArgumentException();
    }

    assert getOriginPiece(move) != IntPiece.NOPIECE;
    string += ":";
    string += IntPiece.toGenericPiece(getOriginPiece(move));

    string += ":";
    string += toGenericMove(move).toString();

    if (getTargetPiece(move) != IntPiece.NOPIECE) {
      string += ":";
      string += IntPiece.toGenericPiece(getTargetPiece(move));
    }

    if (getType(move) == Type.PAWNPROMOTION) {
      string += ":";
      string += IntChessman.toGenericChessman(getPromotion(move));
    }

    return string;
  }

}
