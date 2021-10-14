#ifndef MESSAGE_GAMA_SENSE_IT
#define MESSAGE_GAMA_SENSE_IT



#include<string>



#define CAPTURE_COMMAND 1
#define DATE_UPDATE_COMMAND 2
#define REGISTER_COMMAND 3


using namespace std;
class Message
{
private:
    int command;
    int sender;
    int receiver;
    int date;
    string contents;

public:
    Message(int cmd,int src,int dest, string msg, int dte);
    int getCommand();
    int getSender();
    int getReceiver();
    string getContents();
};

#endif
