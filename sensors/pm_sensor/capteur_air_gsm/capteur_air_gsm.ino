#include "PMS.h"
#include <SoftwareSerial.h>
#include <PubSubClient.h>


#define TINY_GSM_MODEM_A7
#define SerialMon Serial
#define TINY_GSM_DEBUG SerialMon

#include <TinyGsmClient.h>

// Set phone numbers, if you want to test SMS and Calls
//#define SMS_TARGET  "+33638231637"
//#define CALL_TARGET "+33638231637"

#define SMS_TARGET  "+33688334906"
#define CALL_TARGET "+33688334906"

#define SENSOR_NAME "SENSOR_1"
 float p1 = 0, p10 = 0,  p25 = 0;
//Dust sensor configuration
SoftwareSerial SerialPMS(12, 13);
PMS pms(SerialPMS);
PMS::DATA data;

//Gsm sending configuration
SoftwareSerial SerialAT(7, 8); 
// Your GPRS credentials
// Leave empty, if missing user or pass
const char apn[]  = "sl2sfr";//"mmsbouygtel.com";
const char user[] = "";
const char pass[] = "";

const char* broker = "vmpams.mpl.ird.fr";

const char* topicSensor = "SENS_AIR_1";
  TinyGsm modem(SerialAT);
  TinyGsmClient client(modem);
  PubSubClient mqtt(client);

void setup() {
  Serial.begin(115200);
  while(!Serial);
 Serial.println("start GSM");
  setupGSM();
  delay(1000);
  Serial.println("start dust Sensor");
  setupDustSensor();
  delay(1000);

    
    
    Serial.println("start measure");
    
}

void loop() {
  Serial.println("start sensor");
  collectData();
  delay(1000);
  publishData();
}

/**
 * Dust Sensor
 */
void collectData()
{
  //setupDustSensor();
  SerialPMS.listen();
  delay(100);
  getSensorData();
  //closeDustSensor();
}


void setupDustSensor()
{
   // configure dust sensor
  SerialPMS.begin(9600);
  pms.passiveMode(); 
  delay(10);
}
void getSensorData()
{

  Serial.println("wake up, wait 30 seconds for stable readings...");
  pms.wakeUp();
  delay(60000);

  Serial.println("Send request read...");
  pms.requestRead();

  Serial.println("Wait max. 10 seconds for read...");
  if (pms.read(data, 5000))
  {
    Serial.println("Data:");
    p1 = data.PM_AE_UG_1_0;
    p10 = data.PM_AE_UG_10_0;
    p25 = data.PM_AE_UG_2_5;
    
    Serial.print("PM 1.0 (ug/m3): ");
    Serial.println(p1);

    Serial.print("PM 2.5 (ug/m3): ");
    Serial.println(p25);

    Serial.print("PM 10.0 (ug/m3): ");
    Serial.println(p10);
   // sendData(p1,p25,p10);
  }
  else
  {
    Serial.println("No data.");
  }
//sendData(1.0, 2.5, 10.0);
  Serial.println("Going to sleep");
  pms.sleep();

}

void closeDustSensor()
{
  SerialPMS.end();
}

/**
 * Send Data
 */
void publishData()
{
  //setupGSM();
  SerialAT.listen();
  delay(1000);
  sendData(p1, p25, p10);
  //closeGSM();
}
 
void setupGSM()
{
  //delay(5000);
  int rate = 9600;


  DBG("Trying baud rate", rate, "...");
    SerialAT.begin(rate);
    delay(10);
    bool notConnected = true;
    while(notConnected) {
      SerialAT.print("AT\r\n");
      String input = SerialAT.readString();
      if (input.indexOf("OK") >= 0) {
        DBG("Modem responded at rate", rate);
        notConnected=false;
      }
      DBG("wait connexion");
      delay(1000);
    }
  delay(3000);
}
void closeGSM()
{
  SerialAT.end();
}
void sendData(float p1, float p25, float p10)
{
    // Restart takes quite some time
  // To skip it, call init() instead of restart()
  DBG("Initializing modem...");
  while (!modem.restart()) {
    delay(10000);
    //return;
  }
  // Unlock your SIM card with a PIN
 // modem.simUnlock("0000");
  Serial.print(F("Waiting for network..."));
  if (!modem.waitForNetwork()) {
    Serial.println(" fail");
    delay(10000);
    return;
  }
  Serial.println(" OK");
  
bool res;

  res = modem.sendSMS("+33688334906", String("Air quality P1:"+String(p1)+"; P2,5:"+String(p25)+"; P10:"+String(p10)));
  DBG("SMS:", res ? "OK" : "fail");

Serial.print("Connecting to ");
  Serial.print(apn);
 if (!modem.gprsConnect(apn,"wap","wapwap")){
    Serial.println(" fail");
    while (true);
  }
 Serial.println(" OK");
  broker = "broker.hivemq.com";//"iot.eclipse.org"; //"broker.hivemq.com";
  mqtt.setServer(broker, 1883);
  Serial.print("Connecting to ");
  Serial.print(broker);
  if (0 == mqtt.connect("GsmClientTest","gest","gest")) {
     Serial.print(mqtt.state());
      Serial.println(" fail");
    return false;
  }
  Serial.println(" OK");
  String message = String(SENSOR_NAME )+";"+String(p1)+";"+String(p25)+";"+String(p10);
 
  unsigned int bufSize = message.length() + 1; //String length + null terminator
  char cmessage[bufSize];
  message.toCharArray(cmessage, bufSize);
  Serial.print("to send");
  Serial.println(cmessage); 
  //char cmessage[50];
//message.toCharArray(cmessage,50);
  if(!mqtt.publish(topicSensor, cmessage))
  {
    Serial.println(" fail to send");
  }
  else
  {
     Serial.println("sent OK");
  }
  
  mqtt.disconnect ();
//mqtt.close();
  
}

