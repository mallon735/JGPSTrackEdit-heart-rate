build:
	mvn package
clean:
	rm -r target
run:
	java -jar target/JGPSTrackEdit-*-bundle.jar
