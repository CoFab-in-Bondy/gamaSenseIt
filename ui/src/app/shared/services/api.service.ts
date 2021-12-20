import { DatePipe } from "@angular/common";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { API } from "src/app/constantes";

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
    return this.http.get<Date>(API + "/public/server/date");
  }

  getAuthMe(): Observable<AuthMe> {
    /* TODO change this for production */
    return new Observable(o=>{o.next({
      roles: ["ADMIN"],
      name: "***REMOVED***",
      auth: true
    }); o.complete()})
    // return this.http.get<AuthMe>(API + "/auth/me");
  }

  getServerSeparator(): Observable<string> {
    return this.http.get<string>(API + "/public/server/separator");
  }

  downloadSensorParameters(params: QueryParameters): void {
    window.open(
      API + "/public/parameters/download?" + this.queryToHttpParams(params),
      "_blank"
    );
  }
  /*

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
  }*/

  getSensorsMetadataExtended(): Observable<SensorMetadataExtended[]> {
    return this.http.get<SensorMetadataExtended[]>(API + `/public/sensors/metadata/extended`);
  }

  getSensorByIdExtended(id: number): Observable<SensorExtended> {
    return this.http.get<SensorExtended>(API + `/public/sensors/${id}/extended`);
  }

  goToginPage(): void {
    window.location.href = 'https://localhost:8443/login';
  }

  /**
   * Inject all cyclic dependency
   *
   * @returns Array<SensorCyclic>
   */
  /*
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
  }*/
}
