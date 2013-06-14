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
package jcpi.data;

import java.util.List;
import java.util.Objects;

public class Option {

    public final String name;
    public final String type;
    public final String defaultValue;
    public final String minValue;
    public final String maxValue;
    public final List<String> varValues;

    private String value = null;

    public Option(String name, String type, String defaultValue, String minValue, String maxValue, List<String> varValues) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.varValues = varValues;

        this.value = defaultValue;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        Objects.requireNonNull(value);

        this.value = value;
    }

}
