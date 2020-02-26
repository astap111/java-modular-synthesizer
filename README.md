# java-modular-synthesizer
It's a Modular Synthesizer written in java

# How to run
Please clone this repository and run maven goal 'mvn clean package' (use java 8).
You can find the jar under 'target/' directory.

Run the following command: 

java -jar target/SimpleSynth-1.0-SNAPSHOT-jar-with-dependencies.jar

Please use keys Z to / and Q to ] as whitekeys, A to \' and numbers as black keys.

Today only 1 real sunthesizer was simulated - Moog Grandmother. Almost all was done except patching (this is the next step to code after I make reverb channel)

# Goals
1) Learn JavaFX
2) Make base Synthesis core to be able to combine all modules to a Synthesizer of any complexity
3) Make devs aware of Sound Synthesis, give a real example
