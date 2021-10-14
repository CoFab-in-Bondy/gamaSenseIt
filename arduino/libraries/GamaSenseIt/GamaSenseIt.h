#ifndef GAMASENSEIT_H
#define GAMASENSEIT_H

#include "SX1272.h"

#define GAMA_SENSE_IT_DEBUG_MODE 4

#define WITH_ACK 
#define NB_RETRIES 10

#define  GAMA_SENS_IT_MESSAGE_HEADER String("GamaSenseIT_")
#define GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND String("UPDATE_DATE_")
#define GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND String("REGISTER_")
#define GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND String("CAPTURE")
#define GAMA_SENS_IT_SENDER_NAME "_SENDER_"
#define GAMA_SENS_IT_MESSAGE_VALUE "_VALUES_"
#define  GAMA_SENS_IT_MESSAGE_ID "_MID_"
#define CAPTURE_COMMAND 1
#define  DATE_UPDATE_COMMAND 2
#define  REGISTER_COMMAND 3

#define GATEWAY_ADDRESS  1


#define PRINTLN                   Serial.println("")
#define PRINT_CSTSTR(fmt,param)   Serial.print(F(param))
#define PRINT_STR(fmt,param)      Serial.print(param)
#define PRINT_VALUE(fmt,param)    Serial.print(param)
#define FLUSHOUTPUT               Serial.flush();

//#define PABOOST
#define LORAMODE  1

#define MAX_DBM 14

using namespace std;

class GamaSenseIt
{
 public :
    GamaSenseIt();
    int configure(String nm, int addr);
     void sendDataToGateway(String message);
   
    String waitAndReceiveMessage();
    uint32_t DEFAULT_CHANNEL; //=CH_10_868
	  int loraMode;
    int messageID;

 private :
    String myName;
    int address;
    int e;
    unsigned long int timeStamp;
    void sendToGateway(String message);
    
    int setupLora();
    String buildCaptureMessage(String data);
    String buildRegisterMessage();
    String messageContents(String message);
    int offsetMessageContent(String message);
    int messageCommand(String message);
    String extractData(String message);
    bool containPrefix(String s, String  prefix);
    




     
};

extern GamaSenseIt gamaSenseIt;
#endif
