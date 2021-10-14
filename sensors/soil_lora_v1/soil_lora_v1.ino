/*  
 *  LoRa 868 / 915MHz SX1272 LoRa module
 *  
 *  Version:           1
 *  Implementation :            Nicolas Marilleau, Hannane Aroui, Christian Hartmann et Norbert Silvera
 */

#include <Wire.h>
#include "SX1272.h"
#include <SPI.h>

/***
 * 
 * Sensor libraries
 * 
 * Begin
 */

#include <DHT.h>
#include <OneWire.h>
#include <DallasTemperature.h>

/***
 * 
 * Sensor libraries
 * 
 * End
 */

String  GAMA_SENS_IT_MESSAGE_HEADER = "GamaSenseIT_";
#define GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND String("UPDATE_DATE_")
#define GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND String("REGISTER_")
#define GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND String("CAPTURE")
#define GAMA_SENS_IT_MESSAGE_DATE "_DATE_"
#define GAMA_SENS_IT_MESSAGE_VALUE "_VALUES_"
#define CAPTURE_COMMAND 1
#define  DATE_UPDATE_COMMAND 2
#define  REGISTER_COMMAND 3

#define GATEWAY_ADDRESS  1

#define SENSOR_POWER_SLOT 7

#define SENSOR_NAME  String("sensor_1") 
int SENSOR_ADDRESS = 2;

//char messageBuffer[150];
//char messageSize = 0;

int e;

unsigned long int startDate;
unsigned long int timeStamp;

int i = 0;



/********
 * 
 * Sensor variable and methods
 * 
 * BEGIN
 */


#define TIME_MSG_LEN  11   // time sync to PC is HEADER followed by unix time_t as ten ascii digits
#define TIME_HEADER  255   // Header tag for serial time sync message

// Data wire is plugged into port 2 on the Arduino
#define ONE_WIRE_BUS 3

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

//capteur de temperature sur A0
#define DHTPIN A0
#define DHTTYPE DHT11

// Test code for Grove - Moisture Sensor 
#define conductivite1Pin  A1 // select the input pin for the potentiometer
#define conductivite2Pin  A2 // select the input pin for the potentiometer
#define conductivite3Pin  A3 // select the input pin for the potentiometer

unsigned long time;
DHT dht(DHTPIN, DHTTYPE);
 
struct MyData {
  int m_date;
  float air_temperature;
  int air_humidite;
  float soil_temperature;
  int soil_connectivite1;
  int soil_connectivite2;
  int soil_connectivite3;
};

 MyData global_data;



void setup_sensor()
{
// initialize digital pin LED_BUILTIN as an output to power the relay for moisture sensors
 pinMode(SENSOR_POWER_SLOT, OUTPUT);
 setup_humidity();
 setup_soil_temp();
 
}

void setup_humidity() 
{
    dht.begin();
}


void setup_soil_temp()
{
    sensors.begin();
}


String loop_sensor()
{
  time = millis();
  int dte = time /1000/60;
  global_data.m_date = dte;
  
  loop_soil_temp();
  loop_moisture();
  loop_temperature();

  
  
 
  return "T1:"+String(global_data.air_temperature)+";T2:"+String(global_data.soil_temperature)+";H1:"+String(global_data.air_humidite)+";C1:"+String(global_data.soil_connectivite1)+";C2:"+String(global_data.soil_connectivite2)+";C3:"+String(global_data.soil_connectivite3);
}


void loop_soil_temp()
{ 
  // call sensors.requestTemperatures() to issue a global temperature 
  // request to all devices on the bus
   sensors.requestTemperatures(); // Send the command to get temperatures
   // After we got the temperatures, we can print them here.
  // We use the function ByIndex, and as an example get the temperature from the first sensor only.
  float tmp = sensors.getTempCByIndex(0);
  global_data.soil_temperature = tmp;
}

void loop_moisture() {
    global_data.soil_connectivite1 =  analogRead(conductivite1Pin);// Read soil moisture
    global_data.soil_connectivite2 =  analogRead(conductivite2Pin);// Read soil moisture
    global_data.soil_connectivite3 =  analogRead(conductivite3Pin);// Read soil moisture

}

void loop_moisture1() {
    // read the value from the sensor:
    global_data.soil_connectivite1 =  analogRead(conductivite1Pin);
  
}
void loop_moisture2() {
    // read the value from the sensor:
    global_data.soil_connectivite2 =  analogRead(conductivite2Pin);
 }

void loop_moisture3() {
    // read the value from the sensor:
    global_data.soil_connectivite3 =  analogRead(conductivite3Pin);
 }



void loop_temperature() 
{
    // Reading temperature or humidity takes about 250 milliseconds!
    // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
    float h = dht.readHumidity();
    float t = dht.readTemperature();
     // check if returns are valid, if they are NaN (not a number) then something went wrong!
    if (isnan(t) || isnan(h)) 
    {
    //    Serial.println("Failed to read from DHT");
    } 
    else 
    {
    global_data.air_humidite = h;
    global_data.air_temperature = t;
    }
}



/********
 * 
 * Sensor variable and methods
 * 
 * END
 */

