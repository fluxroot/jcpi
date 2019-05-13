/*
 * Copyright 2007-2019 The Java Chess Protocol Interface Project Authors
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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

import static com.fluxchess.jcpi.models.GenericCastling.KINGSIDE;
import static com.fluxchess.jcpi.models.GenericCastling.QUEENSIDE;
import static com.fluxchess.jcpi.models.GenericChessman.*;
import static com.fluxchess.jcpi.models.GenericColor.BLACK;
import static com.fluxchess.jcpi.models.GenericColor.WHITE;
import static com.fluxchess.jcpi.models.GenericFile.*;
import static com.fluxchess.jcpi.models.GenericPiece.*;
import static com.fluxchess.jcpi.models.GenericRank.*;

public final class GenericBoard {

	public static final int STANDARDSETUP = 518;

	private final Map<GenericPosition, GenericPiece> board = new HashMap<GenericPosition, GenericPiece>();
	private final Map<GenericColor, Map<GenericCastling, GenericFile>> castling = new EnumMap<GenericColor, Map<GenericCastling, GenericFile>>(GenericColor.class);
	private GenericPosition enPassant = null;
	private GenericColor activeColor = WHITE;
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
			this.board.put(GenericPosition.of(file, _2), WHITEPAWN);
			this.board.put(GenericPosition.of(file, _7), BLACKPAWN);
		}

		int x = setup;
		GenericFile file = null;

		// Setup light-square bishop
		int remainder = x % 4;
		switch (remainder) {
			case 0:
				file = b;
				break;
			case 1:
				file = d;
				break;
			case 2:
				file = f;
				break;
			case 3:
				file = h;
				break;
			default:
				throw new IllegalStateException();
		}
		this.board.put(GenericPosition.of(file, _1), WHITEBISHOP);
		this.board.put(GenericPosition.of(file, _8), BLACKBISHOP);
		x = x / 4;

		// Setup dark-square bishop
		remainder = x % 4;
		switch (remainder) {
			case 0:
				file = a;
				break;
			case 1:
				file = c;
				break;
			case 2:
				file = e;
				break;
			case 3:
				file = g;
				break;
			default:
				throw new IllegalStateException();
		}
		this.board.put(GenericPosition.of(file, _1), WHITEBISHOP);
		this.board.put(GenericPosition.of(file, _8), BLACKBISHOP);
		x = x / 4;

		// Setup queen
		file = null;
		remainder = x % 6;
		for (GenericFile queenFile : GenericFile.values()) {
			if (this.board.get(GenericPosition.of(queenFile, _1)) == null) {
				if (remainder == 0) {
					file = queenFile;
					break;
				} else {
					--remainder;
				}
			}
		}
		this.board.put(GenericPosition.of(file, _1), WHITEQUEEN);
		this.board.put(GenericPosition.of(file, _8), BLACKQUEEN);
		x = x / 6;

		// Setup kern ("KRN")
		GenericChessman[] kern = null;
		switch (x) {
			case 0:
				kern = new GenericChessman[]{KNIGHT, KNIGHT, ROOK, KING, ROOK};
				break;
			case 1:
				kern = new GenericChessman[]{KNIGHT, ROOK, KNIGHT, KING, ROOK};
				break;
			case 2:
				kern = new GenericChessman[]{KNIGHT, ROOK, KING, KNIGHT, ROOK};
				break;
			case 3:
				kern = new GenericChessman[]{KNIGHT, ROOK, KING, ROOK, KNIGHT};
				break;
			case 4:
				kern = new GenericChessman[]{ROOK, KNIGHT, KNIGHT, KING, ROOK};
				break;
			case 5:
				kern = new GenericChessman[]{ROOK, KNIGHT, KING, KNIGHT, ROOK};
				break;
			case 6:
				kern = new GenericChessman[]{ROOK, KNIGHT, KING, ROOK, KNIGHT};
				break;
			case 7:
				kern = new GenericChessman[]{ROOK, KING, KNIGHT, KNIGHT, ROOK};
				break;
			case 8:
				kern = new GenericChessman[]{ROOK, KING, KNIGHT, ROOK, KNIGHT};
				break;
			case 9:
				kern = new GenericChessman[]{ROOK, KING, ROOK, KNIGHT, KNIGHT};
				break;
			default:
				throw new IllegalStateException();
		}
		Iterator<GenericChessman> iter = Arrays.asList(kern).iterator();
		for (GenericFile kernFile : GenericFile.values()) {
			if (this.board.get(GenericPosition.of(kernFile, _1)) == null) {
				if (iter.hasNext()) {
					GenericChessman chessman = iter.next();
					this.board.put(GenericPosition.of(kernFile, _1), GenericPiece.of(WHITE, chessman));
					this.board.put(GenericPosition.of(kernFile, _8), GenericPiece.of(BLACK, chessman));

					if (chessman == ROOK) {
						if (this.kingFile.get(WHITE) == null) {
							this.castling.get(WHITE).put(QUEENSIDE, kernFile);
							this.castling.get(BLACK).put(QUEENSIDE, kernFile);
						} else {
							this.castling.get(WHITE).put(KINGSIDE, kernFile);
							this.castling.get(BLACK).put(KINGSIDE, kernFile);
						}
					} else if (chessman == KING) {
						this.kingFile.put(WHITE, kernFile);
						this.kingFile.put(BLACK, kernFile);
					}
				}
			}
		}
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
		this.activeColor = WHITE;

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

		if (piece.chessman == KING) {
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

		if (file != a && file != h) {
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
				GenericPiece piece = this.board.get(GenericPosition.of(file, rank));

				if (piece == null) {
					emptySquares++;
				} else {
					if (emptySquares > 0) {
						fen += emptySquares;
						emptySquares = 0;
					}
					fen += piece.toNotation();
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
		// Precondition: board is clear!

		// Clean notation and split into terms
		notation = notation.trim();

		List<String> tokens = new ArrayList<String>(Arrays.asList(notation.split(" ")));
		for (Iterator<String> iter = tokens.iterator(); iter.hasNext(); ) {
			String token = iter.next();

			if (token.length() == 0) {
				iter.remove();
			}
		}

		parse(tokens);
	}

	private void parse(List<String> tokens) throws IllegalNotationException {
		if (tokens.size() < 4 || tokens.size() > 6) throw new IllegalNotationException();

		Iterator<String> iter = tokens.iterator();

		// Parse data
		if (iter.hasNext()) {
			String token = iter.next();
			parsePiecePlacement(token);
		} else {
			throw new IllegalNotationException();
		}

		// Parse active color
		if (iter.hasNext()) {
			String token = iter.next();

			if (token.length() == 1) {
				char input = token.charAt(0);
				this.activeColor = GenericColor.of(input).orElseThrow(IllegalNotationException::new);
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
						castlingFile = GenericFile.of(character).orElseThrow(IllegalNotationException::new);
						this.isFrc = true;

						GenericFile kingfile = this.kingFile.get(color);
						if (kingfile != null) {
							if (castlingFile.compareTo(kingfile) < 0) {
								castling = QUEENSIDE;
							} else {
								castling = KINGSIDE;
							}
						} else {
							throw new IllegalNotationException();
						}
					} else {
						castling = GenericCastling.valueOf(character);
						if (castling == KINGSIDE) {
							castlingFile = h;
						} else {
							castlingFile = a;
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
					GenericFile file = GenericFile.of(token.charAt(0)).orElseThrow(IllegalNotationException::new);
					GenericRank rank = GenericRank.of(token.charAt(1)).orElseThrow(IllegalNotationException::new);
					if ((rank == GenericRank._3 && this.activeColor == BLACK)
							|| (rank == GenericRank._6 && this.activeColor == WHITE)) {
						this.enPassant = GenericPosition.of(file, rank);
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

	private void parsePiecePlacement(String token) throws IllegalNotationException {
		GenericFile file = a;
		GenericRank rank = _8;
		CharacterIterator iter = new StringCharacterIterator(token);

		char character = iter.first();
		while (true) {
			// Try to get piece
			Optional<GenericPiece> piece = GenericPiece.from(character);
			if (piece.isPresent()) {
				if (piece.get().chessman == KING) {
					this.kingFile.put(piece.get().color, file);
				}
				this.board.put(GenericPosition.of(file, rank), piece.get());
			} else {
				// Try to get empty fields
				int emptyFields = Character.getNumericValue(character);
				if (emptyFields < 1) {
					throw new IllegalNotationException();
				}
				if (emptyFields == 1) {
					// Stay
				} else if (emptyFields > 1 && emptyFields <= 8) {
					file = file.next(emptyFields - 1).orElseThrow(IllegalNotationException::new);
				} else {
					throw new IllegalNotationException();
				}
			}

			character = iter.next();
			if (character == '/') {
				if (file == h) {
					file = a;
					rank = rank.prev().orElseThrow(IllegalNotationException::new);
					character = iter.next();
				} else {
					throw new IllegalNotationException();
				}
			} else if (character == CharacterIterator.DONE) {
				break;
			} else {
				file = file.next().orElseThrow(IllegalNotationException::new);
			}
		}
	}

}
