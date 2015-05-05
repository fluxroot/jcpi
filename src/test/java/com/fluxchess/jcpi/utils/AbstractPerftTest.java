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
package com.fluxchess.jcpi.utils;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.IllegalNotationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public abstract class AbstractPerftTest {

  protected abstract IMoveGenerator getMoveGenerator(GenericBoard genericBoard);

  protected void testPerft(int testDepth) throws IOException, IllegalNotationException {
    for (int i = 1; i <= testDepth; i++) {
      BufferedReader file = null;
      try {
        file = new BufferedReader(new InputStreamReader(AbstractPerftTest.class.getResourceAsStream("/perftsuite.epd")));

        String line = file.readLine();
        while (line != null) {
          String[] tokens = line.split(";");

          if (tokens.length > i) {
            String[] data = tokens[i].trim().split(" ");
            int depth = Integer.parseInt(data[0].substring(1));
            int nodes = Integer.parseInt(data[1]);

            GenericBoard genericBoard = new GenericBoard(tokens[0].trim());
            IMoveGenerator moveGenerator = getMoveGenerator(genericBoard);

            long result = moveGenerator.perft(depth);
            assertEquals(genericBoard.toString() + " at depth " + depth + "failed", nodes, result);
          }

          line = file.readLine();
        }
      } finally {
        if (file != null) {
          try {
            file.close();
          } catch (IOException e) {
            // Do nothing
          }
        }
      }
    }
  }

}
