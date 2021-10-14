
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
#define WITH_ACK

#define GAMA_SENS_IT_MESSAGE_HEADER "GamaSenseIT_"
#define GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND "UPDATE_DATE_"
#define GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND "REGISTER_"
#define GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND "CAPTURE"
#define GAMA_SENS_IT_MESSAGE_DATE "_DATE_"
#define GAMA_SENS_IT_MESSAGE_VALUE "_VALUES_"
#define CAPTURE_COMMAND 1
#define DATE_UPDATE_COMMAND 2
#define REGISTER_COMMAND 3

#define ADDRESS     "192.168.1.71"
#define ADDRESS_PROTOCOL     "tcp://"
#define ADDRESS_PORT     ":1883"
#define CLIENTID    "GATEWAY"
#define DEFAULT_FILE_NAME "sensorLogFile.csv"
#define QOS         1
#define TIMEOUT     10000L

using namespace std;


map<int,string>* sensorName;
int e;

MQTTClient client;
MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
MQTTClient_message pubmsg = MQTTClient_message_initializer;
MQTTClient_deliveryToken token;

ofstream * outFile;
char* gatewayName ;
string brokerAddress = ADDRESS;
bool useBroker = false;
bool saveInFile = false;
string fileName = DEFAULT_FILE_NAME;
void setupLora()
{
    // Print a start message
    printf("SX1272 module and Raspberry Pi: receive packets with ACK\n");
    
    // Power ON the module
    e = sx1272.ON();
    
    delay(1000);
    printf("Setting power ON: state %d\n", e);
    
    // Set transmission mode
    e = sx1272.setMode(4);
    printf("Setting Mode: state %d\n", e);
    // Set header
    e = sx1272.setHeaderON();
    printf("Setting Header ON: state %d\n", e);
    
    // Select frequency channel
    e = sx1272.setChannel(CH_09_868);
    printf("Setting Channel: state %d\n", e);
    // Set CRC
    e = sx1272.setCRC_ON();
    printf("Setting CRC ON: state %d\n", e);
    
    // Select output power (Max, High or Low)
    e = sx1272.setPower('H');
    printf("Setting Power: state %d\n", e);
    
    // Set the node address
    e = sx1272.setNodeAddress(1);
    printf("Setting Node address: state %d\n", e);
    
    // Print a success message
    printf("SX1272 successfully configured\n\n");
}



    bool containPrefix(string s, string  prefix)
    {
        for(int i=0; i<prefix.size() && i < s.size(); i++)
        {
            if(s[i] != prefix[i])
            {
                return false;
            }
        }
        return true;
    }
    
    void sendToSensor(string data,int receiverAddress)
    {
        string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
        
        string toSend = prefix + data;
        char dtToSend[toSend.size()+1];//as 1 char space for null is also required
        strcpy(dtToSend, toSend.c_str());


#ifdef WITH_ACK
      int n_retry=NB_RETRIES;

      do {
        e = sx1272.sendPacketTimeoutACKRetries(receiverAddress, dtToSend);

        n_retry--;

        if (n_retry == 0)
            cout<<"Abort message to "<<receiverAddress<<endl;
        	cout<<"contents "<<dtToSend<<endl;
      } while (e && n_retry);
#else
      e = sx1272.sendPacketTimeout(receiverAddress, dtToSend);
#endif
 }
	string extractData(string message)
	{
		string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
		string result=message.substr(prefix.size());
		return result;
	}

