#include "Message.h"



Message::Message(int cmd,int src,int dest, string msg, int dte)
{
    this->command = cmd;
    this->sender = src;
    this->receiver = dest;
    this->contents = msg;
    this->date = dte;
}

int Message::getCommand()
{
    return this->command;
}

int Message::getSender()
{
    return this->sender;
}

int Message::getReceiver()
{
    return this->receiver;
}

string Message::getContents()
{
    return this->contents;
}
