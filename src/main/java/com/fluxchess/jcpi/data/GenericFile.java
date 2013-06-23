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
package com.fluxchess.jcpi.data;

public enum GenericFile {

    Fa('a'),
    Fb('b'),
    Fc('c'),
    Fd('d'),
    Fe('e'),
    Ff('f'),
    Fg('g'),
    Fh('h');

    private final char token;

    private GenericFile(char token) {
        this.token = token;
    }

    public static GenericFile valueOf(char input) {
        for (GenericFile file : values()) {
            if (Character.toLowerCase(input) == Character.toLowerCase(file.token)) {
                return file;
            }
        }

        return null;
    }

    public GenericFile prev() {
        return prev(1);
    }

    public GenericFile prev(int i) {
        if (i < 0) throw new IllegalArgumentException();

        int position = this.ordinal() - i;
        if (position >= 0) {
            return values()[position];
        } else {
            return null;
        }
    }

    public GenericFile next() {
        return next(1);
    }

    public GenericFile next(int i) {
        if (i < 0) throw new IllegalArgumentException();

        int position = this.ordinal() + i;
        if (position < values().length) {
            return values()[position];
        } else {
            return null;
        }
    }

    public char toChar() {
        return this.token;
    }

    public String toString() {
        return Character.toString(this.token);
    }

}
