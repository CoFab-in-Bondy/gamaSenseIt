import { DatePipe } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {AccessPrivilege} from "../../constantes";

@Injectable()
export class AccessService {
  constructor(private http: HttpClient, public datepipe: DatePipe) {}

  create(name: string, maintenance: boolean): Observable<Access> {
    const data = new FormData();
    data.append("access", new Blob(
      [JSON.stringify({
        "name": name,
        "privilege": maintenance? AccessPrivilege.MAINTENANCE: AccessPrivilege.SOCIAL
      })],
    {
        type: "application/json; charset=utf-8",
      }
    ));
    return this.http.post<Access>("/private/accesses", data);
  }

  search({ query }: {query: string} ): Observable<Access[]> {
    return this.http.get<Access[]>("/private/accesses/search", {
      params: { query },
    });
  }

  searchById(
    id: number|string,
    args: {query?: string, sensor?: boolean, user?: boolean, in?: boolean, out?: boolean}
  ): Observable<AccessSearch> {
    return this.http.get<AccessSearch>(`/private/accesses/${id}/search`, {
      params: args,
    });
  }

  getById(id: number|string): Observable<Access> {
    return this.http.get<Access>(`/private/accesses/${id}`);
  }

  addSensor(accessId: number, sensorId: number): Observable<void> {
    return this.http.post<void>(`/private/accesses/${accessId}/sensors`, { sensorId });
  }

  delSensor(accessId: number, sensorId: number): Observable<Object> {
    return this.http.delete(`/private/accesses/${accessId}/sensors/${sensorId}`);
  }

  addUser(accessId: number, userId: number): Observable<void> {
    return this.http.post<void>(`/private/accesses/${accessId}/users`, { userId });
  }

  delUser(accessId: number, userId: number): Observable<Object> {
    return this.http.delete(`/private/accesses/${accessId}/users/${userId}`);
  }

  promoteUser(accessId: number, userId: number): Observable<void> {
    return this.http.post<void>(`/private/accesses/${accessId}/users/${userId}/promote`, {});

  }

  dismissUser(accessId: number, userId: number): Observable<void> {
    return this.http.post<void>(`/private/accesses/${accessId}/users/${userId}/dismiss`, {});
  }
}
