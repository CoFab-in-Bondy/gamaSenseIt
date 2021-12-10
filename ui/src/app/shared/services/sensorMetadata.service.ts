import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { map } from "rxjs/operators";
import { ApiService } from "./api.service";

@Injectable()
export class SensorMetadataService {
  private subject = new Subject<SensorMetadataExtended[]>();
  private _sensorsMetadata: SensorMetadataExtended[] = [];
  private _lastLoad = new Date(2000);

  constructor(private api: ApiService) {}

  emit(): void {
    this.subject.next(this._sensorsMetadata);
  }

  observeAll(): Observable<SensorMetadataExtended[]> {
    return this.subject;
  }

  lazyLoad(): void {
    console.log(
      `Last load of sensors ${this._lastLoad} => (${
        new Date().getTime() - this._lastLoad.getTime()
      }`
    );
    if (new Date().getTime() - this._lastLoad.getTime() > 60000)
      this.load();
    else this.emit();
  }

  load(): void {
    this._lastLoad = new Date();
    this.api.getSensorsMetadataExtended().subscribe(
      sensorsMetadata => {
        this._sensorsMetadata = sensorsMetadata;
        this.emit();
      }
    );
  }

  getAll(): SensorMetadataExtended[] {
    return this._sensorsMetadata;
  }
}
