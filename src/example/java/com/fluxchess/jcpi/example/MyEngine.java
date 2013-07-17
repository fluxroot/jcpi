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
package com.fluxchess.jcpi.example;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.GenericPosition;

/**
 * This class represents your main engine. It is primarily used to communicate
 * with the framework.
 *
 * Inherit from AbstractEngine and implement all methods. It doesn't have to be
 * final. We use final here to improve performance.
 */
public final class MyEngine extends AbstractEngine {

    public MyEngine() {
        // Do some initialization stuff here...
    }

    // We do not implement the main function in the framework, leaving you
    // with all the flexibility you might need.
    public static void main(String[] args) {
        // Create your engine.
        AbstractEngine engine = new MyEngine();

        // Start the engine.
        engine.run();
    }

    protected void quit() {
        // Maybe do something like
        new EngineStopCalculatingCommand().accept(this);

        // Cleanup stuff if you like
    }

    public void receive(EngineInitializeRequestCommand command) {
        // Maybe it's a good idea to stop computing first...
        new EngineStopCalculatingCommand().accept(this);

        // Do your initialization stuff here...

        // Send an initialization answer back.
        getProtocol().send(new ProtocolInitializeAnswerCommand("MyEngine 1.0", "Nobody"));
    }

    public void receive(EngineSetOptionCommand command) {
        // Set options
    }

    public void receive(EngineDebugCommand command) {
        // Get your debugging state from somewhere else...
        // This is just an example.
        boolean state = false;

        // If toggle is set, just toggle debugging state
        if (command.toggle) {
            state = !state;
        } else {
            // Otherwise just set the debugging state
            state = command.debug;
        }

        // It might be nice to send an information string back, if the protocol supports it.
        ProtocolInformationCommand infoCommand = new ProtocolInformationCommand();
        if (state) {
            infoCommand.setString("Turning on debugging mode");
        } else {
            infoCommand.setString("Turning off debugging mode");
        }
        getProtocol().send(infoCommand);
    }

    public void receive(EngineReadyRequestCommand command) {
        // Send the token back
        getProtocol().send(new ProtocolReadyAnswerCommand(command.token));
    }

    public void receive(EngineNewGameCommand command) {
        // It might be good to stop computing first...
        new EngineStopCalculatingCommand().accept(this);

        // Maybe you want to clear some tables here...

        // Don't start computing though!
    }

    public void receive(EngineAnalyzeCommand command) {
        // Just setup the board
        // You have a GenericBoard available and all the moves
    }

    public void receive(EngineStartCalculatingCommand command) {
        // Start calculating here!
    }

    public void receive(EngineStopCalculatingCommand command) {
        // Do something like...
        getProtocol().send(new ProtocolBestMoveCommand(new GenericMove(GenericPosition.a1, GenericPosition.a2), null));
    }

    public void receive(EnginePonderHitCommand command) {
        // We have a ponder hit, it's your turn now!
    }

}
