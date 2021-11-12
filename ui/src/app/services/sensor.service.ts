import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

export interface Sensor {}

@Injectable()
export class SensorService {
  sensorSubject = new Subject<any[]>();
  private _sensors: any[] = [];

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

  getSensorsById(id: number): any {
    this.loadSensorsToServer();
    for (let sensor of this._sensors) {
      if (sensor['id'] == id) {
        return sensor
      }
    }
  }
}
