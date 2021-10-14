/*
 * CarCounter.h
 *
 *  Created on: 23 juil. 2017
 *      Author: nicolas
 */

#ifndef LIDAR_H_
#define LIDAR_H_

#include "arduPi_pi2.h"
#include <iostream>
#include <chrono>

using namespace std;

class Lidar {

private:
	int activationPin;
	int currentDistance;
	std::chrono::high_resolution_clock::time_point lastTimeUpdade;
	int lastMeasure;

public:
	void start();
	void stop();
	int getDistance();
	int getLastDistance();
	void distanceDetected();
	Lidar(int pin);
	virtual ~Lidar();
};




#endif /* LIDAR_H_ */
