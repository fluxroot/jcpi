/*
 * Copyright 2007-2013 the original author or authors.
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
import com.fluxchess.jcpi.protocols.UciProtocol;

import java.io.*;
import java.util.Objects;

/**
 * This is the main engine class. Inherit your engine from this class and
 * implement all abstract methods.
 */
public abstract class AbstractEngine implements IEngine {

    private boolean running = true;
    private IProtocolHandler handler;

    protected AbstractEngine() {
    }

    public final void run() {
        // Set the standard input and output stream
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintStream output = System.out;

        try {
            // Wait for the protocol keyword
            while (handler == null) {
                String tokenString = input.readLine();
                if (tokenString != null) {
                    tokenString = tokenString.trim();

                    if (UciProtocol.isProtocolKeyword(tokenString)) {
                        handler = new UciProtocol(input, output);
                    }
                } else {
                    // Something's wrong with the communication channel
                    throw new EOFException();
                }
            }

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
        Objects.requireNonNull(command);

        quit();
        running = false;
    }

}
