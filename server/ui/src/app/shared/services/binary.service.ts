import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {saveAs} from "file-saver";
import {HttpClient} from "@angular/common/http";
import { map } from 'rxjs/operators';
import {ServerService} from "@services/server.service";


@Injectable()
export class BinaryService {

  constructor(private http: HttpClient, private server: ServerService) {

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
          if (resp.body == null) return;
          const blob = new Blob([resp.body], {type: 'application/octet-stream'});
          const url = window.URL.createObjectURL(blob);
          saveAs(url, this.server.getFilename(resp.headers));
          o.next();
          o.complete();
        },
        err=>{o.error(err); o.complete()}
      );
    });
  }
}
