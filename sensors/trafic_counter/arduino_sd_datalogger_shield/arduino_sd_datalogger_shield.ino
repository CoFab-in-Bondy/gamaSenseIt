#include <OneWire.h>
#include <SPI.h>
#include <SD.h>
#include <Wire.h>
#include "RTClib.h"
const int chipSelect = 10; //cs or the save select pin from the sd shield is connected to 10.
RTC_DS1307 RTC;
#include <LIDARLite.h>

LIDARLite myLidarLite;

File dataFile;
DateTime now;
unsigned long timeMilli;
int distance;
int mdelay ;
unsigned long lastUpdate=0;
void setup(void) {
  Serial.begin(115200);
  //setup lidarLite
   myLidarLite.begin(0, true);
  
  //setup clock
  Wire.begin();
  RTC.begin();
//check or the Real Time Clock is on
  if ( false &&! RTC.isrunning()) {
    Serial.println("RTC is NOT running!");
    // following line sets the RTC to the date & time this sketch was compiled
    // uncomment it & upload to set the time, date and start run the RTC!
    RTC.adjust(DateTime(__DATE__, __TIME__));
  }
//setup SD card
   Serial.print("Initializing SD card...");

  // see if the SD card is present and can be initialized:
  if (!SD.begin(chipSelect)) {
    Serial.println("Card failed, or not present");
    // don't do anything more:
    return;
  }
  Serial.println("card initialized.");

  //write down the date (year / month / day         prints only the start, so if the logger runs for sevenal days you only findt the start back at the begin.
    now = RTC.now();
    timeMilli = millis();
    
    dataFile = SD.open("datalog.txt", FILE_WRITE);
    dataFile.print("Start logging on: ");
    dataFile.print(now.year(),DEC);
    dataFile.print('/');
    dataFile.print(now.month(),DEC);
    dataFile.print('/');
    dataFile.print(now.day(),DEC);
    dataFile.print("\t");
    dataFile.print("at: ");
    dataFile.print(now.hour(),DEC);
    dataFile.print("h ");
    dataFile.print(now.minute(),DEC);
    dataFile.print("min ");
    dataFile.print(now.second(),DEC);
    dataFile.print("s \t");
    dataFile.print("arduino Clock: ");
    dataFile.println(timeMilli,DEC);
    dataFile.println("********************************************");
    dataFile.println("Distance\tTime");
    dataFile.flush();
   // dataFile.close();
}

void loop(void) {

    timeMilli = millis();
    distance = myLidarLite.distance();
    
    dataFile.print(distance,DEC);
    dataFile.print("\t");
    dataFile.print(timeMilli,DEC);
    dataFile.print("\n");
    if(millis() - lastUpdate >  5000)
    {
      dataFile.flush();
      lastUpdate = millis();
      Serial.println((millis() - timeMilli));
    }
    while(millis() - timeMilli <50);
  
}


