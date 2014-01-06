/*
 * Copyright 2007-2014 the original author or authors.
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

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.utils.AbstractPerftTest;

abstract class AbstractX88PerftTest extends AbstractPerftTest {

  @Override
  protected long miniMax(GenericBoard genericBoard, int depth) {
    Board board = new Board(genericBoard);

    return _miniMax(board, new X88MoveGenerator(board), depth);
  }

  private long _miniMax(Board board, X88MoveGenerator moveGenerator, int depth) {
    if (depth == 0) {
      return 1;
    }

    long totalNodes = 0;

    MoveList moves = moveGenerator.getMoves();
    for (int i = 0; i < moves.size; ++i) {
      int move = moves.moves[i];

      board.makeMove(move);
      long nodes = _miniMax(board, moveGenerator, depth - 1);
      board.undoMove(move);

      totalNodes += nodes;
    }

    return totalNodes;
  }

}
