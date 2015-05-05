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

import java.util.*;

public final class GenericBoard {

  public static final int STANDARDSETUP = 518;

  private final Map<GenericPosition, GenericPiece> board = new HashMap<GenericPosition, GenericPiece>();
  private final Map<GenericColor, Map<GenericCastling, GenericFile>> castling = new EnumMap<GenericColor, Map<GenericCastling, GenericFile>>(GenericColor.class);
  private GenericPosition enPassant = null;
  private GenericColor activeColor = GenericColor.WHITE;
  private int halfMoveClock = 0;
  private int fullMoveNumber = 1;
  private final Map<GenericColor, GenericFile> kingFile = new EnumMap<GenericColor, GenericFile>(GenericColor.class);
  private boolean isFrc = false;

  public GenericBoard() {
    for (GenericColor color : GenericColor.values()) {
      this.castling.put(color, new EnumMap<GenericCastling, GenericFile>(GenericCastling.class));
    }
  }

  public GenericBoard(int setup) {
    this();
    if (setup < 0 || setup > 959) throw new IllegalArgumentException();

    if (setup != STANDARDSETUP) {
      this.isFrc = true;
    }

    // Setup pawns
    for (GenericFile file : GenericFile.values()) {
      this.board.put(GenericPosition.valueOf(file, GenericRank.R2), GenericPiece.WHITEPAWN);
      this.board.put(GenericPosition.valueOf(file, GenericRank.R7), GenericPiece.BLACKPAWN);
    }

    int x = setup;
    GenericFile file = null;

    // Setup light-square bishop
    int remainder = x % 4;
    switch (remainder) {
      case 0:
        file = GenericFile.Fb;
        break;
      case 1:
        file = GenericFile.Fd;
        break;
      case 2:
        file = GenericFile.Ff;
        break;
      case 3:
        file = GenericFile.Fh;
        break;
      default:
        assert false;
        break;
    }
    this.board.put(GenericPosition.valueOf(file, GenericRank.R1), GenericPiece.WHITEBISHOP);
    this.board.put(GenericPosition.valueOf(file, GenericRank.R8), GenericPiece.BLACKBISHOP);
    x = x / 4;

    // Setup dark-square bishop
    remainder = x % 4;
    switch (remainder) {
      case 0:
        file = GenericFile.Fa;
        break;
      case 1:
        file = GenericFile.Fc;
        break;
      case 2:
        file = GenericFile.Fe;
        break;
      case 3:
        file = GenericFile.Fg;
        break;
      default:
        assert false;
        break;
    }
    this.board.put(GenericPosition.valueOf(file, GenericRank.R1), GenericPiece.WHITEBISHOP);
    this.board.put(GenericPosition.valueOf(file, GenericRank.R8), GenericPiece.BLACKBISHOP);
    x = x / 4;

    // Setup queen
    file = null;
    remainder = x % 6;
    for (GenericFile queenFile : GenericFile.values()) {
      if (this.board.get(GenericPosition.valueOf(queenFile, GenericRank.R1)) == null) {
        if (remainder == 0) {
          file = queenFile;
          break;
        } else {
          --remainder;
        }
      }
    }
    assert file != null;
    this.board.put(GenericPosition.valueOf(file, GenericRank.R1), GenericPiece.WHITEQUEEN);
    this.board.put(GenericPosition.valueOf(file, GenericRank.R8), GenericPiece.BLACKQUEEN);
    x = x / 6;

    // Setup kern ("KRN")
    assert x >= 0 && x <= 9;
    GenericChessman[] kern = null;
    switch (x) {
      case 0:
        kern = new GenericChessman[]{GenericChessman.KNIGHT, GenericChessman.KNIGHT, GenericChessman.ROOK, GenericChessman.KING, GenericChessman.ROOK};
        break;
      case 1:
        kern = new GenericChessman[]{GenericChessman.KNIGHT, GenericChessman.ROOK, GenericChessman.KNIGHT, GenericChessman.KING, GenericChessman.ROOK};
        break;
      case 2:
        kern = new GenericChessman[]{GenericChessman.KNIGHT, GenericChessman.ROOK, GenericChessman.KING, GenericChessman.KNIGHT, GenericChessman.ROOK};
        break;
      case 3:
        kern = new GenericChessman[]{GenericChessman.KNIGHT, GenericChessman.ROOK, GenericChessman.KING, GenericChessman.ROOK, GenericChessman.KNIGHT};
        break;
      case 4:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KNIGHT, GenericChessman.KNIGHT, GenericChessman.KING, GenericChessman.ROOK};
        break;
      case 5:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KNIGHT, GenericChessman.KING, GenericChessman.KNIGHT, GenericChessman.ROOK};
        break;
      case 6:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KNIGHT, GenericChessman.KING, GenericChessman.ROOK, GenericChessman.KNIGHT};
        break;
      case 7:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KING, GenericChessman.KNIGHT, GenericChessman.KNIGHT, GenericChessman.ROOK};
        break;
      case 8:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KING, GenericChessman.KNIGHT, GenericChessman.ROOK, GenericChessman.KNIGHT};
        break;
      case 9:
        kern = new GenericChessman[]{GenericChessman.ROOK, GenericChessman.KING, GenericChessman.ROOK, GenericChessman.KNIGHT, GenericChessman.KNIGHT};
        break;
      default:
        assert false;
        break;
    }
    assert kern != null;
    Iterator<GenericChessman> iter = Arrays.asList(kern).iterator();
    for (GenericFile kernFile : GenericFile.values()) {
      if (this.board.get(GenericPosition.valueOf(kernFile, GenericRank.R1)) == null) {
        if (iter.hasNext()) {
          GenericChessman chessman = iter.next();
          this.board.put(GenericPosition.valueOf(kernFile, GenericRank.R1), GenericPiece.valueOf(GenericColor.WHITE, chessman));
          this.board.put(GenericPosition.valueOf(kernFile, GenericRank.R8), GenericPiece.valueOf(GenericColor.BLACK, chessman));

          if (chessman == GenericChessman.ROOK) {
            if (this.kingFile.get(GenericColor.WHITE) == null) {
              this.castling.get(GenericColor.WHITE).put(GenericCastling.QUEENSIDE, kernFile);
              this.castling.get(GenericColor.BLACK).put(GenericCastling.QUEENSIDE, kernFile);
            } else {
              this.castling.get(GenericColor.WHITE).put(GenericCastling.KINGSIDE, kernFile);
              this.castling.get(GenericColor.BLACK).put(GenericCastling.KINGSIDE, kernFile);
            }
          } else if (chessman == GenericChessman.KING) {
            this.kingFile.put(GenericColor.WHITE, kernFile);
            this.kingFile.put(GenericColor.BLACK, kernFile);
          }
        } else {
          assert false;
        }
      }
    }
    assert !iter.hasNext();
  }

  public GenericBoard(String notation) throws IllegalNotationException {
    this();
    if (notation == null) throw new IllegalArgumentException();

    parse(notation);
  }

  public void clear() {
    // Clear board
    for (GenericPosition position : GenericPosition.values()) {
      this.board.put(position, null);
    }

    // Clear castling
    for (GenericColor color : GenericColor.values()) {
      for (GenericCastling castling : GenericCastling.values()) {
        this.castling.get(color).put(castling, null);
      }
    }

    // Clear en passant
    this.enPassant = null;

    // Clear active color
    this.activeColor = GenericColor.WHITE;

    // Clear half move clock
    this.halfMoveClock = 0;

    // Clear full move number
    this.fullMoveNumber = 1;

    // Clear king file
    for (GenericColor color : GenericColor.values()) {
      this.kingFile.put(color, null);
    }

    // Clear FRC flag
    this.isFrc = false;
  }

  public GenericPiece getPiece(GenericPosition position) {
    if (position == null) throw new IllegalArgumentException();

    return this.board.get(position);
  }

  public void setPiece(GenericPiece piece, GenericPosition position) {
    if (piece == null) throw new IllegalArgumentException();
    if (position == null) throw new IllegalArgumentException();

    if (piece.chessman == GenericChessman.KING) {
      this.kingFile.put(piece.color, position.file);
    }
    this.board.put(position, piece);
  }

  public GenericFile getCastling(GenericColor color, GenericCastling castling) {
    if (color == null) throw new IllegalArgumentException();
    if (castling == null) throw new IllegalArgumentException();

    return this.castling.get(color).get(castling);
  }

  public void setCastling(GenericColor color, GenericCastling castling, GenericFile file) {
    if (color == null) throw new IllegalArgumentException();
    if (castling == null) throw new IllegalArgumentException();

    if (file != GenericFile.Fa && file != GenericFile.Fh) {
      this.isFrc = true;
    }
    this.castling.get(color).put(castling, file);
  }

  public GenericPosition getEnPassant() {
    return this.enPassant;
  }

  public void setEnPassant(GenericPosition position) {
    if (position == null) throw new IllegalArgumentException();

    this.enPassant = position;
  }

  public GenericColor getActiveColor() {
    return this.activeColor;
  }

  public void setActiveColor(GenericColor color) {
    if (color == null) throw new IllegalArgumentException();

    this.activeColor = color;
  }

  public int getHalfMoveClock() {
    return this.halfMoveClock;
  }

  public void setHalfMoveClock(int halfMoveClock) {
    if (halfMoveClock < 0) throw new IllegalArgumentException();

    this.halfMoveClock = halfMoveClock;
  }

  public int getFullMoveNumber() {
    return this.fullMoveNumber;
  }

  public void setFullMoveNumber(int fullMoveNumber) {
    if (fullMoveNumber < 1) throw new IllegalArgumentException();

    this.fullMoveNumber = fullMoveNumber;
  }

  public String toString() {
    String fen = "";

    // Chessman
    for (int i = GenericRank.values().length - 1; i >= 0; i--) {
      GenericRank rank = GenericRank.values()[i];
      int emptySquares = 0;

      for (GenericFile file : GenericFile.values()) {
        GenericPiece piece = this.board.get(GenericPosition.valueOf(file, rank));

        if (piece == null) {
          emptySquares++;
        } else {
          if (emptySquares > 0) {
            fen += emptySquares;
            emptySquares = 0;
          }
          fen += piece.toChar();
        }
      }

      if (emptySquares > 0) {
        fen += emptySquares;
      }

      if (i > 0) {
        fen += '/';
      }
    }

    fen += ' ';

    // Color
    fen += this.activeColor.toChar();

    fen += ' ';

    // Castling
    boolean castlingAvailable = false;
    for (GenericColor color : GenericColor.values()) {
      for (GenericCastling castling : GenericCastling.values()) {
        GenericFile value = this.castling.get(color).get(castling);
        if (value != null) {
          if (this.isFrc) {
            fen += color.transform(value.toChar());
          } else {
            fen += castling.toChar(color);
          }
          castlingAvailable = true;
        }
      }
    }
    if (!castlingAvailable) {
      fen += '-';
    }

    fen += ' ';

    // En passant
    if (this.enPassant != null) {
      fen += this.enPassant.toString();
    } else {
      fen += '-';
    }

    fen += ' ';

    // Half move clock
    fen += this.halfMoveClock;

    fen += ' ';

    // Full move number
    fen += this.fullMoveNumber;

    return fen;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof GenericBoard)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    GenericBoard rhs = (GenericBoard) obj;

    // Test pieces
    for (GenericPosition position : GenericPosition.values()) {
      if (this.board.get(position) != rhs.board.get(position)) {
        return false;
      }
    }

    // Test castling
    for (GenericColor color : GenericColor.values()) {
      for (GenericCastling castling : GenericCastling.values()) {
        GenericFile myFile = this.castling.get(color).get(castling);
        GenericFile rhsFile = rhs.castling.get(color).get(castling);
        if (myFile != null ^ rhsFile != null) {
          return false;
        } else if (myFile != null && rhsFile != null) {
          if (myFile != rhsFile) {
            return false;
          }
        } else {
          assert myFile == null && rhsFile == null;
        }
      }
    }

    // Test en passant
    if (this.enPassant != rhs.enPassant) {
      return false;
    }

    // Test active color
    if (this.activeColor != rhs.activeColor) {
      return false;
    }

    // Test half move clock
    if (this.halfMoveClock != rhs.halfMoveClock) {
      return false;
    }

    // Test full move number
    if (this.fullMoveNumber != rhs.fullMoveNumber) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = 17;

    for (GenericPosition genericPosition : GenericPosition.values()) {
      GenericPiece genericPiece = this.board.get(genericPosition);
      result = 31 * result + (genericPiece == null ? 0 : genericPiece.hashCode());
    }

    for (GenericColor genericColor : GenericColor.values()) {
      for (GenericCastling genericCastling : GenericCastling.values()) {
        GenericFile genericFile = this.castling.get(genericColor).get(genericCastling);
        result = 31 * result + (genericFile == null ? 0 : genericFile.hashCode());
      }
    }

    result = 31 * result + (this.enPassant == null ? 0 : this.enPassant.hashCode());

    result = 31 * result + (this.activeColor == null ? 0 : this.activeColor.hashCode());

    result = 31 * result + this.halfMoveClock;

    result = 31 * result + this.fullMoveNumber;

    return result;
  }

  private void parse(String notation) throws IllegalNotationException {
    assert notation != null;
    // Precondition: board is clear!

    // Clean notation and split into terms
    notation = notation.trim();

    List<String> tokens = new ArrayList<String>(Arrays.asList(notation.split(" ")));
    for (Iterator<String> iter = tokens.iterator(); iter.hasNext();) {
      String token = iter.next();

      if (token.length() == 0) {
        iter.remove();
      }
    }

    parse(tokens);
  }

  private void parse(List<String> tokens) throws IllegalNotationException {
    assert tokens != null;
    if (tokens.size() < 4 || tokens.size() > 6) throw new IllegalNotationException();

    Iterator<String> iter = tokens.iterator();

    // Parse data
    if (iter.hasNext()) {
      String token = iter.next();
      GenericFile file = GenericFile.Fa;
      GenericRank rank = GenericRank.R8;

      for (char character : token.toCharArray()) {
        if (file == null) {
          if (character == '/') {
            file = GenericFile.Fa;

            if (rank.hasPrev()) {
              rank = rank.prev();
            } else {
              // Wrong rank position!
              throw new IllegalNotationException();
            }
          } else {
            // Wrong file position!
            throw new IllegalNotationException();
          }
        } else {
          // Try to get piece
          if (GenericPiece.isValid(character)) {
            GenericPiece piece = GenericPiece.valueOf(character);
            if (piece.chessman == GenericChessman.KING) {
              this.kingFile.put(piece.color, file);
            }
            this.board.put(GenericPosition.valueOf(file, rank), piece);
          } else {
            // Try to get empty fields
            int emptyFields = Character.getNumericValue(character);

            if (emptyFields >= 1 && emptyFields <= 8) {
              if (file.hasNext(emptyFields - 1)) {
                file = file.next(emptyFields - 1);
              } else {
                // Out of bound!
                throw new IllegalNotationException();
              }
            } else {
              // Wrong character!
              throw new IllegalNotationException();
            }
          }

          if (file.hasNext()) {
            file = file.next();
          } else {
            file = null;
          }
        }
      }
    } else {
      throw new IllegalNotationException();
    }

    // Parse active color
    if (iter.hasNext()) {
      String token = iter.next();

      if (token.length() == 1) {
        char input = token.charAt(0);
        if (GenericColor.isValid(input)) {
          this.activeColor = GenericColor.valueOf(input);
        } else {
          throw new IllegalNotationException();
        }
      } else {
        throw new IllegalNotationException();
      }
    } else {
      throw new IllegalNotationException();
    }

    // Parse castling
    if (iter.hasNext()) {
      String token = iter.next();

      if (token.equalsIgnoreCase("-")) {
        // No castling available
      } else {
        for (char character : token.toCharArray()) {
          GenericColor color = GenericColor.colorOf(character);
          if (color == null) {
            throw new IllegalNotationException();
          }

          GenericCastling castling;
          GenericFile castlingFile;
          if (!GenericCastling.isValid(character)) {
            if (!GenericFile.isValid(character)) {
              throw new IllegalNotationException();
            } else {
              castlingFile = GenericFile.valueOf(character);
              this.isFrc = true;

              GenericFile kingfile = this.kingFile.get(color);
              if (kingfile != null) {
                if (castlingFile.compareTo(kingfile) < 0) {
                  castling = GenericCastling.QUEENSIDE;
                } else {
                  assert castlingFile.compareTo(kingfile) > 0;
                  castling = GenericCastling.KINGSIDE;
                }
              } else {
                throw new IllegalNotationException();
              }
            }
          } else {
            castling = GenericCastling.valueOf(character);
            if (castling == GenericCastling.KINGSIDE) {
              castlingFile = GenericFile.Fh;
            } else {
              assert castling == GenericCastling.QUEENSIDE;
              castlingFile = GenericFile.Fa;
            }
          }

          this.castling.get(color).put(castling, castlingFile);
        }
      }
    } else {
      throw new IllegalNotationException();
    }

    // Parse en passant
    if (iter.hasNext()) {
      String token = iter.next();

      if (token.equalsIgnoreCase("-")) {
        // No en passant available
      } else {
        if (token.length() == 2) {
          if (GenericFile.isValid(token.charAt(0)) && GenericRank.isValid(token.charAt(1))) {
            GenericFile file = GenericFile.valueOf(token.charAt(0));
            GenericRank rank = GenericRank.valueOf(token.charAt(1));

            if ((rank == GenericRank.R3 && this.activeColor == GenericColor.BLACK)
              || (rank == GenericRank.R6 && this.activeColor == GenericColor.WHITE)) {
              this.enPassant = GenericPosition.valueOf(file, rank);
            } else {
              throw new IllegalNotationException();
            }
          } else {
            throw new IllegalNotationException();
          }
        } else {
          throw new IllegalNotationException();
        }
      }
    } else {
      throw new IllegalNotationException();
    }

    // Parse half move clock
    if (iter.hasNext()) {
      String token = iter.next();

      try {
        int halfMoveClock = new Integer(token);

        if (halfMoveClock >= 0) {
          this.halfMoveClock = halfMoveClock;
        } else {
          throw new IllegalNotationException();
        }
      } catch (NumberFormatException e) {
        throw new IllegalNotationException();
      }
    }

    // Parse full move number
    if (iter.hasNext()) {
      String token = iter.next();

      try {
        int fullMoveNumber = new Integer(token);

        if (fullMoveNumber >= 1) {
          this.fullMoveNumber = fullMoveNumber;
        } else {
          throw new IllegalNotationException();
        }
      } catch (NumberFormatException e) {
        throw new IllegalNotationException();
      }
    }
  }

}