void setupLora()
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  
  // Print a start message
  Serial.println(F("SX1272 module and Arduino: send packets without ACK"));
  
  // Power ON the module
  e = sx1272.ON();
  Serial.print(F("Setting power ON: state "));
  Serial.println(e, DEC);
  
  // Set transmission mode and print the result
  e |= sx1272.setMode(4);
  Serial.print(F("Setting Mode: state "));
  Serial.println(e, DEC);
  
  // Set header
  e |= sx1272.setHeaderON();
  Serial.print(F("Setting Header ON: state "));
  Serial.println(e, DEC);
  
  // Select frequency channel
  e |= sx1272.setChannel(CH_10_868);
  Serial.print(F("Setting Channel: state "));
  Serial.println(e, DEC);
  
  // Set CRC
  e |= sx1272.setCRC_ON();
  Serial.print(F("Setting CRC ON: state "));
  Serial.println(e, DEC);
  
  // Select output power (Max, High or Low)
  e |= sx1272.setPower('H');
  Serial.print(F("Setting Power: state "));
  Serial.println(e, DEC);
  
  // Set the node address and print the result
  e |= sx1272.setNodeAddress(SENSOR_ADDRESS);
  Serial.print(F("Setting node address: state "));
  Serial.println(e, DEC);
  
  // Print a success message
  if (e == 0)
    Serial.println(F("SX1272 successfully configured"));
  else
    Serial.println(F("SX1272 initialization failed"));

  
}
    bool containPrefix(String s, String  prefix)
    {
        for(int i=0; i<prefix.length() && i < s.length(); i++)
        {
            if(s[i] != prefix[i])
                return false;
        }
        
        return true;
    }
    String extractData(String message)
    {
        String prefix = GAMA_SENS_IT_MESSAGE_HEADER;
        String result=message.substring(prefix.length());
        return result;
    }
    
    int messageCommand(String message)
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
    
    int offsetMessageContent(String message)
    {
        switch(messageCommand(message))
        {
            case CAPTURE_COMMAND : { return GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND.length();}
            case DATE_UPDATE_COMMAND : { return GAMA_SENS_IT_MESSAGE_UPDATE_DATE_COMMAND.length();}
            case REGISTER_COMMAND : { return GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND.length();}
        }
        return -1;
    }
    
    String messageContents(String message)
    {
        String tailString = message.substring(offsetMessageContent(message));
        return tailString;
    }

String buildRegisterMessage()
{
  String msg =  GAMA_SENS_IT_MESSAGE_REGISTER_COMMAND+SENSOR_NAME;
  return msg; 
}

String buildCaptureMessage(String data)
{
    unsigned long mdate =getCurrentDate();
    String sdate = String(mdate);
    String message = "" ;
    message+=GAMA_SENS_IT_MESSAGE_CAPTURE_COMMAND;
    message+=GAMA_SENS_IT_MESSAGE_DATE;
    message+=sdate;
    message+=GAMA_SENS_IT_MESSAGE_VALUE;
    message+=data;
 return message;
}

String waitAndReceiveMessage()
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
      Serial.print(F(".."));
    }
  }while(cc);
  return receivedMessage;
}

void sendToGateway(String message)
{
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
  e = sx1272.sendPacketTimeout(GATEWAY_ADDRESS, data);
}


int registerToGateway()
{
 sendToGateway(buildRegisterMessage());
  String receivedMessage="";
  int command = -1;
  unsigned long startWaiting = millis()/1000;
  Serial.print("Registering");
  do
  {
    Serial.print("...");
    receivedMessage = waitAndReceiveMessage();
    command = messageCommand(receivedMessage);
     Serial.println("");
        Serial.print("command recu ");
        Serial.print(command);
       
    if(startWaiting + 120 < (millis()/1000))
      return -1;
  }while(DATE_UPDATE_COMMAND != command);
 
   Serial.println("XXXXXXXXXXXX");
 
  Serial.println(receivedMessage);
  startDate = millis() / 1000;
  timeStamp = (receivedMessage.substring(offsetMessageContent(receivedMessage))).toInt();
  Serial.print("date ");
  Serial.println(timeStamp);
  
  
  return 0;
}

unsigned long getCurrentDate()
{
  unsigned long delayFromStart = (millis()/1000) - startDate;
  return timeStamp + delayFromStart;
}


void setup()
{
  setup_sensor();
  setupLora();
  delay(2000);
  int res = registerToGateway();
  if(res == 0)
  {
    Serial.println("Sensor registering to Gateway OK");
    Serial.print("Current date is ");
    Serial.println(getCurrentDate(), DEC);
  }
  else
  {
     Serial.println("Sensor registering to Gateway ERROR");
     while(true);
  }
}
void sendSensorData(String data)
{







  
  sendToGateway(buildCaptureMessage(data));
}


void loop(void)
{
  digitalWrite(SENSOR_POWER_SLOT, HIGH);     // LED and Relay off
  delay(400);
  
sendSensorData(loop_sensor());
 digitalWrite(SENSOR_POWER_SLOT, LOW);     // LED and Relay off
 
  delay(4000);  
 }

