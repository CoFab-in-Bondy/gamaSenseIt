#include "GamaSenseIt.h"

GamaSenseIt::GamaSenseIt()
{
	loraMode=LORAMODE;
  DEFAULT_CHANNEL = CH_10_868;
}

int GamaSenseIt::configure(String nm, int addr)
{
  this->myName = nm;
  this->address = addr;
  this-> messageID = 0;
  return this->setupLora();
 }


int GamaSenseIt::setupLora()
{
  // Open serial communications and wait for port to open:
 #if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  Serial.begin(9600);
  // Print a start message
  Serial.println(F("SX1272 module and Arduino: send packets without ACK"));
 #endif 


 // Print a start message
  PRINT_CSTSTR("%s","Simple LoRa ping-pong with the gateway\n");  
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
#ifdef ARDUINO_AVR_PRO
  PRINT_CSTSTR("%s","Arduino Pro Mini detected\n");  
#endif
#ifdef ARDUINO_AVR_NANO
  PRINT_CSTSTR("%s","Arduino Nano detected\n");   
#endif
#ifdef ARDUINO_AVR_MINI
  PRINT_CSTSTR("%s","Arduino MINI/Nexus detected\n");  
#endif
#ifdef ARDUINO_AVR_MEGA2560
  PRINT_CSTSTR("%s","Arduino Mega2560 detected\n");  
#endif
#ifdef ARDUINO_SAM_DUE
  PRINT_CSTSTR("%s","Arduino Due detected\n");  
#endif
#ifdef __MK66FX1M0__
  PRINT_CSTSTR("%s","Teensy36 MK66FX1M0 detected\n");
#endif
#ifdef __MK64FX512__
  PRINT_CSTSTR("%s","Teensy35 MK64FX512 detected\n");
#endif
#ifdef __MK20DX256__
  PRINT_CSTSTR("%s","Teensy31/32 MK20DX256 detected\n");
#endif
#ifdef __MKL26Z64__
  PRINT_CSTSTR("%s","TeensyLC MKL26Z64 detected\n");
#endif
#if defined ARDUINO_SAMD_ZERO && not defined ARDUINO_SAMD_FEATHER_M0
  PRINT_CSTSTR("%s","Arduino M0/Zero detected\n");
#endif
#ifdef ARDUINO_AVR_FEATHER32U4 
  PRINT_CSTSTR("%s","Adafruit Feather32U4 detected\n"); 
#endif
#ifdef  ARDUINO_SAMD_FEATHER_M0
  PRINT_CSTSTR("%s","Adafruit FeatherM0 detected\n");
#endif

// See http://www.nongnu.org/avr-libc/user-manual/using_tools.html
// for the list of define from the AVR compiler

#ifdef __AVR_ATmega328P__
  PRINT_CSTSTR("%s","ATmega328P detected\n");
#endif 
#ifdef __AVR_ATmega32U4__
  PRINT_CSTSTR("%s","ATmega32U4 detected\n");
#endif 
#ifdef __AVR_ATmega2560__
  PRINT_CSTSTR("%s","ATmega2560 detected\n");
#endif 
#ifdef __SAMD21G18A__ 
  PRINT_CSTSTR("%s","SAMD21G18A ARM Cortex-M0+ detected\n");
#endif
#ifdef __SAM3X8E__ 
  PRINT_CSTSTR("%s","SAM3X8E ARM Cortex-M3 detected\n");
#endif
 #endif 

  
  // Power ON the module
  e = sx1272.ON();
 #if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  Serial.print(F("Setting power ON: state "));
  Serial.println(e, DEC);
 #endif 
  // Set transmission mode and print the result
  e = sx1272.setMode(loraMode);
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
   Serial.print(F("Setting Mode: state "));
  Serial.println(e, DEC);
#endif 
      // Set header
  //  e = sx1272.setHeaderON();
  //  printf("Setting Header ON: state %d\n", e);
    
  sx1272._enableCarrierSense=true;
   
  // Set header
/*  e |= sx1272.setHeaderON();
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  Serial.print(F("Setting Header ON: state "));
  Serial.println(e, DEC);
#endif  
  // Select frequency channel
  e |= sx1272.setChannel(CH_11_868);
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  Serial.print(F("Setting Channel: state "));
  Serial.println(e, DEC);
#endif  */
  // Set CRC
/*  e |= sx1272.setCRC_ON();

#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  Serial.print(F("Setting CRC ON: state "));
  Serial.println(e, DEC);
#endif  */
  
    e = sx1272.setChannel(DEFAULT_CHANNEL);
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
    PRINT_CSTSTR("%s","Setting Channel: state ");
    PRINT_VALUE("%d", e);
    PRINTLN;
#endif
    // Set CRC
 //   e = sx1272.setCRC_ON();
 //   printf("Setting CRC ON: state %d\n", e);
  // Select amplifier line; PABOOST or RFO
#ifdef PABOOST
  sx1272._needPABOOST=true;
  // previous way for setting output power
  // powerLevel='x';
#else
  // previous way for setting output power
  // powerLevel='M';  
#endif
  
  
  e = sx1272.setPowerDBM((uint8_t)MAX_DBM); 
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  PRINT_CSTSTR("%s","Setting Power: state ");
  PRINT_VALUE("%d", e);
  PRINTLN;
#endif 
  
  // Set the node address and print the result
  e = sx1272.setNodeAddress(this->address);
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
  PRINT_CSTSTR("%s","Setting node addr: state ");
  PRINT_VALUE("%d", e);
  PRINTLN;
  
  PRINT_CSTSTR("%s","Lora network adress is : ");
  PRINT_VALUE("%d", this->address);
  PRINTLN;
  

  // Print a success message
  if (e == 0)
    Serial.println(F("SX1272 successfully configured"));
  else
    Serial.println(F("SX1272 initialization failed"));
#endif
  return e;
}


