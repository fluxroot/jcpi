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
package com.fluxchess.jcpi;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.fluxchess.jcpi.commands.EngineInitializeRequestCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EnginePonderHitCommand;
import com.fluxchess.jcpi.commands.EngineQuitCommand;
import com.fluxchess.jcpi.commands.EngineReadyRequestCommand;
import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.commands.EngineStopCalculatingCommand;

/**
 * This is the engine command interface.
 *
 * @author Phokham Nonava
 */
public interface IEngine {

    void visit(EngineInitializeRequestCommand command);
    void visit(EngineSetOptionCommand command);
    void visit(EngineQuitCommand command);

    void visit(EngineDebugCommand command);
    void visit(EngineReadyRequestCommand command);

    void visit(EngineNewGameCommand command);
    void visit(EngineAnalyzeCommand command);
    void visit(EngineStartCalculatingCommand command);
    void visit(EngineStopCalculatingCommand command);
    void visit(EnginePonderHitCommand command);

}
