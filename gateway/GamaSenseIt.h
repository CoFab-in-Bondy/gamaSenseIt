#ifndef GAMA_SENSE_IT
#define GAMA_SENSE_IT

#include "SX1272.h"
#include <unistd.h>

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <time.h>
#include <fstream>
#include <sstream>
#include <map>
#include <cstring>
#include "MQTTClient.h"

#define NB_RETRIES 10
//#define WITH_ACK

#define GAMA_SENS_IT_MESSAGE_HEADER "GamaSenseIT_"
#define GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND "UPDATE_DATE_"
#define GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND "REGISTER_"
#define GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND "CAPTURE"
#define GAMA_SENS_IT_SENDER_NAME "_SENDER_"
#define GAMA_SENS_IT_MESSAGE_ID "_MID_"
#define GAMA_SENS_IT_MESSAGE_VALUE "_VALUES_"
#define CAPTURE_COMMAND 1
#define DATE_UPDATE_COMMAND 2
#define REGISTER_COMMAND 3

#define ADDRESS     "vmpams.ird.fr:1935/gamaSenseIt/"
#define ADDRESS_PROTOCOL     "tcp://"
#define MQTT_USER_NAME "gamasenseit"
#define MQTT_PASSWORD "gamasenseit"
#define MQTT_TOPIC "gamasenseit"

#define DEFAULT_FILE_NAME "sensorLogFile.csv"
#define QOS         1
#define TIMEOUT     10000L


//#define PABOOST
#define LORAMODE  1

#define MAX_DBM 14


using namespace std;



class GamaSenseIT
{
private:

	SX1272 loraConnector;
	map<int,string>* sensorName;
	MQTTClient client;
	MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
	MQTTClient_message pubmsg = MQTTClient_message_initializer;
	MQTTClient_deliveryToken token;

	ofstream * outFile;
	char* gatewayName ;
	string brokerAddress;
	bool useBroker;
	bool saveInFile;
	string fileName;
	string username;
	string password;

    
    void setupLora();
    bool containPrefix(string s, string  prefix);
    string extractData(string message);
    int messageCommand(string message);
    int offsetMessageContent(string message);
    string messageContents(string message);
    int sendToBrocker(string message, string sender, string mid, unsigned long sensorDate);
    int computeCaptureCommand(string message, int senderAddress);
    void computeRegisterCommand(string message, int senderAddress);
    void computeMessage(string message, int senderAddress);
    void setupMQTT();
	void closeMQTT();
    void setupOutFile(string outf);



public:
	int loraMode;
	uint32_t DEFAULT_CHANNEL;
    GamaSenseIT(SX1272 &loraConnection);
    
    void sendToSensor(string data,int receiverAddress);
    void analyseParameter(string cmd, string value);
    void waitAndReceiveMessage(string& message, int& source);
    void sendDate(int receiverAddress);
    unsigned long getdate();
    void setup();
    void loop();
};



#endif
