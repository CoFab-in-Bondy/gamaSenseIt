/*
 * MqttProbe.h
 *
 *  Created on: 14 mai 2017
 *      Author: nicolas
 */

#ifndef MQTTPROBE_H_
#define MQTTPROBE_H_

#include <stdlib.h>
#include <string>

#define ADDRESS     "tcp://localhost:1883"
#define CLIENTID    "ExampleClientPub"
#define TOPIC       "MQTT Examples"
#define PAYLOAD     "Hello World!"
#define QOS         1
#define TIMEOUT     10000L

using namespace std;

class MqttProbe : public Probe
{
private:
char* address = ADDRESS;
char* clientID = CLIENTID;
string topic = TOPIC;
int qos = QOS;
long timeout = TIMEOUT;

public:
MqttProbe(string clientId, string server );
void saveMessage(Message m);

};



#endif /* MQTTPROBE_H_ */
