import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable()
export class SensorMetadataService {

  constructor(private http: HttpClient) {}

  getAll(): Observable<SensorMetadata<true>[]> {
    return this.http.get<SensorMetadata<true>[]>(`/public/sensors/metadata`);
  }
}