void waitAndReceiveMessage(string& message, int& source)
{
    boolean cc = true;
    int sender = 0;
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
    string receivedMessage = "";
    cout<<"waiting message."<<endl;
    do
    {
    	e = 0;
        string tmpReceivedMessage = "";
        e = sx1272.receivePacketTimeout(10000);
        if ( e == 0 )
        {
            sender =sx1272.packet_received.src;
            for (unsigned int i = 0; i < sx1272.packet_received.length; i++)
            {
                tmpReceivedMessage += (char)sx1272.packet_received.data[i];
            }
            if(containPrefix(tmpReceivedMessage,prefix))
            {
                receivedMessage = extractData(tmpReceivedMessage);
                cc = false;
             }
        }
        else {
            cout<<".";
            // Serial.println(e, DEC);
        }
    }while(cc);
    message  =receivedMessage;
    source = sender;
    cout<<"receiving message from : "<<source<<endl;
    cout<<"contents : "<<message<<endl;
    //return 0;
}

    unsigned long getdate()
    {
        time_t timer;
        time(&timer);
        unsigned long tt = timer;
        return tt;
    }
    
    void sendDate(int receiverAddress)
    {
        unsigned long mdate =getdate();
        stringstream ss;
        ss << mdate;
        string sdate = ss.str();
        string data = GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND;
        data = data + sdate;
        sendToSensor(data,receiverAddress);
    }
    
    

    int messageCommand(string message)
    {
        int found = message.find(GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND);
        if(found!=std::string::npos)
            return CAPTURE_COMMAND;
        found = message.find(GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND);
        if(found!=std::string::npos)
            return DATE_UPDATE_COMMAND;
        found = message.find(GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND);
        if(found!=std::string::npos)
            return REGISTER_COMMAND;
        return -1;
    }
    
    int offsetMessageContent(string message)
    {
        switch(messageCommand(message))
        {
            case CAPTURE_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND);}
            case DATE_UPDATE_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND);}
            case REGISTER_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND);}
        }
        return -1;
    }
    
    string messageContents(string message)
    {
        string tailString = message.substr(offsetMessageContent(message));
        return tailString;
    }

    int sendToBrocker(string message, string sender, int sensorDate)
    {
    	string dte = std::to_string(sensorDate);
    	string data =""+ dte+";"+sender+";"+message;
    	char  msg[data.length() + 1];
    	strcpy(msg, data.c_str());
    	int rc;
    	pubmsg.payload = msg;
        pubmsg.payloadlen = message.length();
        pubmsg.qos = QOS;
        pubmsg.retained = 0;
        MQTTClient_publishMessage(client, gatewayName, &pubmsg, &token);
        printf("Waiting for up to %d seconds for publication of %s\n"
                "on topic %s for client with ClientID: %s\n",
                (int)(TIMEOUT/1000), msg, gatewayName, CLIENTID);
        rc = MQTTClient_waitForCompletion(client, token, TIMEOUT);
        printf("Message with delivery token %d delivered\n", token);
        return rc;
    }

    int computeCaptureCommand(string message, int senderAddress)
    {
        string datePrefix = GAMA_SENS_IT_MESSAGE_DATE;
        string valuePrefix = GAMA_SENS_IT_MESSAGE_VALUE;
        
        int dateFound = message.find(GAMA_SENS_IT_MESSAGE_DATE);
        if(dateFound==std::string::npos)
            return -1;
        int dataFound = message.find(GAMA_SENS_IT_MESSAGE_VALUE);
        if(dataFound==std::string::npos)
            return -1;
        int sensorDate;
        
        
        int dateIndex =dateFound + datePrefix.size();
        int dateSize =dataFound - dateIndex ;
        istringstream( message.substr(dateIndex,dateSize)) >> sensorDate;
        int dataIndex =dataFound + valuePrefix.size();
        string data = message.substr(dataIndex);
        string ssender = sensorName->find(senderAddress)->second;
        if(saveInFile == true)
        {
        	(*outFile) <<  sensorDate << ";"<<ssender<<";"<<data<<"\r\n";
        	outFile->flush();
        }
        int sending = useBroker==false?0:sendToBrocker(data,  ssender,  sensorDate);
        return sending;
    }

    void computeRegisterCommand(string message, int senderAddress)
    {
        string senderName = messageContents(message);
        cout <<"sender name" <<senderName <<endl;
        sensorName->insert(make_pair(senderAddress,senderName));
        sendDate(senderAddress);
    }

    void computeMessage(string message, int senderAddress)
    {
        int command = messageCommand(message);
        switch(command)
        {
            case CAPTURE_COMMAND : {
                computeCaptureCommand(message,senderAddress);
                break;
            }
            case REGISTER_COMMAND : {
                computeRegisterCommand(message,senderAddress);
                break;
            }
        }
    }

    void setupMQTT(string address, string clientID)
    {
    	string pro = ADDRESS_PROTOCOL;
    	string port = ADDRESS_PORT;

    	char  tcpAddress[pro.length()+port.length()+1+address.length()];
    	strcpy(tcpAddress,pro.c_str());
    	strcat(tcpAddress,address.c_str());
    	strcat(tcpAddress,port.c_str());
    	int rc;

    	char id_c[clientID.length()+1];
    	strcpy(id_c,clientID.c_str());

    	 MQTTClient_create(&client, tcpAddress, id_c,
            MQTTCLIENT_PERSISTENCE_NONE, NULL);
        conn_opts.keepAliveInterval = 20;
        conn_opts.cleansession = 1;

        if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS)
        {
            printf("Failed to connect, return code %d\n", rc);
            exit(-1);
        }
    }
void setupOutFile(string outf)
{
    outFile = new ofstream();
    outFile->open(outf.c_str());
}
void setup()
{
    sensorName = new map<int,string>();
    setupLora();
    if(useBroker == true)
    {
        setupMQTT(brokerAddress,gatewayName);
    }
    if(saveInFile == true)
    {
    	setupOutFile(fileName);
    }


}

void loop()
{
   while(true)
   {
       string msg = "";
       int source = -1;
       waitAndReceiveMessage(msg,source);
       computeMessage(msg,source);
    }
}
void analyseParameter(string cmd, string value)
{
	if (cmd.compare("-broker") == 0)
	{
		useBroker = true;
		brokerAddress = value;
	}
	if (cmd.compare("-name") == 0)
	{
		gatewayName = new char[value.length()+1];
		strcpy(gatewayName, value.c_str());
	}
	if (cmd.compare("-file") == 0)
	{
		saveInFile = true;
		fileName = value;
	}
}
int main(int argc, char *argv[])
{
	string id = CLIENTID;
	gatewayName = new char[id.length()+1];
	strcpy(gatewayName,id.c_str());
	for(int i = 1; i+1<argc; i=i+2 )
	{
		string cmd(argv[i]);
		string val(argv[i+1]);
		cout <<"command: "<<cmd<<" value: "<<val<<endl;
		analyseParameter(cmd,val);
	}

    setup();
    loop();
    return 0;
}
