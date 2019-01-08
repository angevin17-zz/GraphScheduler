make:
	javac -cp .:./json-simple-1.1.1.jar *.java
	java -cp .:./json-simple-1.1.1.jar Main

test:
	javac -cp .:./json-simple-1.1.1.jar *.java
	java -cp .:./json-simple-1.1.1.jar TestGraph

clean:
	\rm *.class
