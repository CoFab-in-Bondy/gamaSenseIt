import { DatePipe } from "@angular/common";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

@Injectable()
export class ApiService {
  constructor(private http: HttpClient, public datepipe: DatePipe) {}

  date(date: Date): string {
    return this.datepipe.transform(date, "MMddyyyy") || "01012000";
  }

  queryToHttpParams(params: QueryParameters): HttpParams {
    let http = new HttpParams();
    http = http.set("sensorId", params.sensorId);
    if (params.parameterMetadataId !== undefined)
      http = http.set("parameterMetadataId", params.parameterMetadataId);
    if (params.start !== undefined)
      http = http.set("start", this.date(params.start));
    if (params.end !== undefined) http = http.set("end", this.date(params.end));
    if (params.type !== undefined) http = http.set("type", params.type);
    console.log("queryToHttpParams return " + http.toString());
    return http;
  }

  getServerDate(): Observable<Date> {
    return this.http.get<Date>("/public/server/date");
  }

  getAuthMe(): Observable<AuthMe> {
    return this.http.get<AuthMe>("/auth/me");
  }

  getServerSeparator(): Observable<string> {
    return this.http.get<string>("/public/server/separator");
  }

  downloadSensorParameters(params: QueryParameters): void {
    window.open(
      "/public/parameters/download?" + this.queryToHttpParams(params),
      "_blank"
    );
  }

  getParameters(params: QueryParameters): Observable<Parameter[]> {
    return this.http.get<Parameter[]>("/public/parameters", {
      params: this.queryToHttpParams(params),
    });
  }

  getSensors(): Observable<Sensor[]> {
    return this.http.get<Sensor[]>("/public/sensors");
  }

  getSensorsMetadata(): Observable<SensorMetadata[]> {
    return this.http.get<SensorMetadata[]>("/public/sensors/metadata");
  }

  getSensorsBySensorsMetadata(sensorMetadataId: number): Observable<Sensor[]> {
    return this.http.get<Sensor[]>(
      `/public/sensors/metadata/${sensorMetadataId}/sensors`
    );
  }

  getParametersMetadataBySensorMetadataId(
    sensorMetadataId: number
  ): Observable<ParameterMetadata[]> {
    return this.http.get<ParameterMetadata[]>(
      `/public/sensors/metadata/${sensorMetadataId}/parameters/metadata`
    );
  }

  getSensorsMetadataExtended(): Observable<SensorMetadata[]> {
    return this.http.get<SensorMetadata[]>(`/public/sensors/metadata/extended`);
  }

  /**
   * Inject all cyclic dependency
   *
   * @returns Array<SensorCyclic>
   */
  getSensorsCyclic(): Observable<SensorCyclic[]> {
    return this.getSensorsMetadataExtended().pipe(
      map((smds) => {
        let sensors = [];
        for (let smd of smds as SensorMetadataCyclic[]) {
          for (let pmd of smd.parametersMetadata as ParameterMetadataCyclic[])
            pmd.sensorMetadata = smd;
          for (let s of smd.sensors as SensorCyclic[]) {
            s.sensorMetadata = smd;
            sensors.push(s);
          }
        }
        return sensors;
      })
    );
  }
}
