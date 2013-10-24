/*
 * Copyright 2007-2013 the original author or authors.
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
import com.fluxchess.jcpi.models.*;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public final class UciProtocol implements IProtocolHandler {

    private final BufferedReader input;
    private final PrintStream output;

    final Queue<IEngineCommand> queue = new LinkedList<>();

    public static boolean isProtocolKeyword(String token) {
        if (token.equalsIgnoreCase("uci")) {
            return true;
        } else {
            return false;
        }
    }

    public UciProtocol(BufferedReader input, PrintStream output) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);

        this.input = input;
        this.output = output;

        queue.add(new EngineInitializeRequestCommand());
    }

    public IEngineCommand receive() throws IOException {
        // Get the next command from the queue
        IEngineCommand engineCommand = queue.poll();
        while (engineCommand == null) {
            // Read from the standard input
            String tokenString = input.readLine();
            if (tokenString != null) {
                tokenString = tokenString.trim();

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
                parse(tokenList);

                // Get the next command from the queue
                engineCommand = queue.poll();
            } else {
                // Something's wrong with the communication channel
                throw new EOFException();
            }
        }

        assert engineCommand != null;
        return engineCommand;
    }

    void parse(List<String> tokenList) {
        Objects.requireNonNull(tokenList);

        for (Iterator<String> iter = tokenList.iterator(); iter.hasNext();) {
            String token = iter.next();

            if (token.equalsIgnoreCase("debug")) {
                parseDebugCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("isready")) {
                queue.add(new EngineReadyRequestCommand());
                break;
            } else if (token.equalsIgnoreCase("setoption")) {
                parseSetOptionCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("register")) {
                // Do nothing
                break;
            } else if (token.equalsIgnoreCase("ucinewgame")) {
                queue.add(new EngineNewGameCommand());
                break;
            } else if (token.equalsIgnoreCase("position")) {
                parsePositionCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("go")) {
                parseGoCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("stop")) {
                queue.add(new EngineStopCalculatingCommand());
                break;
            } else if (token.equalsIgnoreCase("ponderhit")) {
                queue.add(new EnginePonderHitCommand());
                break;
            } else if (token.equalsIgnoreCase("quit")) {
                queue.add(new EngineQuitCommand());
                break;
            }
        }
    }

    private void error(String message) {
        // Currently ignore errors in token stream
    }

    private void parseDebugCommand(Iterator<String> iter) {
        assert iter != null;

        if (iter.hasNext()) {
            String token = iter.next();

            if (token.equalsIgnoreCase("on")) {
                queue.add(new EngineDebugCommand(false, true));
            } else if (token.equalsIgnoreCase("off")) {
                queue.add(new EngineDebugCommand(false, false));
            } else {
                error("Error in debug command: unknown parameter " + token);
            }
        } else {
            queue.add(new EngineDebugCommand(true, false));
        }
    }

    private void parseSetOptionCommand(Iterator<String> iter) {
        assert iter != null;

        String name = null;
        String value = null;

        if (iter.hasNext()) {
            String token = iter.next();

            if (token.equalsIgnoreCase("name") && iter.hasNext()) {
                boolean hasValue = false;

                name = iter.next();
                while (iter.hasNext()) {
                    token = iter.next();
                    if (token.equalsIgnoreCase("value")) {
                        hasValue = true;
                        break;
                    } else {
                        // Token is part of the name
                        name += " " + token;
                    }
                }

                if (hasValue) {
                    if (iter.hasNext()) {
                        value = iter.next();
                        while (iter.hasNext()) {
                            token = iter.next();
                            value += " " + token;
                        }

                        queue.add(new EngineSetOptionCommand(name, value));
                    } else {
                        error("Error in setoption command: missing option value");
                    }
                } else {
                    queue.add(new EngineSetOptionCommand(name, null));
                }
            } else {
                error("Error in setoption command: missing option name");
            }
        } else {
            error("Error in setoption command: no parameters specified");
        }
    }

    private void parsePositionCommand(Iterator<String> iter) {
        assert iter != null;

        if (iter.hasNext()) {
            String token = iter.next();
            GenericBoard board = null;

            if (token.equalsIgnoreCase("startpos")) {
                board = new GenericBoard(GenericBoard.STANDARDSETUP);

                if (iter.hasNext()) {
                    token = iter.next();
                    if (!token.equalsIgnoreCase("moves")) {
                        // Somethings really wrong here...
                        board = null;
                        error("Error in position command: unknown keyword " + token + " after startpos");
                    }
                }
            } else if (token.equalsIgnoreCase("fen")) {
                String fen = "";

                while (iter.hasNext()) {
                    token = iter.next();
                    if (token.equalsIgnoreCase("moves")) {
                        break;
                    }

                    fen += token + " ";
                }

                try {
                    board = new GenericBoard(fen);
                } catch (IllegalNotationException e) {
                    error("Error in position command: illegal fen notation " + fen);
                }
            }

            if (board != null) {
                List<GenericMove> moveList = new ArrayList<GenericMove>();

                try {
                    while (iter.hasNext()) {
                        token = iter.next();

                        GenericMove move = new GenericMove(token);
                        moveList.add(move);
                    }

                    queue.add(new EngineAnalyzeCommand(board, moveList));
                } catch (IllegalNotationException e) {
                    error("Error in position command: illegal move notation " + token);
                }
            }
        } else {
            error("Error in position command: no parameters specified");
        }
    }

    private void parseGoCommand(Iterator<String> iter) {
        assert iter != null;

        EngineStartCalculatingCommand engineCommand = new EngineStartCalculatingCommand();

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
                        error("Error in position command: illegal move notation " + token);
                    }
                } else {
                    error("Error in go command: missing searchmoves value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("ponder")) {
                engineCommand.setPonder();
            } else if (token.equalsIgnoreCase("wtime")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setClock(GenericColor.WHITE, new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing wtime value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("btime")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setClock(GenericColor.BLACK, new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing btime value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("winc")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setClockIncrement(GenericColor.WHITE, new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing winc value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("binc")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setClockIncrement(GenericColor.BLACK, new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing binc value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("movestogo")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setMovesToGo(new Integer(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing movestogo value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("depth")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setDepth(new Integer(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing depth value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("nodes")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setNodes(new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing nodes value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("mate")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setMate(new Integer(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing mate value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("movetime")) {
                if (iter.hasNext()) {
                    token = iter.next();
                    try {
                        engineCommand.setMoveTime(new Long(token));
                    } catch (NumberFormatException e) {
                        error("Error in go command: incorrect number format " + token);
                        engineCommand = null;
                        break;
                    }
                } else {
                    error("Error in go command: missing movetime value");
                    engineCommand = null;
                    break;
                }
            } else if (token.equalsIgnoreCase("infinite")) {
                engineCommand.setInfinite();
            }
        }

        if (engineCommand != null) {
            queue.add(engineCommand);
        }
    }

    public void send(ProtocolInitializeAnswerCommand command) {
        output.println("id name " + command.name);
        output.println("id author " + command.author);

        for (Iterator<Option> iter = command.optionIterator(); iter.hasNext();) {
            Option option = iter.next();

            String optionString = "option";
            optionString += " name " + option.name;
            optionString += " type " + option.type;

            if (option.getValue() != null) {
                optionString += " default " + option.getValue();
            } else if (option.defaultValue != null) {
                optionString += " default " + option.defaultValue;
            }
            if (option.minValue != null) {
                optionString += " min " + option.minValue;
            }
            if (option.maxValue != null) {
                optionString += " max " + option.maxValue;
            }
            if (option.varValues != null) {
                for (String varValue : option.varValues) {
                    optionString += " var " + varValue;
                }
            }

            output.println(optionString);
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

}
