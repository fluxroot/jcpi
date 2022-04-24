/*
 * Copyright 2007-2022 The Java Chess Protocol Interface Project Authors
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
package com.fluxchess.jcpi;

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.fluxchess.jcpi.commands.EngineInitializeRequestCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EnginePonderHitCommand;
import com.fluxchess.jcpi.commands.EngineReadyRequestCommand;
import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.commands.EngineStopCalculatingCommand;
import com.fluxchess.jcpi.protocols.IOProtocolHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractEngineTest {

	private BufferedReader testInput = null;
	private PrintStream testOutput = null;
	private BufferedReader engineInput = null;
	private PrintStream engineOutput = null;

	@BeforeEach
	public void setUp() throws IOException {
		PipedInputStream testInputPipe = new PipedInputStream();
		PipedOutputStream testOutputPipe = new PipedOutputStream();
		PipedInputStream engineInputPipe = new PipedInputStream(testOutputPipe);
		PipedOutputStream engineOutputPipe = new PipedOutputStream(testInputPipe);

		testInput = new BufferedReader(new InputStreamReader(testInputPipe));
		testOutput = new PrintStream(testOutputPipe);
		engineInput = new BufferedReader(new InputStreamReader(engineInputPipe));
		engineOutput = new PrintStream(engineOutputPipe);
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	public void testAbstractEngine() throws IOException, InterruptedException {
		final Semaphore semaphore = new Semaphore(0);
		Engine engine = new Engine(engineInput, engineOutput) {
			@Override
			protected void quit() {
				semaphore.release();
			}
		};
		Thread thread = new Thread(engine);
		thread.start();

		testOutput.println("uci");
		testOutput.println("quit");

		assertTrue(semaphore.tryAcquire(10, TimeUnit.SECONDS));

		thread.join(10000);
		assertFalse(thread.isAlive());
	}

	@Test
	public void testStreamClose() throws InterruptedException {
		final Semaphore semaphore = new Semaphore(0);
		Engine engine = new Engine(engineInput, engineOutput) {
			@Override
			protected void quit() {
				semaphore.release();
			}
		};
		Thread thread = new Thread(engine);
		thread.start();

		testOutput.close();

		assertTrue(semaphore.tryAcquire(10, TimeUnit.SECONDS));

		thread.join(10000);
		assertFalse(thread.isAlive());
	}

	@Test
	public void testGetUciProtocol() throws Exception {
		final Semaphore semaphore = new Semaphore(0);
		Engine engine = new Engine(engineInput, engineOutput) {
			@Override
			public void receive(EngineInitializeRequestCommand command) {
				semaphore.release();
			}
		};
		Thread thread = new Thread(engine);
		thread.start();

		testOutput.println("uci");

		assertTrue(semaphore.tryAcquire(10, TimeUnit.SECONDS));
		assertNotNull(engine.getProtocol());
		assertEquals(IOProtocolHandler.class, engine.getProtocol().getClass());

		testOutput.println("quit");

		thread.join(10000);
	}

	class Engine extends AbstractEngine {

		public Engine(BufferedReader input, PrintStream output) {
			super(input, output);
		}

		@Override
		protected void quit() {
		}

		@Override
		public void receive(EngineInitializeRequestCommand command) {
		}

		@Override
		public void receive(EngineSetOptionCommand command) {
		}

		@Override
		public void receive(EngineDebugCommand command) {
		}

		@Override
		public void receive(EngineReadyRequestCommand command) {
		}

		@Override
		public void receive(EngineNewGameCommand command) {
		}

		@Override
		public void receive(EngineAnalyzeCommand command) {
		}

		@Override
		public void receive(EngineStartCalculatingCommand command) {
		}

		@Override
		public void receive(EngineStopCalculatingCommand command) {
		}

		@Override
		public void receive(EnginePonderHitCommand command) {
		}

	}

}
