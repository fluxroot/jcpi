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
package com.fluxchess.jcpi.protocols;

import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.GenericPosition;
import com.fluxchess.jcpi.options.SpinnerOption;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UciProtocolTest {

	@Test
	public void testIsProtocolKeyword() {
		assertThat(UciProtocol.isProtocolKeyword("uci")).isTrue();
		assertThat(UciProtocol.isProtocolKeyword("UCI")).isTrue();

		assertThat(UciProtocol.isProtocolKeyword("xboard")).isFalse();
	}

	@Test
	public void testBasic() throws IOException {
		String[] commands = {
				""
		};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testDebug() throws IOException {
		String[] commands = {
				"debug",
				"debug on",
				"debug off",
				"debug abc"
		};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "debug"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineDebugCommand.class);
		assertThat(((EngineDebugCommand) command).toggle).isEqualTo(true);

		// "debug on"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineDebugCommand.class);
		assertThat(((EngineDebugCommand) command).toggle).isEqualTo(false);
		assertThat(((EngineDebugCommand) command).debug).isEqualTo(true);

		// "debug off"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineDebugCommand.class);
		assertThat(((EngineDebugCommand) command).toggle).isEqualTo(false);
		assertThat(((EngineDebugCommand) command).debug).isEqualTo(false);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testIsReady() throws IOException {
		String[] commands = {"isready"};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "isready"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineReadyRequestCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testSetOption() throws IOException {
		String[] commands = {
				"setoption",
				"setoption name",
				"setoption name Clear Hash",
				"setoption name Clear   Hash",
				"setoption name Clear Hash value",
				"setoption name Clear Hash value 5",
				"setoption name Clear Hash value 5 4 3 2 1"
		};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "setoption name Clear Hash"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineSetOptionCommand.class);
		assertThat(((EngineSetOptionCommand) command).name).isEqualTo("Clear Hash");
		assertThat(((EngineSetOptionCommand) command).value).isEqualTo(null);

		// "setoption name Clear   Hash"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineSetOptionCommand.class);
		assertThat(((EngineSetOptionCommand) command).name).isEqualTo("Clear   Hash");
		assertThat(((EngineSetOptionCommand) command).value).isEqualTo(null);

		// "setoption name Clear Hash value 5"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineSetOptionCommand.class);
		assertThat(((EngineSetOptionCommand) command).name).isEqualTo("Clear Hash");
		assertThat(((EngineSetOptionCommand) command).value).isEqualTo("5");

		// "setoption name Clear Hash value 5 4 3 2 1"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineSetOptionCommand.class);
		assertThat(((EngineSetOptionCommand) command).name).isEqualTo("Clear Hash");
		assertThat(((EngineSetOptionCommand) command).value).isEqualTo("5 4 3 2 1");

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testRegister() throws IOException {
		String[] commands = {"register"};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testUciNewGame() throws IOException {
		String[] commands = {"ucinewgame"};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "ucinewgame"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineNewGameCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testPosition() throws IOException {
		String[] commands = {
				"position",
				"position startpos",
				"a b c d position startpos",
				"a b c d position startpos moves a2a3",
				"a b c d position startpos x y z",
				"a b c d position startpos moves",
				"a b c d position fen",
				"a b c d position fen x y z",
				"a b c d position fen moves",
				"a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
				"a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves a4a5",
				"a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves xa10"
		};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "position startpos"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineAnalyzeCommand.class);
		assertThat(((EngineAnalyzeCommand) command).board).isEqualTo(new GenericBoard(GenericBoard.STANDARDSETUP));
		assertThat(((EngineAnalyzeCommand) command).moves.isEmpty()).isTrue();

		// "a b c d position startpos"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineAnalyzeCommand.class);
		assertThat(((EngineAnalyzeCommand) command).board).isEqualTo(new GenericBoard(GenericBoard.STANDARDSETUP));
		assertThat(((EngineAnalyzeCommand) command).moves.isEmpty()).isTrue();

		// "a b c d position startpos moves a2a3"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineAnalyzeCommand.class);
		assertThat(((EngineAnalyzeCommand) command).board).isEqualTo(new GenericBoard(GenericBoard.STANDARDSETUP));
		assertThat(((EngineAnalyzeCommand) command).moves.size()).isEqualTo(1);
		assertEquals(new GenericMove(GenericPosition.a2, GenericPosition.a3), ((EngineAnalyzeCommand) command).moves.get(0));

		// "a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineAnalyzeCommand.class);
		assertThat(((EngineAnalyzeCommand) command).board).isEqualTo(new GenericBoard(GenericBoard.STANDARDSETUP));
		assertThat(((EngineAnalyzeCommand) command).moves.isEmpty()).isTrue();

		// "a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves a4a5"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineAnalyzeCommand.class);
		assertThat(((EngineAnalyzeCommand) command).board).isEqualTo(new GenericBoard(GenericBoard.STANDARDSETUP));
		assertThat(((EngineAnalyzeCommand) command).moves.size()).isEqualTo(1);
		assertEquals(new GenericMove(GenericPosition.a4, GenericPosition.a5), ((EngineAnalyzeCommand) command).moves.get(0));

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testGo() throws IOException {
		String[] commands = {
				"go",
				"go searchmoves",
				"go searchmoves a2a3",
				"go searchmoves t2a9",
				"go ponder",
				"go wtime",
				"go wtime 100",
				"go wtime abc",
				"go btime",
				"go btime 100",
				"go btime abc",
				"go winc",
				"go winc 50",
				"go winc abc",
				"go binc",
				"go binc 50",
				"go binc abc",
				"go movestogo",
				"go movestogo 20",
				"go movestogo abc",
				"go depth",
				"go depth 5",
				"go depth abc",
				"go nodes",
				"go nodes 1000",
				"go nodes abc",
				"go mate",
				"go mate 4",
				"go mate abc",
				"go movetime",
				"go movetime 1000",
				"go movetime abc",
				"go infinite"
		};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "go"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);

		// "go searchmoves a2a3"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getSearchMoveList().size()).isEqualTo(1);
		assertEquals(new GenericMove(GenericPosition.a2, GenericPosition.a3), ((EngineStartCalculatingCommand) command).getSearchMoveList().get(0));

		// "go ponder"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getPonder()).isEqualTo(true);

		// "go wtime 100"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getClock(GenericColor.WHITE).longValue()).isEqualTo(100);

		// "go btime 100"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getClock(GenericColor.BLACK).longValue()).isEqualTo(100);

		// "go winc 50"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getClockIncrement(GenericColor.WHITE).longValue()).isEqualTo(50);

		// "go binc 50"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getClockIncrement(GenericColor.BLACK).longValue()).isEqualTo(50);

		// "go movestogo 20"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getMovesToGo().intValue()).isEqualTo(20);

		// "go depth 5"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getDepth().intValue()).isEqualTo(5);

		// "go nodes 1000"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getNodes().longValue()).isEqualTo(1000);

		// "go mate 4"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getMate().intValue()).isEqualTo(4);

		// "go movetime 1000"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getMoveTime().longValue()).isEqualTo(1000);

		// "go infinite"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStartCalculatingCommand.class);
		assertThat(((EngineStartCalculatingCommand) command).getInfinite()).isEqualTo(true);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testStop() throws IOException {
		String[] commands = {"stop"};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "stop"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineStopCalculatingCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testPonderHit() throws IOException {
		String[] commands = {"ponderhit"};
		UciProtocol protocol = createUciProtocol(commands);

		IEngineCommand command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EngineInitializeRequestCommand.class);

		// "ponderhit"
		command = protocol.receive();
		assertThat(command.getClass()).isEqualTo(EnginePonderHitCommand.class);

		try {
			command = protocol.receive();
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void testProtocolInitializeAnswerCommand() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		UciProtocol protocol = new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes()))), new PrintStream(buffer));

		ProtocolInitializeAnswerCommand command = new ProtocolInitializeAnswerCommand("My Engine", "The Author");
		command.addOption(new SpinnerOption("Hash", 16, 4, 64));
		protocol.send(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray())));
		String line = input.readLine();
		assertThat(line).isEqualTo("id name My Engine");
		line = input.readLine();
		assertThat(line).isEqualTo("id author The Author");
		line = input.readLine();
		assertThat(line).isEqualTo("option name Hash type spin default 16 min 4 max 64");
		line = input.readLine();
		assertThat(line).isEqualTo("uciok");
		assertNull(input.readLine());
	}

	@Test
	public void testProtocolReadyAnswerCommand() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		UciProtocol protocol = new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes()))), new PrintStream(buffer));

		ProtocolReadyAnswerCommand command = new ProtocolReadyAnswerCommand("does not matter");
		protocol.send(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray())));
		String line = input.readLine();
		assertThat(line).isEqualTo("readyok");
		assertNull(input.readLine());
	}

	@Test
	public void testProtocolBestMoveCommand() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		UciProtocol protocol = new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes()))), new PrintStream(buffer));

		// 1. Test
		ProtocolBestMoveCommand command = new ProtocolBestMoveCommand(
				new GenericMove(GenericPosition.a2, GenericPosition.a3),
				new GenericMove(GenericPosition.d3, GenericPosition.e5)
		);
		protocol.send(command);

		// 2. Test
		command = new ProtocolBestMoveCommand(null, null);
		protocol.send(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray())));

		// 1. Test
		String line = input.readLine();
		assertThat(line).isEqualTo("bestmove a2a3 ponder d3e5");

		// 2. Test
		line = input.readLine();
		assertThat(line).isEqualTo("bestmove nomove");

		assertNull(input.readLine());
	}

	@Test
	public void testProtocolInformationCommand() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		UciProtocol protocol = new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes()))), new PrintStream(buffer));

		ProtocolInformationCommand command = new ProtocolInformationCommand();
		command.setDepth(8);
		command.setMaxDepth(20);
		command.setCentipawns(400);
		command.setCurrentMove(new GenericMove(GenericPosition.a2, GenericPosition.a3));
		command.setCurrentMoveNumber(30);
		command.setHash(50);
		command.setNps(300);
		command.setTime(3000);
		command.setNodes(5000);
		protocol.send(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray())));
		String line = input.readLine();
		assertThat(line).isEqualTo("info depth 8 seldepth 20 score cp 400 currmove a2a3 currmovenumber 30 hashfull 50 nps 300 time 3000 nodes 5000");
		assertNull(input.readLine());
	}

	private UciProtocol createUciProtocol(String[] commands) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(buffer);
		try {
			for (String command : commands) {
				stream.println(command);
			}
		} finally {
			if (stream != null) {
				stream.close();
			}
		}

		return new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray()))), new PrintStream(new ByteArrayOutputStream()));
	}

}
