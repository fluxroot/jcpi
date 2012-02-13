/**
 * GenericMove.java
 * 
 * Copyright 2007 Java Chess Protocol Interface Project
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
package jcpi.data;

/**
 * GenericMove
 *
 * @author Phokham Nonava
 */
public class GenericMove {

	public final GenericPosition from;
	public final GenericPosition to;
	public final GenericChessman promotion;

	public GenericMove(GenericPosition from, GenericPosition to) {
		this(from, to, null);
	}

	public GenericMove(GenericPosition from, GenericPosition to, GenericChessman promotion) {
		if (from == null) throw new IllegalArgumentException();
		if (to == null) throw new IllegalArgumentException();
		if (promotion != null && !promotion.isValidPromotion()) throw new IllegalArgumentException();

		this.from = from;
		this.to = to;
		this.promotion = promotion;
	}

	public GenericMove(String notation) throws IllegalNotationException {
		if (notation == null) throw new IllegalArgumentException();
		
		// Clean whitespace at the beginning and at the to
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
			this.promotion = GenericChessman.valueOfPromotion(notation.charAt(4));
			if (this.promotion == null) {
				throw new IllegalNotationException();
			}
			
			notation = notation.substring(0, 4);
		} else {
			this.promotion = null;
		}

		if (notation.length() == 4) {
			GenericFile file = GenericFile.valueOf(notation.charAt(0));
			if (file == null) {
				throw new IllegalNotationException();
			}
			
			GenericRank rank = GenericRank.valueOf(notation.charAt(1));
			if (rank == null) {
				throw new IllegalNotationException();
			}
			
			this.from = GenericPosition.valueOf(file, rank);
			
			file = GenericFile.valueOf(notation.charAt(2));
			if (file == null) {
				throw new IllegalNotationException();
			}
			
			rank = GenericRank.valueOf(notation.charAt(3));
			if (rank == null) {
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

}
