/*
** Copyright 2007-2012 Phokham Nonava
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
package jcpi;

import jcpi.commands.EngineQuitCommand;
import jcpi.commands.IEngineCommand;

/**
 * This is the main engine class. Inherit your engine from this class and
 * implement all abstract methods.
 *
 * @author Phokham Nonava
 */
public abstract class AbstractEngine implements IEngine {

	/**
	 * Stops and exits the engine.
	 */
	protected abstract void quit();

	/**
	 * The communication channel.
	 */
	protected final AbstractCommunication communication;

	/**
	 * Whether the engine is running.
	 */
	private boolean running = true;

	/**
	 * Creates a new AbstractEngine.
	 *
	 * @param communication the AbstractCommunication.
	 */
	public AbstractEngine(AbstractCommunication communication) {
		if (communication == null) throw new IllegalArgumentException();

		this.communication = communication;
	}

	/**
	 * Runs the engine.
	 */
	public final void run() {
		while (this.running) {
			IEngineCommand command = this.communication.receive();
			assert command != null;

			command.accept(this);
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jcpi.IEngine#visit(net.sourceforge.jcpi.commands.EngineQuitCommand)
	 */
	public final void visit(EngineQuitCommand command) {
		if (command == null) throw new IllegalArgumentException();

		quit();
		this.running = false;
	}

}
