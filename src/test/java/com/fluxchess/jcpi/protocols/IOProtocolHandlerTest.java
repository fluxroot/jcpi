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

import com.fluxchess.jcpi.commands.ProtocolInitializeAnswerCommand;
import org.junit.Test;

import java.io.*;

public class IOProtocolHandlerTest {

  @Test(expected = NoProtocolException.class)
  public void testSend() {
    String[] commands = {""};
    IOProtocolHandler handler = createIOProtocolHandler(commands);

    handler.send(new ProtocolInitializeAnswerCommand("Engine 1.0", "Author"));
  }

  private IOProtocolHandler createIOProtocolHandler(String[] commands) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    PrintStream stream = new PrintStream(buffer);
    try {
      for (String command : commands) {
        stream.println(command);
      }
    } finally {
      if (buffer != null) {
        try {
          buffer.close();
        } catch (IOException e) {
          // Do nothing
        }
      }
    }

    return new IOProtocolHandler(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray()))), new PrintStream(new ByteArrayOutputStream()));
  }

}
