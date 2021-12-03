import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { ApiService } from "./api.service";

@Injectable()
export class ParameterService {
  private _subject = new Map<number, Subject<Parameter[]>>();
  private _parametersBySensorId = new Map<number, Parameter[]>();
  private _lastLoadBySensorId = new Map<number, Date>();
  private static EMPTY_SUBJECT = new Subject<Parameter[]>();

  constructor(private api: ApiService) {}

  emitBySensorId(id: number): void {
    let sensors = this.getBySensorId(id);
    this.observeBySensorId(id).next(sensors);
  }

  lazyLoadBySensorId(id: number): void {
    let lastLoad = this._lastLoadBySensorId.get(id);
    console.log(
      `Last load of parameters ${lastLoad} => ${
        new Date().getTime() - (lastLoad ? lastLoad.getTime() : 0)
      }`
    );
    if (
      lastLoad === undefined ||
      new Date().getTime() - lastLoad.getTime() > 60000
    )
      this.loadBySensorId(id);
    else this.emitBySensorId(id);
  }

  loadBySensorId(id: number): void {
    this._lastLoadBySensorId.set(id, new Date());
    this.api.getParameters({ sensorId: id }).subscribe(
      (parameters) => {
        this._parametersBySensorId.set(id, parameters);
        this.emitBySensorId(id);
      },
      (err) => console.error(err)
    );
  }

  getBySensorId(id: number): Parameter[] {
    return this._parametersBySensorId.get(id) || [];
  }

  observeBySensorId(id: number): Subject<Parameter[]> {
    return this._subject.get(id) || ParameterService.EMPTY_SUBJECT;
  }

  download(params: QueryParameters): void {
    this.api.downloadSensorParameters(params);
  }
}
