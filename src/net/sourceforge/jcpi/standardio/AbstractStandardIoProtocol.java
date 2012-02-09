/**
 * AbstractStandardIoProtocol.java
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
package net.sourceforge.jcpi.standardio;

import java.io.PrintStream;
import java.util.List;
import java.util.Queue;

import net.sourceforge.jcpi.ICommunication;
import net.sourceforge.jcpi.commands.IEngineCommand;

/**
 * AbstractStandardIoProtocol
 *
 * @author Phokham Nonava
 */
public abstract class AbstractStandardIoProtocol implements ICommunication {

	/**
	 * The standard output.
	 */
	protected final PrintStream writer;

	/**
	 * The engine command queue.
	 */
	protected final Queue<IEngineCommand> queue;

	/**
	 * Creates a new AbstractStandardIoProtocol.
	 * 
	 * @param writer the standard output.
	 * @param queue the engine command queue.
	 */
	public AbstractStandardIoProtocol(PrintStream writer, Queue<IEngineCommand> queue) {
		if (writer == null) throw new IllegalArgumentException();
		if (queue == null) throw new IllegalArgumentException();

		this.writer = writer;
		this.queue = queue;
	}

	/**
	 * Parse the list of tokens.
	 * 
	 * @param tokenList the list of tokens.
	 */
	protected abstract void parse(List<String> tokenList);

}
