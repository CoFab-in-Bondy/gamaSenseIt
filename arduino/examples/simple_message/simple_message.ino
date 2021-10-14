#include <GamaSenseIt.h>

// for aduino UNO
//       Arduino      Radio module
//          GND----------GND   (ground in)
//          3V3----------3.3V  (3.3V in)
//   SS pin D10----------NSS   (CS chip select in)
//  SCK pin D13----------SCK   (SPI clock in)
// MOSI pin D11----------MOSI  (SPI Data in)
// MISO pin D12----------MISO  (SPI Data out)

// for aduino MEGA
//       Arduino      Radio module
//          GND----------GND   (ground in)
//          3V3----------3.3V  (3.3V in)
//   SS pin D10----------NSS   (CS chip select in)
//  SCK pin D52----------SCK   (SPI clock in)
// MOSI pin D51----------MOSI  (SPI Data in)
// MISO pin D50----------MISO  (SPI Data out)


int i = 0;

void setup() {
  Serial.begin(9600);
  //**********************************
  //Paramètres de connexion du capteur au réseau 
  //**********************************
  String sensorName = "my_sensor"; //nom du capteur sur le réseau LORA 
  int sensorAddress = 2; //Adresse du capteur, L'adresse 0 est réservée pour la gateway
  
  //Configuration des paramètres réseau
  int res = gamaSenseIt.configure(sensorName,sensorAddress);
}

void loop() {
  i++;
  //envoi d'un message à la gateway
  gamaSenseIt.sendDataToGateway("Bonjour le Monde " + String(i));
  Serial.print("Bonjour le Monde " + String(i));
}
