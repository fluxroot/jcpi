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

import com.fluxchess.jcpi.internal.x88.X88MoveGenerator;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;

public final class MoveGenerator {

  private MoveGenerator() {
  }

  public static GenericMove[] getGenericMoves(GenericBoard genericBoard) {
    IMoveGenerator x88MoveGenerator = new X88MoveGenerator(genericBoard);
    return x88MoveGenerator.getGenericMoves();
  }

  public static long perft(GenericBoard genericBoard, int depth) {
    IMoveGenerator x88MoveGenerator = new X88MoveGenerator(genericBoard);
    return x88MoveGenerator.perft(depth);
  }

}
