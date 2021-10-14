
#include "GamaSenseIt.h"





using namespace std;

GamaSenseIT::GamaSenseIT(SX1272 &loraConnection)
{
	string id = MQTT_TOPIC;
	gatewayName = new char[id.length()+1];
	strcpy(gatewayName,id.c_str());
	this->loraConnector = loraConnection;
	this->username= MQTT_USER_NAME;
	this->password= MQTT_PASSWORD;
	brokerAddress = ADDRESS;
	useBroker = false;
	saveInFile = false;
	fileName = DEFAULT_FILE_NAME;
	loraMode=LORAMODE;
	DEFAULT_CHANNEL = CH_10_868;
}

void GamaSenseIT::setupLora()
{
	int e;
    // Print a start message
    printf("loraConnector module and Raspberry Pi: receive packets with ACK\n");
    
    // Power ON the module
    e = loraConnector.ON();
    
    delay(1000);
    printf("Setting power ON: state %d\n", e);
    
    // Set transmission mode
    e = loraConnector.setMode(loraMode);
    printf("Setting Mode: state %d\n", e);

    // Set header
  //  e = loraConnector.setHeaderON();
  //  printf("Setting Header ON: state %d\n", e);
	
	loraConnector._enableCarrierSense=true;
    printf("Setting Header ON: state %d\n", e);
    
    // Select frequency channel
	
	e = loraConnector.setChannel(DEFAULT_CHANNEL);
    printf("Setting Channel: state %d\n", e);
    // Set CRC
    //e = loraConnector.setCRC_ON();
    //printf("Setting CRC ON: state %d\n", e);
	
#ifdef PABOOST
  	loraConnector._needPABOOST=true;
  // previous way for setting output power
  // powerLevel='x';
printf("with PA BOOST");
#else
 printf("no PA BOOST");
 // previous way for setting output power
  // powerLevel='M';  
#endif
  
	
    e = loraConnector.setPowerDBM((uint8_t)MAX_DBM); 
    printf("Setting Power: state %d\n", e);
    printf("select power in DBM : %d\n", MAX_DBM);
    
    // Set the node address
    e = loraConnector.setNodeAddress(1);
    printf("Setting Node address: state %d\n", e);
    
    // Print a success message
    printf("loraConnector successfully configured\n\n");
}

bool GamaSenseIT::containPrefix(string s, string  prefix)
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

void GamaSenseIT::sendToSensor(string data,int receiverAddress)
{
	int e;
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;

    string toSend = prefix + data;
    char dtToSend[toSend.size()+1];//as 1 char space for null is also required
    strcpy(dtToSend, toSend.c_str());


#ifdef WITH_ACK
  int n_retry=NB_RETRIES;
  do {
	  e = loraConnector.sendPacketTimeoutACKRetries(receiverAddress, dtToSend);

    n_retry--;

    if (n_retry == 0)
    {
    	cout<<"Abort message to "<<receiverAddress<<endl;
    	cout<<"contents "<<dtToSend<<endl;
    }
  } while (e && n_retry);
#else
  e = loraConnector.sendPacketTimeout(receiverAddress, dtToSend);
#endif
}

string GamaSenseIT::extractData(string message)
{
	string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
	string result=message.substr(prefix.size());
	return result;
}

void GamaSenseIT::waitAndReceiveMessage(string& message, int& source)
{
	int e;
    boolean cc = true;
    int sender = 0;
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
    string receivedMessage = "";
    cout<<"waiting message."<<endl;
    do
    {
    	e = 0;
        string tmpReceivedMessage = "";

#ifdef WITH_ACK
        e = loraConnector.receivePacketTimeoutACK(10000);
        
#else
        e = loraConnector.receivePacketTimeout(10000);
#endif
        
        if ( e == 0 )
        {
            sender =loraConnector.packet_received.src;
            for (unsigned int i = 0; i < loraConnector.packet_received.length; i++)
            {
                tmpReceivedMessage += (char)loraConnector.packet_received.data[i];
            }
			cout<<"pre contents : "<<tmpReceivedMessage<<endl;
            if(containPrefix(tmpReceivedMessage,prefix))
            {
                receivedMessage = extractData(tmpReceivedMessage);
                cc = false;
             }
        }
        else {
         //   cout<<".";
         delay(100);
        }
    }while(cc);
    message  =receivedMessage;
    source = sender;
    cout<<"receiving message from : "<<source<<endl;
    cout<<"contents : "<<message<<endl;
    //return 0;
}


