import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { SafeUrl } from "@angular/platform-browser";
import { SecurePipe } from "@pipes/secure.pipe";
import { Observable } from "rxjs";
import { saveAs } from 'file-saver';

@Injectable()
export class SensorService {

  constructor(private http: HttpClient, private secure: SecurePipe) {}

  create(sensor: FormData): Observable<SensorExtended> {
    return this.http.post<SensorExtended>("/private/sensors", sensor);
  }

  update(id: number, sensor: FormData): Observable<SensorExtended> {
    return this.http.post<SensorExtended>(`/private/sensors/${id}`, sensor);
  }

  getById(id: number): Observable<SensorExtended> {
    return this.http.get<SensorExtended>(`/public/sensors/${id}?`);
  }

  getParametersOfId(id: number, options: ParamsOption = {}): Observable<RecordParameters> {
    return this.http.get<RecordParameters>(`/public/parameters`, {params: { sensorId: id, ...<any>options} } );
  }

  buildFilename(params: QueryParams, ext?: string): string {
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

  download(params: QueryParams): Observable<void> {

    return new Observable(
      o=>{
        this.http.get("/public/parameters/download", { params: <any>params, responseType: 'blob'}).subscribe(
          data=>{
            const blob = new Blob([data], { type: 'application/zip' });
            const url = window.URL.createObjectURL(blob);
            saveAs(url, this.buildFilename(params, "zip"));
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

  getImage(id: number): Observable<SafeUrl> {
    return this.secure.transform(`/public/sensors/${id}/image`);
  }
}
