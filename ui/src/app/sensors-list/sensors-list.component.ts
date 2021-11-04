import { SensorService } from './../services/sensor.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-sensors-list',
  templateUrl: './sensors-list.component.html',
  styleUrls: ['./sensors-list.component.scss']
})
export class SensorsListComponent implements OnInit, OnDestroy {


  sensorSubscription: Subscription;
  sensors: any[];

  constructor(private sensorService: SensorService) { }

  ngOnInit(): void {
    this.sensorSubscription = this.sensorService.sensorSubject.subscribe(
      (sensors: any[]) => {
        this.sensors = sensors;
      }
    );
    this.sensorService.loadSensorsToServer();
  }

  ngOnDestroy(): void {
    this.sensorSubscription.unsubscribe();
  }

}
