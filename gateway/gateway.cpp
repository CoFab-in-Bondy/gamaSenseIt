
#include "GamaSenseIt.h"


#define GAMA_SENS_IT_MESSAGE_HEADER "GamaSenseIT_"
#define GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND "UPDATE_DATE_"
#define GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND "REGISTER_"
#define GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND "CAPTURE"
#define GAMA_SENS_IT_MESSAGE_DATE "_DATE_"
#define GAMA_SENS_IT_MESSAGE_VALUE "_VALUES_"
#define CAPTURE_COMMAND 1
#define DATE_UPDATE_COMMAND 2
#define REGISTER_COMMAND 3


using namespace std;


map<int,string>* sensorName;
int e;
char my_packet[200];




bool GamaSenseIT::containPrefix(string s, string  prefix)
{
    cout<< "taill"<<prefix.size()<<endl;
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
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
    
    string toSend = prefix + data;
    char dtToSend [toSend.size()];
    for(int j = 0; j < toSend.size(); j++)
    {
        dtToSend[j]=toSend[j];
    }
    e = sx1272.sendPacketTimeout(receiverAddress, dtToSend);
}
string GamaSenseIT::extractData(string message)
{
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
    string result=message.substr(prefix.size());
    return result;
}

void GamaSenseIT::waitAndReceiveMessage(string& message, int& source)
{
    boolean cc = true;
    int sender = 0;
    string prefix = GAMA_SENS_IT_MESSAGE_HEADER;
    string receivedMessage = "";
    do
    {
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
            cout<<".."<<endl;
            // Serial.println(e, DEC);
        }
    }while(cc);
    message  =receivedMessage;
    source = sender;
    
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
        case CAPTURE_COMMAND : { return sizeof(GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND);}
        case DATE_UPDATE_COMMAND : { return sizeof(GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND);}
        case REGISTER_COMMAND : { return sizeof(GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND);}
    }
    return -1;
}

string GamaSenseIT::messageContents(string message)
{
    string tailString = message.substr(offsetMessageContent(message));
    return tailString;
}

int GamaSenseIT::computeCaptureCommand(string message, int senderAddress)
{
    string datePrefix = GAMA_SENS_IT_MESSAGE_DATE;
    string valuePrefix = GAMA_SENS_IT_MESSAGE_VALUE;
    
    cout << "donnees arrivees"<<message<<endl;
    
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
    
    cout<<"date:"<< sensorDate << endl<<"  data :"<<data<<endl;
    return 0;
}

void GamaSenseIT::computeRegisterCommand(string message, int senderAddress)
{
    string senderName = messageContents(message);
    sensorName->insert(make_pair(senderAddress,senderName));
    sendDate(senderAddress);
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
        case REGISTER_COMMAND : {
            computeRegisterCommand(message,senderAddress);
            break;
        }
    }
}

void setup()
{
    sensorName = new map<int,string>();
    setupLora();
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


