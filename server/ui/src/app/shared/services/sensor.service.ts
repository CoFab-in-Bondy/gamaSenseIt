import { Injectable } from "@angular/core";
import { SafeUrl } from "@angular/platform-browser";
import { Observable } from "rxjs";
import { saveAs } from 'file-saver';
import * as QRCode from "qrcode";
import {HttpClient} from "@angular/common/http";
import {ServerService} from "@services/server.service";
import {SecurePipe} from "@pipes/secure.pipe";

@Injectable()
export class SensorService {

  constructor(private http: HttpClient,
              private server: ServerService,
              private secure: SecurePipe) {}

  create(sensor: FormData): Observable<Sensor<true>> {
    return this.http.post<Sensor<true>>("/private/sensors", sensor);
  }

  update(id: number, sensor: FormData): Observable<Sensor<true>> {
    return this.http.post<Sensor<true>>(`/private/sensors/${id}`, sensor);
  }

  getById(id: number): Observable<Sensor<true>> {
    return this.http.get<Sensor<true>>(`/public/sensors/${id}?`);
  }

  getParametersOfId(id: number, options: ParamsOption = {}): Observable<RecordParameters> {
    return this.http.get<RecordParameters>(`/public/parameters`, {params: { sensorId: id, ...<any>options} } );
  }

  download(params: QueryParams): Observable<void> {

    return new Observable(
      o=>{
        this.http.get("/public/parameters/download", {
          params: <any>params,
          responseType: 'blob',
          observe: 'response'
        }).subscribe(
          res => {
            if (!res.body) {
              console.error("Empty body for download");
              return;
            }
            const blob = new Blob([res.body], { type: this.server.getContentType(res.headers) });
            const url = window.URL.createObjectURL(blob);
            saveAs(url, this.server.getFilename(res.headers));
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

  getData(sensorId: number, parameterMetadataId: number): Observable<Data[]> {
    return this.http.get<Data[]>(`/public/sensors/${sensorId}/data/${parameterMetadataId}`);
  }

  qrcode(sensorId: number) {
    const parsedUrl = new URL(window.location.href);
    const url = parsedUrl.origin + `/sensors/${sensorId}`;
    const opts = {
      errorCorrectionLevel: 'H',
      type: 'image/png',
      margin: 1,
      width: 512,
      color: {
        dark:"#000000FF",
        light:"#FFFFFFFF"
      }
    }
    // @ts-ignore
    QRCode.toDataURL(url, opts, (err, data) => {
      saveAs(data, `qrcode-${sensorId}.png`);
    });
  }
}
