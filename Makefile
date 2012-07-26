CC=javac
CFLAGS=-source 1.4 -d cls -sourcepath src
#-classpath cls;lib/fdsion


all:
	$(CC) $(CFLAGS) src/*.java


clean:
	rm cls/*.class
