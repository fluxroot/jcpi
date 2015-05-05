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

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.utils.AbstractPerftTest;
import com.fluxchess.jcpi.utils.IMoveGenerator;

import java.io.IOException;

public final class X88MoveGeneratorLongTest extends AbstractPerftTest {

  public static void main(String[] args) throws IOException, IllegalNotationException {
    X88MoveGeneratorLongTest x88MoveGeneratorLongTest = new X88MoveGeneratorLongTest();
    x88MoveGeneratorLongTest.testPerft(6);
  }

  @Override
  protected IMoveGenerator getMoveGenerator(GenericBoard genericBoard) {
    return new X88MoveGenerator(genericBoard);
  }

}
