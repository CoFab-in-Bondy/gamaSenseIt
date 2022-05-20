import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
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
    return PROXY && PROXY[0] != undefined ? PROXY[0].target : "/";
  }
}
