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

import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineInitializeRequestCommand;
import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.fluxchess.jcpi.commands.IEngineCommand;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UciProtocolTest {

    @Test
    public void testPosition1() throws IOException {
        String[] commands = {"position startpos"};
        UciProtocol protocol = createUciProtocol(commands);

        IEngineCommand command = protocol.receive();
        assertEquals(EngineInitializeRequestCommand.class, command.getClass());

        command = protocol.receive();
        assertEquals(EngineAnalyzeCommand.class, command.getClass());

        try {
            command = protocol.receive();
            fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testPosition2() throws IOException {
        String[] commands = {"a b c d position startpos"};
        UciProtocol protocol = createUciProtocol(commands);

        IEngineCommand command = protocol.receive();
        assertEquals(EngineInitializeRequestCommand.class, command.getClass());

        command = protocol.receive();
        assertEquals(EngineAnalyzeCommand.class, command.getClass());

        try {
            command = protocol.receive();
            fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testPosition3() throws IOException {
        String[] commands = {"a b c d position startpos moves a2a3"};
        UciProtocol protocol = createUciProtocol(commands);

        IEngineCommand command = protocol.receive();
        assertEquals(EngineInitializeRequestCommand.class, command.getClass());

        command = protocol.receive();
        assertEquals(EngineAnalyzeCommand.class, command.getClass());

        try {
            command = protocol.receive();
            fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testPosition4() throws IOException {
        String[] commands = {"a b c d position startpos x y z"};
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
    public void testSetOption1() throws IOException {
        String[] commands = {"setoption"};
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
    public void testSetOption2() throws IOException {
        String[] commands = {"setoption name"};
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
    public void testSetOption3() throws IOException {
        String[] commands = {"setoption name Clear Hash"};
        UciProtocol protocol = createUciProtocol(commands);

        IEngineCommand command = protocol.receive();
        assertEquals(EngineInitializeRequestCommand.class, command.getClass());

        command = protocol.receive();
        assertEquals(EngineSetOptionCommand.class, command.getClass());
        assertEquals(((EngineSetOptionCommand)command).name, "Clear Hash");

        try {
            command = protocol.receive();
            fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testSetOption4() throws IOException {
        String[] commands = {"setoption name Clear Hash value"};
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
    public void testSetOption5() throws IOException {
        String[] commands = {"setoption name Clear Hash value 5"};
        UciProtocol protocol = createUciProtocol(commands);

        IEngineCommand command = protocol.receive();
        assertEquals(EngineInitializeRequestCommand.class, command.getClass());

        command = protocol.receive();
        assertEquals(EngineSetOptionCommand.class, command.getClass());
        assertEquals(((EngineSetOptionCommand)command).name, "Clear Hash");
        assertEquals(((EngineSetOptionCommand)command).value, "5");

        try {
            command = protocol.receive();
            fail();
        } catch (IOException e) {
        }
    }

    private UciProtocol createUciProtocol(String[] commands) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream stream = new PrintStream(buffer)) {
            for (String command : commands) {
                stream.println(command);
            }
        }

        return new UciProtocol(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray()))), new PrintStream(new ByteArrayOutputStream()));
    }

}
