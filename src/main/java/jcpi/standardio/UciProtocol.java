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
package jcpi.standardio;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

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
import jcpi.commands.GuiBestMoveCommand;
import jcpi.commands.GuiInformationCommand;
import jcpi.commands.GuiInitializeAnswerCommand;
import jcpi.commands.GuiQuitCommand;
import jcpi.commands.GuiReadyAnswerCommand;
import jcpi.commands.IEngineCommand;
import jcpi.data.GenericBoard;
import jcpi.data.GenericColor;
import jcpi.data.GenericMove;
import jcpi.data.IllegalNotationException;
import jcpi.data.Option;


/**
 * UciProtocol
 *
 * @author Phokham Nonava
 */
public final class UciProtocol extends AbstractStandardIoProtocol {

    /**
     * Creates a new UciProtocol.
     *
     * @param writer the standard output.
     * @param queue the engine command queue.
     */
    public UciProtocol(PrintStream writer, Queue<IEngineCommand> queue) {
        super(writer, queue);

        this.queue.add(new EngineInitializeRequestCommand());
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jcpi.standardio.AbstractStandardIoProtocol#parse(java.util.List)
     */
    protected void parse(List<String> tokenList) {
        if (tokenList == null) throw new IllegalArgumentException();

        for (Iterator<String> iter = tokenList.iterator(); iter.hasNext();) {
            String token = iter.next();

            if (token.equalsIgnoreCase("debug")) {
                parseDebugCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("isready")) {
                this.queue.add(new EngineReadyRequestCommand());
                break;
            } else if (token.equalsIgnoreCase("setoption")) {
                parseSetOptionCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("register")) {
                // Do nothing
                break;
            } else if (token.equalsIgnoreCase("ucinewgame")) {
                this.queue.add(new EngineNewGameCommand());
                break;
            } else if (token.equalsIgnoreCase("position")) {
                parsePositionCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("go")) {
                parseGoCommand(iter);
                break;
            } else if (token.equalsIgnoreCase("stop")) {
                this.queue.add(new EngineStopCalculatingCommand());
                break;
            } else if (token.equalsIgnoreCase("ponderhit")) {
                this.queue.add(new EnginePonderHitCommand());
                break;
            } else if (token.equalsIgnoreCase("quit")) {
                this.queue.add(new EngineQuitCommand());
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
                this.queue.add(new EngineDebugCommand(false, true));
            } else if (token.equalsIgnoreCase("off")) {
                this.queue.add(new EngineDebugCommand(false, false));
            } else {
                error("Error in debug command: unknown parameter " + token);
            }
        } else {
            this.queue.add(new EngineDebugCommand(true, false));
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

                        this.queue.add(new EngineSetOptionCommand(name, value));
                    } else {
                        error("Error in setoption command: missing option value");
                    }
                } else {
                    this.queue.add(new EngineSetOptionCommand(name, null));
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

                    this.queue.add(new EngineAnalyzeCommand(board, moveList));
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
            this.queue.add(engineCommand);
        }
    }

    public void visit(GuiInitializeAnswerCommand command) {
        this.writer.println("id name " + command.name);
        this.writer.println("id author " + command.author);

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

            this.writer.println(optionString);
        }

        this.writer.println("uciok");
    }

    public void visit(GuiReadyAnswerCommand command) {
        this.writer.println("readyok");
    }

    public void visit(GuiBestMoveCommand command) {
        String bestMoveCommand = "bestmove ";

        if (command.bestMove != null) {
            bestMoveCommand += command.bestMove.toString();

            if (command.ponderMove != null) {
                bestMoveCommand += " ponder " + command.ponderMove.toString();
            }
        } else {
            bestMoveCommand += "nomove";
        }

        this.writer.println(bestMoveCommand);
    }

    public void visit(GuiInformationCommand command) {
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

        this.writer.println(infoCommand);
    }

    public void visit(GuiQuitCommand command) {
        throw new IllegalStateException();
    }

}
