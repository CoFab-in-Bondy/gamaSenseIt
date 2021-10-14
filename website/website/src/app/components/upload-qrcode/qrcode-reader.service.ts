import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

declare const qrcode: any;

@Injectable({
  providedIn: 'root'
})

export class QrcodeReaderService {

  decodeService(file: any): Observable<string> {

    return new Observable(observer => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e: any) => {
        const data = e.target.result;
          qrcode.callback = (res) => {
            observer.next(res);
            observer.complete();
          };
          qrcode.decode(data);
      };
    });
  }
}
