import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

export interface Sensor {}

@Injectable()
export class SensorService {
  sensorSubject = new Subject<any[]>();
  private _sensors: Array<any[]> = [];

  constructor(private httpClient: HttpClient) {}

  emitSensorSubject() {
    this.sensorSubject.next(this._sensors);
  }

  loadSensorsToServer() {
    this.httpClient.get<any[]>("/public/sensors").subscribe(
      (res) => {
        this._sensors = res;
        console.log(this._sensors);
        this.emitSensorSubject();
      },
      (err) => {
        console.error(err);
      }
    );
  }

  get sensors(): any[] {
    return this._sensors;
  }

  
}
