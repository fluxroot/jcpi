Java Chess Protocol Interface
=============================

Copyright 2007-2013 Flux Chess Project  
github.com/fluxchess/jcpi


Introduction
------------
The Java Chess Protocol Interface aims to provide a standard Java
interface to all possible chess protocols. Mainly to [UCI] and to
[Winboard/XBoard]. Currently only UCI is fully supported.


Build it
--------
The Java Chess Protocol Interface uses [Gradle] as build system. To
build it from source, use the following steps.

- get it  
`git clone https://github.com/fluxchess/jcpi.git`

- build it  
`./gradlew build`

- grab it  
`cp build/distributions/jcpi-$version$.zip <installation directory>`


License
-------
The Java Chess Protocol Interface is released under version 2.0 of the
[Apache License].


[UCI]: http://wbec-ridderkerk.nl/html/UCIProtocol.html
[Winboard/XBoard]: http://www.open-aurec.com/wbforum/WinBoard/engine-intf.html
[Gradle]: http://gradle.org
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
