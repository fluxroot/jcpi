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
package com.fluxchess.jcpi.options;

public final class ComboboxOption extends AbstractValueOption {

  public final String[] varValues;

  public ComboboxOption(String name, String defaultValue, String[] varValues) {
    super(name, defaultValue);

    this.varValues = varValues;
  }

  @Override
  protected String type() {
    return "combo";
  }

  @Override
  public String toString() {
    String s = super.toString();

    for (String value : varValues) {
      s += String.format(" var %s", value);
    }

    return s;
  }

}
