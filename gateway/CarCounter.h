/*
 * CarCounter.h
 *
 *  Created on: 23 juil. 2017
 *      Author: nicolas
 */

#ifndef CARCOUNTER_H_
#define CARCOUNTER_H_

#define BUFFER_SIZE 200
#define BUFFER_DISTANCE 50

#include<chrono>
#include <iostream>
#include <fstream>
#include"Lidar.h"


using namespace std;


struct MeasuredDistance
{
	chrono::high_resolution_clock::time_point captureDate;
	int distance;
};

struct CarArrival
{
	chrono::high_resolution_clock::time_point captureDate;
};

class CarCounter {
private:
	int endOfWay;
	int lastDistance;

	MeasuredDistance* mesuredDistance;
	int measureReadIndex;
	int measureWriterIndex;

	CarArrival* carArrival;
	int carReadIndex;
	int carWriterIndex;
	bool isStarting;




public:
	CarCounter();
	virtual ~CarCounter();
	bool hasMoreDistanceData();
	bool hasMoreArrivalData();
	MeasuredDistance getDistanceData();
	void pushDistanceData(int distance);
	CarArrival getArrivalData();
	void start();
	void stop();
	void initEndOfWay();
};



#endif /* CARCOUNTER_H_ */
