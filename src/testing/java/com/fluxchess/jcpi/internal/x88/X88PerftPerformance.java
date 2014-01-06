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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class X88PerftPerformance {

  private static final Logger LOG = LoggerFactory.getLogger(X88PerftPerformance.class);

  public static void main(String[] args) {
    long totalNodes = 0;
    long totalTime = 0;

    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);
    X88MoveGenerator moveGenerator = new X88MoveGenerator(board);
    int depth = 6;

    LOG.info(String.format("Testing %s at depth %d", genericBoard.toString(), depth));

    for (int i = 1; i < 4; ++i) {
      long startTime = System.currentTimeMillis();
      int result = miniMax(board, moveGenerator, depth);
      long endTime = System.currentTimeMillis();

      long duration = endTime - startTime;
      totalNodes += result;
      totalTime += duration;

      LOG.info(String.format(
        "Duration iteration %d: %02d:%02d:%02d.%03d",
        i,
        TimeUnit.MILLISECONDS.toHours(duration),
        TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
        TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)),
        duration - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration))
      ));
    }

    LOG.info(String.format("Total nodes per millisecond: %d", totalNodes / totalTime));
  }

  private static int miniMax(Board board, X88MoveGenerator moveGenerator, int depth) {
    if (depth == 0) {
      return 1;
    }

    int totalNodes = 0;

    MoveList moves = moveGenerator.getMoves();
    for (int i = 0; i < moves.size; ++i) {
      int move = moves.moves[i];

      board.makeMove(move);
      int nodes = miniMax(board, moveGenerator, depth - 1);
      board.undoMove(move);

      totalNodes += nodes;
    }

    return totalNodes;
  }

}
