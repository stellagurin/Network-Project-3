JCC = javac

default: Traceroute.class

Traceroute.class: Traceroute.java
		$(JCC) -g Traceroute.java

run:
		java Traceroute

clean:
		$(RM) *.class