/*
 * CarCounter.cpp
 *
 *  Created on: 23 juil. 2017
 *      Author: nicolas
 */

#include "CarCounter.h"


#include <mutex>
#include <thread>
#include "Lidar.h"
#include <exception>

using namespace unistd;
using namespace std;

CarCounter::CarCounter() {
	endOfWay = 10000;////s
	lastDistance = 0;
	mesuredDistance = new MeasuredDistance[BUFFER_SIZE];
	this->carArrival = new CarArrival[BUFFER_SIZE];
	measureReadIndex = 0;
	measureWriterIndex = 0;
	this->carReadIndex = 0;
	this->carWriterIndex = 0;
	isStarting = false;
}


CarCounter::~CarCounter() {
	// TODO Auto-generated destructor stub
}

MeasuredDistance CarCounter::getDistanceData()
{
	if(this->measureReadIndex == this->measureWriterIndex)
		return MeasuredDistance();
	return this->mesuredDistance[(measureReadIndex++)%BUFFER_SIZE];
}

bool CarCounter::hasMoreDistanceData()
{
	return this->measureReadIndex != this->measureWriterIndex;
}

void CarCounter::pushDistanceData(int distance)
{
	this->mesuredDistance[(measureWriterIndex)%BUFFER_SIZE].distance = distance;
	this->mesuredDistance[(measureWriterIndex)%BUFFER_SIZE].captureDate =  std::chrono::high_resolution_clock::now();
	measureWriterIndex++;
}


CarArrival CarCounter::getArrivalData()
{
	if(this->carReadIndex == this->carWriterIndex)
		return CarArrival();
	return this->carArrival[(carReadIndex++)%BUFFER_SIZE];
}

bool CarCounter::hasMoreArrivalData()
{
	return this->carReadIndex != this->carWriterIndex;
}

void CarCounter::stop()
{
	this-> isStarting = false;
}

void CarCounter::start()
{


	Lidar* counter= new Lidar(9);
	isStarting = true;
	while(isStarting)
	{
		int dst = counter->getDistance();
//		cout <<"distance "<<dst<<" "<<lastDistance<<endl;
		if(dst < this->lastDistance - BUFFER_DISTANCE || dst > this->lastDistance + BUFFER_DISTANCE)
		{
			this->lastDistance = dst;
			this->pushDistanceData(dst);
		}

	}
}

int main(int argc, char *argv[])
{






	CarCounter car;
	string cmd=argv[1];
	std::thread t1([&car]() {
//		std::cout<<"sfdsfdsgfgdfgfdgfds"<<endl;
		car.start();
//		cout <<" dfdsq "<<endl;
	});
	std::thread t2([&car,&cmd]() {
		ofstream dictionary;
		dictionary.open(cmd,std::ios_base::app | std::ios_base::out);


		while(true)
		{
			if(car.hasMoreDistanceData())
			{
				MeasuredDistance m = car.getDistanceData();
				long long tmp = m.captureDate.time_since_epoch().count();

					std::chrono::system_clock::duration tp = m.captureDate.time_since_epoch();
				    std::chrono::hours h = std::chrono::duration_cast<std::chrono::hours>(tp);
				    tp -= h;
				    std::chrono::minutes g = std::chrono::duration_cast<std::chrono::minutes>(tp);
				    tp -= g;
				    std::chrono::seconds s = std::chrono::duration_cast<std::chrono::seconds>(tp);
				    tp -= s;




//				dictionary <<tmp<<"\t"<<m.distance<<endl;
				cout<<"mesure: "<<tmp<<"\t"<<m.distance<<"\t"<<g.count()<<"min \t"<<s.count()<<"sec \t"<<endl;
			}

		}
		dictionary.close(); //explicite
	});
	t1.join();
	t2.join();


}

