/**
 * EngineDebugCommandTest.java
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
package net.sourceforge.jcpi.commands;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * EngineDebugCommandTest
 *
 * @author Phokham Nonava
 */
public class EngineDebugCommandTest {

	@Test
	public final void testEngineDebugCommand() {
		EngineDebugCommand command = new EngineDebugCommand(false, true);
		
		assertEquals(true, command.debug);
	}

}
