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
package jcpi.standardio;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import jcpi.IGui;
import jcpi.commands.IEngineCommand;

public abstract class AbstractStandardIoProtocol implements IGui {

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
        Objects.requireNonNull(writer);
        Objects.requireNonNull(queue);

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
