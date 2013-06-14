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

import jcpi.commands.IEngineCommand;
import jcpi.commands.IGuiCommand;

public abstract class AbstractCommunication {

    /**
     * Send an IGuiCommand. Make this method public so everyone can send
     * commands. We translate the IGuiCommand to the appropriate communication
     * channel command.
     *
     * @param command the IGuiCommand.
     */
    public abstract void send(IGuiCommand command);

    /**
     * Receives an IEngineCommand. Make this method protected so only our
     * framework can call it.
     *
     * @return an IEngineCommand.
     */
    protected abstract IEngineCommand receive();

    /**
     * Creates a new AbstractCommunication.
     */
    public AbstractCommunication() {
    }

}
