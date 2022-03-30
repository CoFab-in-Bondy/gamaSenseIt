import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class ServerService {
  constructor(private http: HttpClient) {}

  getServerDate(): Observable<Date> {
    return this.http.get<Date>("/public/server/date");
  }

  getServerSeparator(): Observable<string> {
    return this.http.get<string>("/public/server/separator");
  }
}
