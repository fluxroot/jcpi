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
package com.fluxchess.jcpi.protocols;

import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.GenericPosition;
import com.fluxchess.jcpi.options.SpinnerOption;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class UciProtocolTest {

  @Test
  public void testIsProtocolKeyword() {
    assertTrue(UciProtocol.isProtocolKeyword("uci"));
    assertTrue(UciProtocol.isProtocolKeyword("UCI"));

    assertFalse(UciProtocol.isProtocolKeyword("xboard"));
  }

  @Test
  public void testBasic() throws IOException {
    String[] commands = {
      ""
    };
    UciProtocol protocol = createUciProtocol(commands);

    IEngineCommand command = protocol.receive();
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "debug"
    command = protocol.receive();
    assertEquals(EngineDebugCommand.class, command.getClass());
    assertEquals(true, ((EngineDebugCommand) command).toggle);

    // "debug on"
    command = protocol.receive();
    assertEquals(EngineDebugCommand.class, command.getClass());
    assertEquals(false, ((EngineDebugCommand) command).toggle);
    assertEquals(true, ((EngineDebugCommand) command).debug);

    // "debug off"
    command = protocol.receive();
    assertEquals(EngineDebugCommand.class, command.getClass());
    assertEquals(false, ((EngineDebugCommand) command).toggle);
    assertEquals(false, ((EngineDebugCommand) command).debug);

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "isready"
    command = protocol.receive();
    assertEquals(EngineReadyRequestCommand.class, command.getClass());

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "setoption name Clear Hash"
    command = protocol.receive();
    assertEquals(EngineSetOptionCommand.class, command.getClass());
    assertEquals("Clear Hash", ((EngineSetOptionCommand) command).name);
    assertEquals(null, ((EngineSetOptionCommand) command).value);

    // "setoption name Clear   Hash"
    command = protocol.receive();
    assertEquals(EngineSetOptionCommand.class, command.getClass());
    assertEquals("Clear   Hash", ((EngineSetOptionCommand) command).name);
    assertEquals(null, ((EngineSetOptionCommand) command).value);

    // "setoption name Clear Hash value 5"
    command = protocol.receive();
    assertEquals(EngineSetOptionCommand.class, command.getClass());
    assertEquals("Clear Hash", ((EngineSetOptionCommand) command).name);
    assertEquals("5", ((EngineSetOptionCommand) command).value);

    // "setoption name Clear Hash value 5 4 3 2 1"
    command = protocol.receive();
    assertEquals(EngineSetOptionCommand.class, command.getClass());
    assertEquals("Clear Hash", ((EngineSetOptionCommand) command).name);
    assertEquals("5 4 3 2 1", ((EngineSetOptionCommand) command).value);

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "ucinewgame"
    command = protocol.receive();
    assertEquals(EngineNewGameCommand.class, command.getClass());

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
      "a b c d position startpos moves a2a3  e7e5  b2b4",
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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "position startpos"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertTrue(((EngineAnalyzeCommand) command).moves.isEmpty());

    // "a b c d position startpos"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertTrue(((EngineAnalyzeCommand) command).moves.isEmpty());

    // "a b c d position startpos moves a2a3"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertEquals(1, ((EngineAnalyzeCommand) command).moves.size());
    assertEquals(new GenericMove(GenericPosition.a2, GenericPosition.a3), ((EngineAnalyzeCommand) command).moves.get(0));

    // "a b c d position startpos moves a2a3  e7e5  b2b4"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertEquals(3, ((EngineAnalyzeCommand) command).moves.size());
    assertEquals(new GenericMove(GenericPosition.a2, GenericPosition.a3), ((EngineAnalyzeCommand) command).moves.get(0));
    assertEquals(new GenericMove(GenericPosition.e7, GenericPosition.e5), ((EngineAnalyzeCommand) command).moves.get(1));
    assertEquals(new GenericMove(GenericPosition.b2, GenericPosition.b4), ((EngineAnalyzeCommand) command).moves.get(2));

    // "a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertTrue(((EngineAnalyzeCommand) command).moves.isEmpty());

    // "a b c d position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 moves a4a5"
    command = protocol.receive();
    assertEquals(EngineAnalyzeCommand.class, command.getClass());
    assertEquals(new GenericBoard(GenericBoard.STANDARDSETUP), ((EngineAnalyzeCommand) command).board);
    assertEquals(1, ((EngineAnalyzeCommand) command).moves.size());
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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "go"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());

    // "go searchmoves a2a3"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(1, ((EngineStartCalculatingCommand) command).getSearchMoveList().size());
    assertEquals(new GenericMove(GenericPosition.a2, GenericPosition.a3), ((EngineStartCalculatingCommand) command).getSearchMoveList().get(0));

    // "go ponder"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(true, ((EngineStartCalculatingCommand) command).getPonder());

    // "go wtime 100"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(100, ((EngineStartCalculatingCommand) command).getClock(GenericColor.WHITE).longValue());

    // "go btime 100"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(100, ((EngineStartCalculatingCommand) command).getClock(GenericColor.BLACK).longValue());

    // "go winc 50"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(50, ((EngineStartCalculatingCommand) command).getClockIncrement(GenericColor.WHITE).longValue());

    // "go binc 50"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(50, ((EngineStartCalculatingCommand) command).getClockIncrement(GenericColor.BLACK).longValue());

    // "go movestogo 20"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(20, ((EngineStartCalculatingCommand) command).getMovesToGo().intValue());

    // "go depth 5"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(5, ((EngineStartCalculatingCommand) command).getDepth().intValue());

    // "go nodes 1000"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(1000, ((EngineStartCalculatingCommand) command).getNodes().longValue());

    // "go mate 4"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(4, ((EngineStartCalculatingCommand) command).getMate().intValue());

    // "go movetime 1000"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(1000, ((EngineStartCalculatingCommand) command).getMoveTime().longValue());

    // "go infinite"
    command = protocol.receive();
    assertEquals(EngineStartCalculatingCommand.class, command.getClass());
    assertEquals(true, ((EngineStartCalculatingCommand) command).getInfinite());

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "stop"
    command = protocol.receive();
    assertEquals(EngineStopCalculatingCommand.class, command.getClass());

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
    assertEquals(EngineInitializeRequestCommand.class, command.getClass());

    // "ponderhit"
    command = protocol.receive();
    assertEquals(EnginePonderHitCommand.class, command.getClass());

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
    assertEquals("id name My Engine", line);
    line = input.readLine();
    assertEquals("id author The Author", line);
    line = input.readLine();
    assertEquals("option name Hash type spin default 16 min 4 max 64", line);
    line = input.readLine();
    assertEquals("uciok", line);
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
    assertEquals("readyok", line);
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
    assertEquals("bestmove a2a3 ponder d3e5", line);

    // 2. Test
    line = input.readLine();
    assertEquals("bestmove nomove", line);

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
    assertEquals("info depth 8 seldepth 20 score cp 400 currmove a2a3 currmovenumber 30 hashfull 50 nps 300 time 3000 nodes 5000", line);
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
