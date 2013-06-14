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

import jcpi.commands.EngineAnalyzeCommand;
import jcpi.commands.EngineDebugCommand;
import jcpi.commands.EngineInitializeRequestCommand;
import jcpi.commands.EngineNewGameCommand;
import jcpi.commands.EnginePonderHitCommand;
import jcpi.commands.EngineQuitCommand;
import jcpi.commands.EngineReadyRequestCommand;
import jcpi.commands.EngineSetOptionCommand;
import jcpi.commands.EngineStartCalculatingCommand;
import jcpi.commands.EngineStopCalculatingCommand;

/**
 * This is the engine command interface.
 *
 * @author Phokham Nonava
 */
public interface IEngine {

    public abstract void visit(EngineInitializeRequestCommand command);
    public abstract void visit(EngineSetOptionCommand command);
    public abstract void visit(EngineQuitCommand command);

    public abstract void visit(EngineDebugCommand command);
    public abstract void visit(EngineReadyRequestCommand command);

    public abstract void visit(EngineNewGameCommand command);
    public abstract void visit(EngineAnalyzeCommand command);
    public abstract void visit(EngineStartCalculatingCommand command);
    public abstract void visit(EngineStopCalculatingCommand command);
    public abstract void visit(EnginePonderHitCommand command);

}
