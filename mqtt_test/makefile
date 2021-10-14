test_mqtt: mqtt_test.o
	gcc -lrt -lpaho-mqtt3c  mqtt_test.o -o test_mqtt	

mqtt_test.o: test.c
	gcc $(CFLAGS) -DRASPBERRY -DIS_RCV_GATEWAY -c test.c -o mqtt_test.o
		
lib: mqtt_test.o

clean:
	rm *.o test_mqtt
