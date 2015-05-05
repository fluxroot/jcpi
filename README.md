Java Chess Protocol Interface
=============================

Copyright 2007-2015 Flux Chess Project  
http://fluxchess.com

[![Build Status](https://travis-ci.org/fluxroot/jcpi.png?branch=1.4.x)](https://travis-ci.org/fluxroot/jcpi) [![Coverage Status](https://coveralls.io/repos/fluxroot/jcpi/badge.png?branch=1.4.x)](https://coveralls.io/r/fluxroot/jcpi?branch=1.4.x)


Introduction
------------
The Java Chess Protocol Interface provides a clean object-oriented interface to
the [UCI] protocol. It handles all the standard I/O communication and creates
well defined Java objects for the engine to consume.


Use it
------
Inside the distribution zip you'll find the JCPI jar. Add it to your engine
project as an additional dependency and extend the `AbstractEngine` class. The
JCPI jar is also available from our Maven repository.

To use it in Maven use the following code:

    <repositories>
        <repository>
            <id>maven.fluxchess.com</id>
            <url>http://maven.fluxchess.com/release</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.fluxchess</groupId>
        <artifactId>jcpi</artifactId>
        <version>1.4.0</version>
    </dependency>

To use it in Gradle use the following code:

    repositories {
        maven {
            url 'http://maven.fluxchess.com/release'
        }
    }
    dependencies {
        compile 'com.fluxchess:jcpi:1.4.+'
    }


Build it
--------
The Java Chess Protocol Interface uses [Gradle] as build system. To build it
from source, use the following steps.

- get it  
`git clone https://github.com/fluxroot/jcpi.git`

- build it  
`./gradlew build`

- grab it  
`cp build/distributions/jcpi-<version>.zip <installation directory>`


License
-------
The Java Chess Protocol Interface is released under version 2.0 of the
[Apache License].


[UCI]: http://www.shredderchess.com/chess-info/features/uci-universal-chess-interface.html
[Gradle]: http://gradle.org/
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
