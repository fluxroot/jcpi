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

import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UciProtocolTest {

    private List<String> getTokens(String command) {
        List<String> tokens = new ArrayList<String>(Arrays.asList(command.split(" ")));
        for (Iterator<String> iter = tokens.iterator(); iter.hasNext();) {
            String token = iter.next();

            if (token.length() == 0) {
                iter.remove();
            }
        }

        return tokens;
    }

    @Test
    public void testGetCommand() {
        UciProtocol protocol = new UciProtocol(new BufferedReader(new InputStreamReader(System.in)), System.out);

        String command = "position startpos";
        protocol.parse(getTokens(command));
        assertEquals(false, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "a b c d position startpos";
        protocol.parse(getTokens(command));
        assertEquals(false, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "a b c d position startpos moves a2a3";
        protocol.parse(getTokens(command));
        assertEquals(false, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "a b c d position startpos x y z";
        protocol.parse(getTokens(command));
        assertEquals(true, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "setoption";
        protocol.parse(getTokens(command));
        assertEquals(true, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "setoption name";
        protocol.parse(getTokens(command));
        assertEquals(true, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "setoption name Clear Hash";
        protocol.parse(getTokens(command));
        assertEquals(false, protocol.queue.isEmpty());
        assertEquals(1, protocol.queue.size());
        assertEquals("Clear Hash", ((EngineSetOptionCommand) protocol.queue.peek()).name);
        protocol.queue.clear();

        command = "setoption name Clear Hash value";
        protocol.parse(getTokens(command));
        assertEquals(true, protocol.queue.isEmpty());
        protocol.queue.clear();

        command = "setoption name Clear Hash value 5";
        protocol.parse(getTokens(command));
        assertEquals(false, protocol.queue.isEmpty());
        assertEquals(1, protocol.queue.size());
        assertEquals("Clear Hash", ((EngineSetOptionCommand) protocol.queue.peek()).name);
        assertEquals("5", ((EngineSetOptionCommand) protocol.queue.peek()).value);
        protocol.queue.clear();
    }

}
