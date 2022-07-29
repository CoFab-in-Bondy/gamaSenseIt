import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {saveAs} from "file-saver";
import {HttpClient} from "@angular/common/http";
import { map } from 'rxjs/operators';


@Injectable()
export class BinaryService {

  constructor(private http: HttpClient) {

  }

  postToken(sensorId: number, data: any): Observable<string> {
    return this.http.post<{token: string}>(`/private/binary/token/${sensorId}`, data).pipe(map(tk => tk.token));
  }

  getBinaryFile(token: string): Observable<void> {
    return new Observable(o => {
      this.http.get(`/public/binary/download`, {
        responseType: 'blob',
        params: {token: token},
        observe: 'response'
      }).subscribe(
        resp => {
          const contentDisposition = resp.headers.get('content-disposition');
          const filename = contentDisposition
            ? contentDisposition.split(';')[1].split('filename')[1].split('=')[1].replace(/"/g, '').trim()
            : "default.gmst";
          if (resp.body == null) return;
          const blob = new Blob([resp.body], {type: 'application/octet-stream'});
          const url = window.URL.createObjectURL(blob);
          saveAs(url, filename);
          o.next();
          o.complete();
        },
        err=>{o.error(err); o.complete()}
      );
    });
  }
}
