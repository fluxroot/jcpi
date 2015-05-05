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
package com.fluxchess.jcpi.protocols;

import com.fluxchess.jcpi.commands.*;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

public final class IOProtocolHandler implements IProtocolHandler {

  private final BufferedReader input;
  private final PrintStream output;
  private IProtocolHandler protocol = null;

  public IOProtocolHandler(BufferedReader input, PrintStream output) {
    if (input == null) throw new IllegalArgumentException();
    if (output == null) throw new IllegalArgumentException();

    this.input = input;
    this.output = output;
  }

  @Override
  public IEngineCommand receive() throws IOException {
    // Wait for the protocol keyword
    while (protocol == null) {
      String line = input.readLine();
      if (line != null) {
        line = line.trim();

        if (UciProtocol.isProtocolKeyword(line)) {
          protocol = new UciProtocol(input, output);
        }
      } else {
        // Something's wrong with the communication channel
        throw new EOFException();
      }
    }

    return protocol.receive();
  }

  @Override
  public void send(ProtocolInitializeAnswerCommand command) {
    if (protocol == null) throw new NoProtocolException();

    protocol.send(command);
  }

  @Override
  public void send(ProtocolReadyAnswerCommand command) {
    if (protocol == null) throw new NoProtocolException();

    protocol.send(command);
  }

  @Override
  public void send(ProtocolBestMoveCommand command) {
    if (protocol == null) throw new NoProtocolException();

    protocol.send(command);
  }

  @Override
  public void send(ProtocolInformationCommand command) {
    if (protocol == null) throw new NoProtocolException();

    protocol.send(command);
  }

}