bool GamaSenseIt::containPrefix(String s, String  prefix)
    {
        for(int i=0; i<prefix.length() && i < s.length(); i++)
        {
            if(s[i] != prefix[i])
                return false;
        }
        
        return true;
    }
String GamaSenseIt::extractData(String message)
    {
        String prefix = GAMA_SENS_IT_MESSAGE_HEADER;
        String result=message.substring(prefix.length());
        return result;
    }
    
int GamaSenseIt::messageCommand(String message)
    {
        int found = message.indexOf(GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND);
        if(found>=0)
            return CAPTURE_COMMAND;
        found = message.indexOf(GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND);
        if(found>=0)
            return DATE_UPDATE_COMMAND;
        found = message.indexOf(GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND);
        if(found>=0)
            return REGISTER_COMMAND;
        return -1;
    }
    
int GamaSenseIt::offsetMessageContent(String message)
    {
        switch(messageCommand(message))
        {
            case CAPTURE_COMMAND : { return GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND.length();}
            case DATE_UPDATE_COMMAND : { return GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND.length();}
            case REGISTER_COMMAND : { return GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND.length();}
        }
        return -1;
    }
    
String GamaSenseIt::messageContents(String message)
{
    String tailString = message.substring(offsetMessageContent(message));
    return tailString;
}

String GamaSenseIt::buildRegisterMessage()
{
  String msg =  GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND+this->myName;
  return msg; 
}

String GamaSenseIt::buildCaptureMessage(String data)
{
    messageID = messageID + 1;
    String sid = String(messageID); 

    String message = "" ;
    message+=GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND;
    message+=GAMA_SENS_IT_SENDER_NAME;
    message+=myName;
    message+=GAMA_SENS_IT_MESSAGE_ID;
    message+=sid;
    message+=GAMA_SENS_IT_MESSAGE_VALUE;
    message+=data;
    return message;
}

String GamaSenseIt::waitAndReceiveMessage()
{
  boolean cc = true;
  String receivedMessage = "";
  do
  {
    String tmpReceivedMessage = "";
    e = sx1272.receivePacketTimeout(10000);
    if ( e == 0 )
    {
      for (unsigned int i = 0; i < sx1272.packet_received.length; i++)
      {
        tmpReceivedMessage += (char)sx1272.packet_received.data[i];
      }
      if(containPrefix(tmpReceivedMessage,GAMA_SENS_IT_MESSAGE_HEADER))
      {
        receivedMessage = extractData(tmpReceivedMessage);
        cc = false;
      }
    }
    else {
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
       Serial.print(F(".."));
#endif
    }
  }while(cc);
  return receivedMessage;
}


void GamaSenseIt::sendDataToGateway(String message)
{
  sendToGateway(buildCaptureMessage(message));
}


void GamaSenseIt::sendToGateway(String message)
{
 // message = buildCaptureMessage(message);
  String prefix = GAMA_SENS_IT_MESSAGE_HEADER ;     
  char data [message.length()+prefix.length()+1 ];
  //xmm.toCharArray(data, xmm.length());
  int i = 0;
  for(; i<prefix.length();i++)
  {
    data[i] = prefix[i];
  }
  
   for(; i<sizeof(data);i++)
  {
    data[i] = message[i - prefix.length()];
  }
  
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
Serial.println("************************");
Serial.print("sent data : ");
Serial.println(data);
Serial.println("************************");
#endif

#ifdef WITH_ACK
      int n_retry=NB_RETRIES;
      
      do {
        e = sx1272.sendPacketTimeoutACKRetries(GATEWAY_ADDRESS, data);
        n_retry--;
      
#if(GAMA_SENSE_IT_DEBUG_MODE > 0)
        if (e==3)
          Serial.print("No ACK");
        if (n_retry)
          Serial.print("Retry");
        else
          Serial.print("Abort"); 
#endif          
      } while (e && n_retry);          
#else
      e = sx1272.sendPacketTimeout(GATEWAY_ADDRESS, data);
#endif
}



GamaSenseIt gamaSenseIt = GamaSenseIt();

