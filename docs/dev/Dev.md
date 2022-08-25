# Development
If you are a developer, you can use the indications below to have a maximum of information when you develop.

## Install packages for completions

First clone the repository

```sh
git clone https://github.com/CoFab-in-Bondy/gamaSenseIt.git gamaSenseIt
cd gamaSenseIt
```

Download the javadoc from maven
```sh
./mvnw dependency:sources
```

Install npm package 
```sh
(cd ui && npm install)
```

## Install Intellij

This project was developed on Intellij, if u want full support please install it.

[Download Intellij IDEA](https://www.jetbrains.com/fr-fr/idea/download)

## Make a maven wrapper

If you want rebuild the maven wrapper use the command bellow.
```
mvn -N io.takari:maven:wrapper -Dmaven=3.8.5
```

## Build documentation

To build the documentation please install miktex :

[Install MiKTeX](https://miktex.org/download)
