/**
 * GuiInitializeAnswerCommand.java
 * 
 * Copyright 2007 Java Chess Protocol Interface Project
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
package jcpi.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jcpi.IGui;
import jcpi.data.Option;


/**
 * GuiInitializeAnswerCommand
 *
 * @author Phokham Nonava
 */
public class GuiInitializeAnswerCommand implements IGuiCommand {

	public final String name;
	public final String author;
	private final List<Option> optionList = new ArrayList<Option>();

	public GuiInitializeAnswerCommand(String name, String author) {
		if (name == null) throw new IllegalArgumentException();
		if (author == null) throw new IllegalArgumentException();
		
		this.name = name;
		this.author = author;
	}

	public void accept(IGui v) {
		v.visit(this);
	}
	
	public Iterator<Option> optionIterator() {
		return this.optionList.iterator();
	}
	
	public void addOption(Option option) {
		if (option == null) throw new IllegalArgumentException();
		
		this.optionList.add(option);
	}

}