unsigned long GamaSenseIT::getdate()
{
    time_t timer;
    time(&timer);
    unsigned long tt = timer;
    return tt;
}


void GamaSenseIT::sendDate(int receiverAddress)
{
    unsigned long mdate =getdate();
    stringstream ss;
    ss << mdate;
    string sdate = ss.str();
    string data = GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND;
    data = data + sdate;
    sendToSensor(data,receiverAddress);
}

int GamaSenseIT::messageCommand(string message)
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

int GamaSenseIT::offsetMessageContent(string message)
{
    switch(messageCommand(message))
    {
        case CAPTURE_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND);}
        case DATE_UPDATE_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND);}
        case REGISTER_COMMAND : { return strlen(GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND);}
    }
    return -1;
}

string GamaSenseIT::messageContents(string message)
{
    string tailString = message.substr(offsetMessageContent(message));
    return tailString;
}

int GamaSenseIT::sendToBrocker(string message, string sender, string mid, unsigned long sensorDate)
 {
	string dte = to_string(sensorDate);
 	string data ="";
		data.append(dte);
	data.append(";");
	data.append(sender);
	data.append(";");
	data.append(mid);
	data.append(";");
	data.append(message);
 	char  msg[data.size() ];
 	strcpy(msg, data.c_str());
 	int rc;
 	pubmsg.payload = msg;
     pubmsg.payloadlen = strlen(msg); 
     pubmsg.qos = QOS;
     pubmsg.retained = 0;
     MQTTClient_publishMessage(client, gatewayName, &pubmsg, &token);
     printf("NOT Waiting for up to %d seconds for publication of %s\n"
             "on topic %s for client with ClientID: %s\n",
             (int)(TIMEOUT/1000), msg, gatewayName, gatewayName);
     //rc = MQTTClient_waitForCompletion(client, token, TIMEOUT);
     printf("Message with delivery token %d delivered\n", token);
     return rc;
 }


int GamaSenseIT::computeCaptureCommand(string message, int senderAddress)
{
    string datePrefix = GAMA_SENS_IT_SENDER_NAME;
    string MIDPrefix = GAMA_SENS_IT_MESSAGE_ID;
    string valuePrefix = GAMA_SENS_IT_MESSAGE_VALUE;

    int dateFound = message.find(GAMA_SENS_IT_SENDER_NAME);
    if(dateFound==std::string::npos)
        return -1;
	
	cout <<" pos name "<<dateFound<<endl;
    
    int midFound = message.find(GAMA_SENS_IT_MESSAGE_ID);
    if(midFound==std::string::npos)
        return -1;
	
	
	int dataFound = message.find(GAMA_SENS_IT_MESSAGE_VALUE);
    if(dataFound==std::string::npos)
        return -1;

    int dateIndex =dateFound + datePrefix.size();
    int dateSize =midFound - dateIndex ;

	string sensorName = message.substr(dateIndex,dateSize);
    int midIndex =midFound + MIDPrefix.size();
    int midSize =dataFound - midIndex ;

	string mid = message.substr(midIndex,midSize);
    int dataIndex =dataFound + valuePrefix.size();
    string data = message.substr(dataIndex);
	unsigned long sensorDate  = time(NULL);
   if(saveInFile == true)
    {
    	(*outFile) <<  sensorDate << ";"<<sensorName<<";"<<mid<<";"<<data<<endl;
    	outFile->flush();
    }

    int sending = 0;
	if(useBroker)
		{
			setupMQTT();
			sendToBrocker(data,  sensorName, mid,  sensorDate);
			closeMQTT();
		} 
    return 1;
}


