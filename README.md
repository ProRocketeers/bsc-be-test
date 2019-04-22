# Payment Tracker

This program keeps records of payments. The program outputs a list of all the currencies and amounts 
to the console once per minute and allow the user to add more records of transactions in runtime 
by inputting them to console. It also allows the user to optionally specify path to a file including 
transactions to be loaded to the program at start up.  

## Assumptions we take in the program 
- You can input only (all) decimal numbers it means eg.: 123.345 is allowed
- You output only rounded amounts (we are using roundingUp on half eg.: 0.5 => 1)
 
Sample input:
```
USD 1000
HKD 100
USD -100
RMB 2000
HKD 200
```
Sample output:
```
USD 900
RMB 2000
HKD 300
```

## Build

### Prerequisites
- Java 11 or newer
- Maven

In order to build the program, in terminal, go to root of the project and run ` mvn clean compile assembly:single`,
compiled binaries are then in target folder.

## Run

### Prerequisites
- Java 11 or newer

In project root, run in terminal `java -jar target/bsc-1.0.0-jar-with-dependencies.jar`

You can optionally specify path to a file containing payment records as first argument 
`java -jar target/bsc-1.0.0-jar-with-dependencies.jar src/test/resources/data.txt` 

## Develop

### Prerequisites
- Java 11 or newer
- Maven
- Lombok plugin -> Please install particular plugin for your IDE

#### Used features documentation
- https://projectlombok.org/features/val
- https://projectlombok.org/features/Data
- https://projectlombok.org/features/log
