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
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.options.AbstractOption;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UciProtocol implements IProtocolHandler {

  private final BufferedReader input;
  private final PrintStream output;

  private final Queue<IEngineCommand> queue = new LinkedList<IEngineCommand>();

  public static boolean isProtocolKeyword(String token) {
    if (token == null) throw new IllegalArgumentException();

    return token.equalsIgnoreCase("uci");
  }

  public UciProtocol(BufferedReader input, PrintStream output) {
    if (input == null) throw new IllegalArgumentException();
    if (output == null) throw new IllegalArgumentException();

    this.input = input;
    this.output = output;

    queue.add(new EngineInitializeRequestCommand());
  }

  public IEngineCommand receive() throws IOException {
    // Get the next command from the queue
    IEngineCommand engineCommand = queue.poll();
    while (engineCommand == null) {
      // Read from the standard input
      String line = input.readLine();
      if (line != null) {
        try {
          // Try to parse the command.
          String[] tokens = line.trim().split("\\s", 2);
          while (tokens.length > 0) {
            if (tokens[0].equalsIgnoreCase("debug")) {
              parseDebugCommand(tokens);
              break;
            } else if (tokens[0].equalsIgnoreCase("isready")) {
              queue.add(new EngineReadyRequestCommand());
              break;
            } else if (tokens[0].equalsIgnoreCase("setoption")) {
              parseSetOptionCommand(tokens);
              break;
            } else if (tokens[0].equalsIgnoreCase("register")) {
              // Do nothing
              break;
            } else if (tokens[0].equalsIgnoreCase("ucinewgame")) {
              queue.add(new EngineNewGameCommand());
              break;
            } else if (tokens[0].equalsIgnoreCase("position")) {
              parsePositionCommand(tokens);
              break;
            } else if (tokens[0].equalsIgnoreCase("go")) {
              parseGoCommand(tokens);
              break;
            } else if (tokens[0].equalsIgnoreCase("stop")) {
              queue.add(new EngineStopCalculatingCommand());
              break;
            } else if (tokens[0].equalsIgnoreCase("ponderhit")) {
              queue.add(new EnginePonderHitCommand());
              break;
            } else if (tokens[0].equalsIgnoreCase("quit")) {
              queue.add(new EngineQuitCommand());
              break;
            }

            if (tokens.length > 1) {
              assert tokens.length == 2;
              tokens = tokens[1].trim().split("\\s", 2);
            } else {
              break;
            }
          }
        } catch (ParseException e) {
          // Currently ignore errors in token stream
        }

        // Get the next command from the queue
        engineCommand = queue.poll();
      } else {
        // Something's wrong with the communication channel
        throw new EOFException();
      }
    }

    return engineCommand;
  }

  private void parseDebugCommand(String[] tokens) throws ParseException {
    assert tokens != null;

    if (tokens.length > 1) {
      String token = tokens[1].trim();

      if (token.equalsIgnoreCase("on")) {
        queue.add(new EngineDebugCommand(false, true));
      } else if (token.equalsIgnoreCase("off")) {
        queue.add(new EngineDebugCommand(false, false));
      } else {
        throw new ParseException("Error in debug command: unknown parameter " + token);
      }
    } else {
      queue.add(new EngineDebugCommand(true, false));
    }
  }

  private void parseSetOptionCommand(String[] tokens) throws ParseException {
    assert tokens != null;

    if (tokens.length > 1) {
      String token = tokens[1].trim();

      String name = null;
      String value = null;

      String nameToken = null;

      // Get the value
      Matcher matcher = Pattern.compile("\\svalue($|\\s)").matcher(token.toLowerCase());
      if (matcher.find()) {
        value = token.substring(matcher.end()).trim();
        if (value.isEmpty()) {
          throw new ParseException("Error in setoption command: missing parameter after value");
        }

        nameToken = token.substring(0, matcher.start());
      } else {
        nameToken = token;
      }

      // Get the name
      matcher = Pattern.compile("(^|\\s)name\\s").matcher(nameToken.toLowerCase());
      if (matcher.find()) {
        name = nameToken.substring(matcher.end()).trim();
        if (name.isEmpty()) {
          throw new ParseException("Error in setoption command: missing parameter after name");
        }
      } else {
        throw new ParseException("Error in setoption command: missing option name");
      }

      if (value != null) {
        queue.add(new EngineSetOptionCommand(name, value));
      } else {
        queue.add(new EngineSetOptionCommand(name, null));
      }
    } else {
      throw new ParseException("Error in setoption command: no parameters specified");
    }
  }

  private void parsePositionCommand(String[] tokens) throws ParseException {
    assert tokens != null;

    if (tokens.length > 1) {
      List<String> list = getTokens(tokens[1]);
      assert !list.isEmpty();

      Iterator<String> iter = list.iterator();
      String token = iter.next();
      GenericBoard board = null;

      if (token.equalsIgnoreCase("startpos")) {
        board = new GenericBoard(GenericBoard.STANDARDSETUP);

        if (iter.hasNext()) {
          token = iter.next();
          if (!token.equalsIgnoreCase("moves")) {
            // Somethings really wrong here...
            throw new ParseException("Error in position command: unknown keyword " + token + " after startpos");
          } else if (!iter.hasNext()) {
            throw new ParseException("Error in position command: missing moves");
          }
        }
      } else if (token.equalsIgnoreCase("fen")) {
        String fen = "";

        while (iter.hasNext()) {
          token = iter.next();
          if (token.equalsIgnoreCase("moves")) {
            if (!iter.hasNext()) {
              throw new ParseException("Error in position command: missing moves");
            }

            break;
          }

          fen += token + " ";
        }

        try {
          board = new GenericBoard(fen);
        } catch (IllegalNotationException e) {
          throw new ParseException("Error in position command: illegal fen notation " + fen);
        }
      }

      assert board != null;

      List<GenericMove> moveList = new ArrayList<GenericMove>();

      try {
        while (iter.hasNext()) {
          token = iter.next();

          GenericMove move = new GenericMove(token);
          moveList.add(move);
        }

        queue.add(new EngineAnalyzeCommand(board, moveList));
      } catch (IllegalNotationException e) {
        throw new ParseException("Error in position command: illegal move notation " + token);
      }
    } else {
      throw new ParseException("Error in position command: no parameters specified");
    }
  }

  private void parseGoCommand(String[] tokens) throws ParseException {
    assert tokens != null;

    EngineStartCalculatingCommand engineCommand = new EngineStartCalculatingCommand();

    if (tokens.length > 1) {
      List<String> list = getTokens(tokens[1]);
      assert !list.isEmpty();

      Iterator<String> iter = list.iterator();
      while (iter.hasNext()) {
        String token = iter.next();

        if (token.equalsIgnoreCase("searchmoves")) {
          if (iter.hasNext()) {
            List<GenericMove> searchMoveList = new ArrayList<GenericMove>();

            try {
              while (iter.hasNext()) {
                token = iter.next();

                GenericMove move = new GenericMove(token);
                searchMoveList.add(move);
              }

              engineCommand.setSearchMoveList(searchMoveList);
            } catch (IllegalNotationException e) {
              throw new ParseException("Error in position command: illegal move notation " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing searchmoves value");
          }
        } else if (token.equalsIgnoreCase("ponder")) {
          engineCommand.setPonder();
        } else if (token.equalsIgnoreCase("wtime")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setClock(GenericColor.WHITE, new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing wtime value");
          }
        } else if (token.equalsIgnoreCase("btime")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setClock(GenericColor.BLACK, new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing btime value");
          }
        } else if (token.equalsIgnoreCase("winc")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setClockIncrement(GenericColor.WHITE, new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing winc value");
          }
        } else if (token.equalsIgnoreCase("binc")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setClockIncrement(GenericColor.BLACK, new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing binc value");
          }
        } else if (token.equalsIgnoreCase("movestogo")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setMovesToGo(new Integer(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing movestogo value");
          }
        } else if (token.equalsIgnoreCase("depth")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setDepth(new Integer(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing depth value");
          }
        } else if (token.equalsIgnoreCase("nodes")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setNodes(new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing nodes value");
          }
        } else if (token.equalsIgnoreCase("mate")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setMate(new Integer(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing mate value");
          }
        } else if (token.equalsIgnoreCase("movetime")) {
          if (iter.hasNext()) {
            token = iter.next();
            try {
              engineCommand.setMoveTime(new Long(token));
            } catch (NumberFormatException e) {
              throw new ParseException("Error in go command: incorrect number format " + token);
            }
          } else {
            throw new ParseException("Error in go command: missing movetime value");
          }
        } else if (token.equalsIgnoreCase("infinite")) {
          engineCommand.setInfinite();
        }
      }
    }

    queue.add(engineCommand);
  }

  public void send(ProtocolInitializeAnswerCommand command) {
    output.println("id name " + command.name);
    output.println("id author " + command.author);

    for (Iterator<AbstractOption> iter = command.optionIterator(); iter.hasNext();) {
      AbstractOption option = iter.next();
      output.println(option);
    }

    output.println("uciok");
  }

  public void send(ProtocolReadyAnswerCommand command) {
    output.println("readyok");
  }

  public void send(ProtocolBestMoveCommand command) {
    String bestMoveCommand = "bestmove ";

    if (command.bestMove != null) {
      bestMoveCommand += command.bestMove.toString();

      if (command.ponderMove != null) {
        bestMoveCommand += " ponder " + command.ponderMove.toString();
      }
    } else {
      bestMoveCommand += "nomove";
    }

    output.println(bestMoveCommand);
  }

  public void send(ProtocolInformationCommand command) {
    String infoCommand = "info";

    if (command.getPvNumber() != null) {
      infoCommand += " multipv " + command.getPvNumber().toString();
    }
    if (command.getDepth() != null) {
      infoCommand += " depth " + command.getDepth().toString();

      if (command.getMaxDepth() != null) {
        infoCommand += " seldepth " + command.getMaxDepth().toString();
      }
    }
    if (command.getMate() != null) {
      infoCommand += " score mate " + command.getMate().toString();
    } else if (command.getCentipawns() != null) {
      infoCommand += " score cp " + command.getCentipawns().toString();
    }
    if (command.getValue() != null) {
      switch (command.getValue()) {
        case EXACT:
          break;
        case ALPHA:
          infoCommand += " upperbound";
          break;
        case BETA:
          infoCommand += " lowerbound";
          break;
        default:
          assert false : command.getValue();
      }
    }
    if (command.getMoveList() != null) {
      infoCommand += " pv";
      for (GenericMove move : command.getMoveList()) {
        infoCommand += " ";
        infoCommand += move.toString();
      }
    }
    if (command.getRefutationList() != null) {
      infoCommand += " refutation";
      for (GenericMove move : command.getRefutationList()) {
        infoCommand += " ";
        infoCommand += move.toString();
      }
    }
    if (command.getCurrentMove() != null) {
      infoCommand += " currmove " + command.getCurrentMove().toString();
    }
    if (command.getCurrentMoveNumber() != null) {
      infoCommand += " currmovenumber " + command.getCurrentMoveNumber().toString();
    }
    if (command.getHash() != null) {
      infoCommand += " hashfull " + command.getHash().toString();
    }
    if (command.getNps() != null) {
      infoCommand += " nps " + command.getNps().toString();
    }
    if (command.getTime() != null) {
      infoCommand += " time " + command.getTime().toString();
    }
    if (command.getNodes() != null) {
      infoCommand += " nodes " + command.getNodes().toString();
    }
    if (command.getString() != null) {
      infoCommand += " string " + command.getString();
    }

    output.println(infoCommand);
  }

  private List<String> getTokens(String s) {
    List<String> tokens = new ArrayList<String>(Arrays.asList(s.trim().split("\\s")));

    for (Iterator<String> iter = tokens.iterator(); iter.hasNext();) {
      // Remove empty tokens
      if (iter.next().length() == 0) {
        iter.remove();
      }
    }

    return tokens;
  }

}
