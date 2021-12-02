import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { map } from "rxjs/operators";
import { ApiService } from "./api.service";

@Injectable()
export class SensorService {
  private subject = new Subject<Map<number, SensorCyclic>>();
  private _sensors = new Map<number, SensorCyclic>();
  private _lastLoad = new Date(2000);

  constructor(private api: ApiService) {}

  emit(): void {
    this.subject.next(this._sensors);
  }

  observeAll(): Observable<SensorCyclic[]> {
    return this.subject.pipe(map((sensors) => [...sensors.values()]));
  }

  observeBySensorId(id: number): Observable<SensorCyclic | undefined> {
    return this.subject.pipe(map((sensors) => sensors.get(id)));
  }

  lazyLoad(): void {
    console.log(
      `Last load of sensors ${this._lastLoad} => (${
        new Date().getTime() - this._lastLoad.getTime()
      }`
    );
    if (new Date().getTime() - this._lastLoad.getTime() > 60000) this.load();
    else this.emit();
  }

  load(): void {
    this._lastLoad = new Date();
    this.api.getSensorsCyclic().subscribe((sensors) => {
      this._sensors = new Map<number, SensorCyclic>();
      for (let s of sensors) this._sensors.set(s.id, s);
      this.emit();
    });
  }

  getAll(): SensorCyclic[] {
    return [...this._sensors.values()];
  }

  getOne(id: number): SensorCyclic | undefined {
    return this._sensors.get(id);
  }
}
