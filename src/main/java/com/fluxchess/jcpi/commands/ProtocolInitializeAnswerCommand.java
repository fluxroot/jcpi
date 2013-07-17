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
package com.fluxchess.jcpi.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fluxchess.jcpi.models.Option;

public class ProtocolInitializeAnswerCommand implements IProtocolCommand {

    public final String name;
    public final String author;
    private final List<Option> optionList = new ArrayList<Option>();

    public ProtocolInitializeAnswerCommand(String name, String author) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(author);

        this.name = name;
        this.author = author;
    }

    public void accept(IProtocol v) {
        v.send(this);
    }

    public Iterator<Option> optionIterator() {
        return this.optionList.iterator();
    }

    public void addOption(Option option) {
        Objects.requireNonNull(option);

        this.optionList.add(option);
    }

}
