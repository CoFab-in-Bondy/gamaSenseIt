#include "PMS.h"
#include <SoftwareSerial.h>
#define SENSOR_NAME "SENSOR_1"
#include <SD.h>
#include "RTClib.h"

RTC_DS1307 rtc;
File myFile;

 float p1 = 0, p10 = 0,  p25 = 0;
//Dust sensor configuration
SoftwareSerial SerialPMS(5,6); //(12, 13);
PMS pms(SerialPMS);
PMS::DATA data;

void setup() {
  pinMode(2, OUTPUT);
  digitalWrite(2, HIGH);
  pinMode(3, OUTPUT);
  digitalWrite(3, LOW);
  Serial.begin(9600);
  while(!Serial);
 
 if (! rtc.begin()) {
    Serial.println("Couldn't find RTC");
 }
 delay(1000);
 
 Serial.println("start SD");
  setupSD();
  delay(1000);
  Serial.println("start dust Sensor");
  setupDustSensor();
  delay(1000);

    
    
    Serial.println("start measure");
    digitalWrite(2, LOW);
}

void showError()
{
   while(true)
    {
      digitalWrite(2, LOW);
      delay(100);
      digitalWrite(2, HIGH);
      delay(100);
    }

}

void setupSD()
{
   Serial.print("Initializing SD card...");

  if (!SD.begin(10)) {
    Serial.println("initialization failed!");
   showError();
    return;
  }
  myFile =  SD.open("datalog.txt", FILE_WRITE);
  if (SD.exists("datalog.txt")) {
    Serial.println("datalog.txt exists.");
  } else {
    showError();
  }
  Serial.print("initialization done." );

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
}


void publishData()
{
  
    //if(!myFile)
   // {
        digitalWrite(2, HIGH);
        DateTime now = rtc.now();
      Serial.print("Writing to DataLog.txt...   ");
       myFile.print(SENSOR_NAME);
       myFile.print(';');
       myFile.print(now.unixtime(), DEC);
       myFile.print(';');  
       myFile.print(now.year(), DEC);
       myFile.print('/');
       myFile.print(now.month(), DEC);
          myFile.print('/');
       myFile.print(now.day(), DEC);
       myFile.print(' ');
       myFile.print(now.hour(), DEC);
       myFile.print(':');
       myFile.print(now.minute(), DEC);
       myFile.print(':');
       myFile.print(now.second(), DEC);
     myFile.print(';');
       myFile.print(p1, DEC);
      myFile.print(';');
      myFile.print(p25, DEC);
      myFile.print(';');
      myFile.println(p10, DEC);
       myFile.flush();
        delay(1000);
        digitalWrite(2, LOW);
        Serial.println("done.");
        myFile.flush();
       // myFile.close();
   /* }
    else
    {
       showError();
    }*/

}

void setupDustSensor()
{
   // configure dust sensor
  SerialPMS.begin(9600);
  pms.passiveMode(); 
  //pms.activeMode();
  delay(10);
}
void getSensorData()
{

  Serial.println("wake up, wait 30 seconds for stable readings...");
  pms.wakeUp();
  delay(30000);

  Serial.println("Send request read...");
  pms.requestRead();

  Serial.println("Wait max. 10 seconds for read...");
  if (pms.read(data, 10000))
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



