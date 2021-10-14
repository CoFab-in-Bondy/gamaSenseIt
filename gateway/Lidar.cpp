/*
 * CarCounter.cpp
 *
 *  Created on: 23 juil. 2017
 *      Author: nicolas
 */

#include "Lidar.h"

using namespace unistd;

Lidar* counter;

int Lidar::getDistance()
{
	int c = 0, d = 0;
	while(digitalRead(activationPin) == LOW);
auto current = std::chrono::high_resolution_clock::now();
	while(digitalRead(activationPin) == HIGH) ;
auto elapsed = std::chrono::high_resolution_clock::now() - current ;
	chrono::microseconds microseconds = chrono::duration_cast<std::chrono::microseconds> (elapsed);
	return microseconds.count() / 10;
}

Lidar::Lidar(int pin) {
	activationPin = pin;
	lastMeasure = 0;
	currentDistance = 0;
	lastTimeUpdade = std::chrono::high_resolution_clock::now();
	pinMode(activationPin, Pinmode(INPUT));

}

void Lidar::stop()
{
	detachInterrupt(activationPin);
}

int Lidar::getLastDistance()
{
	return this->lastMeasure;
}

Lidar::~Lidar() {
	
}

/*
int main(int argc, char *argv[])
{

   cout<<"pin " << raspberryPinNumber(9)<<endl;
#ifdef RASPBERRY2
	cout<< "coucou "<<endl;

#endif

	pinMode(9, Pinmode(INPUT));
	counter= new Lidar(9);
	while(true)
	{
		int dst = counter->getLastDistance();
		cout<<"distance "<<dst<<endl;
	}

}*/

