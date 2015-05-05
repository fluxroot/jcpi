/*
 * Copyright 2007-2015 the original author or authors.
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
package com.fluxchess.jcpi.commands;

public interface IEngine {

  void receive(EngineInitializeRequestCommand command);
  void receive(EngineSetOptionCommand command);
  void receive(EngineQuitCommand command);

  void receive(EngineDebugCommand command);
  void receive(EngineReadyRequestCommand command);

  void receive(EngineNewGameCommand command);
  void receive(EngineAnalyzeCommand command);
  void receive(EngineStartCalculatingCommand command);
  void receive(EngineStopCalculatingCommand command);
  void receive(EnginePonderHitCommand command);

}
