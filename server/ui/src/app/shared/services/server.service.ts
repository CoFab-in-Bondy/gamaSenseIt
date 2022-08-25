import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
// @ts-ignore
import * as PROXY from "../../../proxy.conf";

@Injectable()
export class ServerService {
  constructor(private http: HttpClient) {}

  getServerDate(): Observable<Date> {
    return this.http.get<Date>("/public/server/date");
  }

  getServerSeparator(): Observable<string> {
    return this.http.get<string>("/public/server/separator");
  }

  getServerUrl(): string {
    return PROXY && PROXY[0] != undefined ? PROXY[0].target : window.location.origin;
  }

  getFilename(headers: HttpHeaders, fallback?: string): string {
    const contentDisposition = headers.get('content-disposition');
    return contentDisposition
      ? contentDisposition.split(';')[1].split('filename')[1].split('=')[1].replace(/"/g, '').trim()
      : (fallback ?? "unnamed.bin");
  }

  getContentType(headers: HttpHeaders, fallback?: string): string {
    return headers.get('content-type') ?? (fallback ?? 'application/octet-stream');

  }
}
