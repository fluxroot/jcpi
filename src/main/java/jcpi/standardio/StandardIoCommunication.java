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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jcpi.AbstractCommunication;
import jcpi.commands.EngineQuitCommand;
import jcpi.commands.IEngineCommand;
import jcpi.commands.IGuiCommand;


/**
 * StandardIoCommunication
 *
 * @author Phokham Nonava
 */
public final class StandardIoCommunication extends AbstractCommunication {

    /**
     * The standard input.
     */
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * The standard output.
     */
    private final PrintStream writer = System.out;

    /**
     * The engine command queue.
     */
    private final Queue<IEngineCommand> engineCommandQueue = new LinkedList<IEngineCommand>();

    /**
     * The protocol.
     */
    private AbstractStandardIoProtocol protocol = null;

    /**
     * Creates a new StandardIoCommunication.
     */
    public StandardIoCommunication() {
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jcpi.AbstractCommunication#send(net.sourceforge.jcpi.commands.IGuiCommand)
     */
    public void send(IGuiCommand command) {
        if (command == null) throw new IllegalArgumentException();

        if (this.protocol != null) {
            command.accept(this.protocol);
        } else {
            // Ignore command, we don't have a protocol yet
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jcpi.AbstractCommunication#receive()
     */
    protected IEngineCommand receive() {
        // Get the next command from the queue
        IEngineCommand engineCommand = this.engineCommandQueue.poll();
        while (engineCommand == null) {
            try {
                // Read from the standard input
                String tokenString = this.reader.readLine();
                if (tokenString != null) {
                    tokenString = tokenString.trim();

                    // Check whether we have a protocol switch
                    AbstractStandardIoProtocol protocol = getProtocol(tokenString);
                    if (protocol != null) {
                        // Switch to the new protocol
                        this.protocol = protocol;
                    } else {
                        // We have a command here. Clean the command.
                        List<String> tokenList = Arrays.asList(tokenString.split(" "));
                        for (Iterator<String> iter = tokenList.iterator(); iter.hasNext();) {
                            String token = iter.next();

                            // Remove empty tokens
                            if (token.length() == 0) {
                                iter.remove();
                            }
                        }

                        // Try to parse the command.
                        // this.protocol.parse() modifies this.engineCommandQueue.
                        if (this.protocol != null) {
                            this.protocol.parse(tokenList);
                        } else {
                            // Ignore command, we don't have a protocol yet
                        }
                    }

                    // Get the next command from the queue
                    engineCommand = this.engineCommandQueue.poll();
                } else {
                    // Something's wrong with the communication channel
                    engineCommand = new EngineQuitCommand();
                }
            } catch (IOException e) {
                // Something's wrong with the communication channel
                engineCommand = new EngineQuitCommand();
            }
        }

        assert engineCommand != null;
        return engineCommand;
    }

    /**
     * Checks the token string on whether we have a protocol switch.
     *
     * @param token the token.
     * @return the new protocol or null if there's no protocol switch.
     */
    private AbstractStandardIoProtocol getProtocol(String token) {
        assert token != null;

        AbstractStandardIoProtocol protocol = null;

        if (token.equalsIgnoreCase("uci")) {
            protocol = new UciProtocol(this.writer, this.engineCommandQueue);
        }

        return protocol;
    }

}
