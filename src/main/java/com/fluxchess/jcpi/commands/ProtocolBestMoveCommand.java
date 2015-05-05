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
package com.fluxchess.jcpi.commands;

import com.fluxchess.jcpi.models.GenericMove;

public class ProtocolBestMoveCommand implements IProtocolCommand {

  public final GenericMove bestMove;
  public final GenericMove ponderMove;

  public ProtocolBestMoveCommand(GenericMove bestMove, GenericMove ponderMove) {
    this.bestMove = bestMove;
    if (bestMove == null) {
      // Force null on ponder move
      this.ponderMove = null;
    } else {
      this.ponderMove = ponderMove;
    }
  }

  public void accept(IProtocol protocol) {
    protocol.send(this);
  }

}