void GamaSenseIT::computeMessage(string message, int senderAddress)
 {
     int command = messageCommand(message);
     switch(command)
     {
         case CAPTURE_COMMAND : {
             computeCaptureCommand(message,senderAddress);
             break;
         }
     }
 }

void GamaSenseIT::setupMQTT()
{
	
	/*char  tcpAddress[pro.length()+port.length()+1+address.length()];
	strcpy(tcpAddress,pro.c_str());
	strcat(tcpAddress,address.c_str());
	strcat(tcpAddress,port.c_str());
	*/
	string address = this->brokerAddress;
	string clientID = this->username;
	char  tcpAddress[1+address.length()];
	strcpy(tcpAddress,address.c_str());
	//strcat(tcpAddress,address.c_str());
	//strcat(tcpAddress,port.c_str());
	int rc;

	char id_c[clientID.length()+1];
	strcpy(id_c,clientID.c_str());

	 MQTTClient_create(&client, tcpAddress, id_c,
        MQTTCLIENT_PERSISTENCE_NONE, NULL);
    conn_opts.keepAliveInterval = 4000;
    conn_opts.cleansession = 1;
	conn_opts.username =(this->username).c_str();
	conn_opts.password =(this->password).c_str();
	cout<<"username "<<(this->username).c_str()<<endl;

    if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS)
    {
        printf("Failed to connect, return code %d\n", rc);
        exit(-1);
    }
}


void GamaSenseIT::closeMQTT()
{
    MQTTClient_disconnect(client, 10000);
    MQTTClient_destroy(&client);
}

void GamaSenseIT::setupOutFile(string outf)
{
    outFile = new ofstream();
    outFile->open(outf.c_str());
}

void  GamaSenseIT::analyseParameter(string cmd, string value)
{
	if (cmd.compare("-broker") == 0)
	{
		useBroker = true;
		if (value.compare("autoconf") == 0)
		{
			cout<<"****************************************************"<<endl;
			cout<<"*       using default broker configuration         *"<<endl;
			cout<<"*   broker:  vmpams.ird.fr:1935/gamaSenseIt        *"<<endl;
			cout<<"*    login:  gamasenseit                           *"<<endl;
			cout<<"* password:  gamasenseit                           *"<<endl;
			cout<<"*    topic:  gamasenseit                           *"<<endl;
			cout<<"****************************************************"<<endl;
		}
		else
		{
			brokerAddress = value;
		}
	
	}
	if (cmd.compare("-username") == 0)
	{
		this->username =value;
		cout<<"username :"<<this->username<<endl;
	}
	if (cmd.compare("-password") == 0)
	{
		this->password =value;
		cout<<"password :"<<this->password<<endl;
	}
	
	if (cmd.compare("-topicname") == 0)
	{
		gatewayName = new char[value.length()+1];
		strcpy(gatewayName, value.c_str());
		cout<<"topicname :"<<this->gatewayName<<endl;
		
	}
	if (cmd.compare("-file") == 0)
	{
		saveInFile = true;
		fileName = value;
	}
}


void GamaSenseIT::setup()
{
    setupLora();
    if(useBroker == true)
    {
        setupMQTT();
		closeMQTT();
    }
    if(saveInFile == true)
    {
    	setupOutFile(fileName);
    }


}

void GamaSenseIT::loop()
{
   while(true)
   {
       string msg = "";
       int source = -1;
       waitAndReceiveMessage(msg,source);
       computeMessage(msg,source);
    }
}


int main(int argc, char *argv[])
{
	 GamaSenseIT* gamaSenseIt;
	 gamaSenseIt = new GamaSenseIT(sx1272);

	for(int i = 1; i+1<argc; i=i+2 )
	{
		string cmd(argv[i]);
		string val(argv[i+1]);
		gamaSenseIt->analyseParameter(cmd,val);
	}

	gamaSenseIt->setup();
	gamaSenseIt->loop();
    return 0;
}

