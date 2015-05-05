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
package com.fluxchess.jcpi.commands;

import com.fluxchess.jcpi.options.AbstractOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProtocolInitializeAnswerCommand implements IProtocolCommand {

  public final String name;
  public final String author;
  private final List<AbstractOption> options = new ArrayList<AbstractOption>();

  public ProtocolInitializeAnswerCommand(String name, String author) {
    if (name == null) throw new IllegalArgumentException();
    if (author == null) throw new IllegalArgumentException();

    this.name = name;
    this.author = author;
  }

  public void accept(IProtocol protocol) {
    protocol.send(this);
  }

  public Iterator<AbstractOption> optionIterator() {
    return this.options.iterator();
  }

  public void addOption(AbstractOption option) {
    if (option == null) throw new IllegalArgumentException();

    this.options.add(option);
  }

}
