# cfggen

![intro](docs/assets/intro.png)

an object database viewer, editor, reading code generator

* define object schema.
* use Excel file to edit and store object. 
* use node based gui editor to view and edit object, store as json file
* generate reading code.

## Features

* polymorphic and nested structure.
* primary/unique key, foreign key, enum, entry. and generate code accordingly.  

## Documentation

read [document](https://stallboy.github.io/cfggen)


## Prerequisites

* jdk21
* gradle
* set git/bin directory to Path

## build & test

### build, generate cfggen.jar.

```bash
genjar.bat
```

### test

first `cd example` 

* show usage 

```bash
usage.bat  
```

* generate java code

```bash
cd example
genjava.bat 
```

* test java code

```bash
gradle build 
java -jar build/libs/example.jar 
```

* generate & test lua

```bash
genlua.bat 
cd lua
chcp 65001
lua.exe test.lua
```

* generate & test c#

```bash
gencshape.bat 
cd cs
run.bat
```

* generate & test go

```bash
gengo.bat 
cd go
go run .
```

* generate & test typescript

```bash
gents.bat 
cd ts
pnpm i -D tsx
npx tsx main.ts
```

## cfgeditor.exe
read [(cfgeditor readme)](cfgeditor/README_EN.md)
