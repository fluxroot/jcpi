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
package com.fluxchess.jcpi;

import com.fluxchess.jcpi.commands.EngineQuitCommand;
import com.fluxchess.jcpi.commands.IEngine;
import com.fluxchess.jcpi.commands.IEngineCommand;
import com.fluxchess.jcpi.commands.IProtocol;
import com.fluxchess.jcpi.protocols.IProtocolHandler;
import com.fluxchess.jcpi.protocols.IOProtocolHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * This is the main engine class. Inherit your engine from this class and
 * implement all abstract methods.
 */
public abstract class AbstractEngine implements IEngine, Runnable {

  private boolean running = true;
  private final IProtocolHandler handler;

  protected AbstractEngine() {
    // Set the standard input and output stream
    this(new BufferedReader(new InputStreamReader(System.in)), System.out);
  }

  protected AbstractEngine(BufferedReader input, PrintStream output) {
    this(new IOProtocolHandler(input, output));
  }

  protected AbstractEngine(IProtocolHandler handler) {
    if (handler == null) throw new IllegalArgumentException();

    this.handler = handler;
  }

  public final void run() {
    try {
      // Run the engine
      while (running) {
        IEngineCommand command = handler.receive();
        assert command != null;

        command.accept(this);
      }
    } catch (IOException e) {
      // Something's wrong with the communication channel
      new EngineQuitCommand().accept(this);
    }
  }

  protected abstract void quit();

  protected final IProtocol getProtocol() {
    return handler;
  }

  public final void receive(EngineQuitCommand command) {
    if (command == null) throw new IllegalArgumentException();

    quit();
    running = false;
  }

}
