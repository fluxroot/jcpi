/**
 * MyEngine.java
 * 
 * Copyright 2007 Java Chess Protocol Interface Project
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
package net.sourceforge.jcpi.example;

import net.sourceforge.jcpi.AbstractCommunication;
import net.sourceforge.jcpi.AbstractEngine;
import net.sourceforge.jcpi.commands.EngineAnalyzeCommand;
import net.sourceforge.jcpi.commands.EngineDebugCommand;
import net.sourceforge.jcpi.commands.EngineInitializeRequestCommand;
import net.sourceforge.jcpi.commands.EngineNewGameCommand;
import net.sourceforge.jcpi.commands.EnginePonderHitCommand;
import net.sourceforge.jcpi.commands.EngineReadyRequestCommand;
import net.sourceforge.jcpi.commands.EngineSetOptionCommand;
import net.sourceforge.jcpi.commands.EngineStartCalculatingCommand;
import net.sourceforge.jcpi.commands.EngineStopCalculatingCommand;
import net.sourceforge.jcpi.commands.GuiBestMoveCommand;
import net.sourceforge.jcpi.commands.GuiInformationCommand;
import net.sourceforge.jcpi.commands.GuiInitializeAnswerCommand;
import net.sourceforge.jcpi.commands.GuiReadyAnswerCommand;
import net.sourceforge.jcpi.data.GenericMove;
import net.sourceforge.jcpi.data.GenericPosition;
import net.sourceforge.jcpi.standardio.StandardIoCommunication;

/**
 * This class represents your main engine. It is primarily used to communicate
 * with the framework.
 * 
 * Inherit from AbstractEngine and implement all methods. It doesn't have to be
 * final. We use final here to improve performance.
 * 
 * @author Phokham Nonava
 */
public final class MyEngine extends AbstractEngine {

	public MyEngine(AbstractCommunication communication) {
		// We need to call the constructor of our base class.
		super(communication);
		
		// Do some initialization stuff here...
	}

	// We do not implement the main function in the framework, leaving you
	// with all the flexibility you might need.
	public static void main(String[] args) {
		// Choose and create a communication channel. For now there exists only
		// an object for the standard io communication.
		AbstractCommunication communication = new StandardIoCommunication();
		
		// Create your engine.
		AbstractEngine engine = new MyEngine(communication);

		// Start the engine.
		engine.run();
	}

	protected void quit() {
		// Maybe do something like
		new EngineStopCalculatingCommand().accept(this);
		
		// Cleanup stuff if you like
	}

	public void visit(EngineInitializeRequestCommand command) {
		// Maybe it's a good idea to stop computing first...
		new EngineStopCalculatingCommand().accept(this);
		
		// Do your initialization stuff here...
		
		// Send an initialization answer back.
		this.communication.send(new GuiInitializeAnswerCommand("MyEngine 1.0", "Nobody"));
	}

	public void visit(EngineSetOptionCommand command) {
		// Set options
	}

	public void visit(EngineDebugCommand command) {
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
		GuiInformationCommand infoCommand = new GuiInformationCommand();
		if (state) {
			infoCommand.setString("Turning on debugging mode");
		} else {
			infoCommand.setString("Turning off debugging mode");
		}
		this.communication.send(infoCommand);
	}

	public void visit(EngineReadyRequestCommand command) {
		// Send the token back
		this.communication.send(new GuiReadyAnswerCommand(command.token));
	}

	public void visit(EngineNewGameCommand command) {
		// It might be good to stop computing first...
		new EngineStopCalculatingCommand().accept(this);
		
		// Maybe you want to clear some tables here...

		// Don't start computing though!
	}

	public void visit(EngineAnalyzeCommand command) {
		// Just setup the board
		// You have a GenericBoard available and all the moves
	}

	public void visit(EngineStartCalculatingCommand command) {
		// Start calculating here!
	}

	public void visit(EngineStopCalculatingCommand command) {
		// Do something like...
		this.communication.send(new GuiBestMoveCommand(new GenericMove(GenericPosition.a1, GenericPosition.a2), null));
	}

	public void visit(EnginePonderHitCommand command) {
		// We have a ponder hit, it's your turn now!
	}

}
