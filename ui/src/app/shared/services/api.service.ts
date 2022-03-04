import { DatePipe } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class ApiService {

  constructor(private http: HttpClient, public datepipe: DatePipe) {}

  date(date: Date): string {
    return this.datepipe.transform(date, "MMddyyyy") || "01012000";
  }

  getServerDate(): Observable<Date> {
    return this.http.get<Date>("/public/server/date");
  }

  getAuthMe(): Observable<AuthMe> {
    /* TODO change this for production */
    return new Observable(o=>{o.next({
      roles: ["ADMIN"],
      name: "***REMOVED***",
      auth: true
    }); o.complete()});
    // return this.http.get<AuthMe>("/auth/me");
  }

  postLogin(username: string, password: string): Observable<any> {
    return this.http.post<any>("/auth/login", {
      username: username,
      password: password
    });
  }

  postResgister(username: string, password: string): Observable<any> {
    return this.http.post<any>("/auth/register", {
      username: username,
      password: password
    });
  }

  getServerSeparator(): Observable<string> {
    return this.http.get<string>("/public/server/separator");
  }

  buildFilename(params: QueryParams, ext?: string) {
    const {parameterMetadataId, start, end, type, sensorId} = params;
    return (
      parameterMetadataId == undefined ?
        "parameters" : parameterMetadataId)
      + "-"
      + sensorId
      + (start != undefined || end != undefined
      ? "-"
      + (start == undefined ? "X" : start)
      + "-"
      + (end == undefined ? "X" : end)
      : "")
      + "."
      + type
      + (ext? `.${ext}`: "");
  }

  downloadSensorParameters(params: QueryParams): Observable<void> {

    return new Observable(
      o=>{
        this.http.get("/public/parameters/download", { params: <any>params, responseType: 'blob'}).subscribe(
          data=>{
            const blob = new Blob([data], { type: 'application/zip' });
            const url = window.URL.createObjectURL(blob);
            const anchor = document.createElement("a");
            anchor.download = this.buildFilename(params, ".zip");
            anchor.href = url;
            anchor.click();
            anchor.remove();
            o.next();
            o.complete();
          },
          err=>{
            o.error(err);
            o.complete();
          }
        )

      }
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
    return this.http.get<SensorMetadataExtended[]>(`/public/sensors/metadata`);
  }

  getSensorByIdExtended(id: number, options: ParamsOption = {}): Observable<SensorExtended> {
    return this.http.get<SensorExtended>(`/public/sensors/${id}?`, {params: <Params>options} );
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
