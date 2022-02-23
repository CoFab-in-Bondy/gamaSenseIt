import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { API } from "src/app/constantes";
import { ApiService } from "./api.service";

@Injectable()
export class SensorService {
  private _subject = new Map<number, Subject<SensorExtended>>();
  private _sensors = new Map<number, SensorExtended>();
  private _lastLoadBySensorId = new Map<number, Date>();
  private static EMPTY_SUBJECT = new Subject<SensorExtended>();

  constructor(private api: ApiService, private http: HttpClient) {}

  emitBySensorId(id: number): void {
    let sensor = this.getBySensorId(id);
    let subject = this.observeById(id);
    if (subject && sensor)
      subject.next(sensor);
  }

  lazyLoadById(id: number): void {
    let lastLoad = this._lastLoadBySensorId.get(id);
    console.log(
      `Last load of parameters ${lastLoad} => ${
        new Date().getTime() - (lastLoad ? lastLoad.getTime() : 0)
      }`
    );
    /*
    if (
      lastLoad === undefined ||
      new Date().getTime() - lastLoad.getTime() > 60000
    )
      this.loadBySensorId(id);
    else this.emitBySensorId(id);
    */
   this.loadBySensorId(id);
  }

  loadBySensorId(id: number): void {
    this._lastLoadBySensorId.set(id, new Date());
    this.api.getSensorByIdExtended(id).subscribe(
      sensor => {
        this._sensors.set(id, sensor);
        this.emitBySensorId(id);
        console.log(sensor);
      },
      (err) => console.error(err)
    );
  }

  getBySensorId(id: number): SensorExtended|null {
    return this._sensors.get(id) || null;
  }

  observeById(id: number): Subject<SensorExtended> {
    return this._subject.get(id) || SensorService.EMPTY_SUBJECT;
  }

  download(params: QueryParams): void {
    this.api.downloadSensorParameters(params);
  }

  addSensor(sensor: PartialSensor): Observable<SensorExtended> {
    return this.http.post<SensorExtended>(API + "/private/sensors", sensor);
  }
}
