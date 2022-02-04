import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiService } from "../services/api.service";

@Injectable()
export class AuthService {
  constructor(private api: ApiService ) { }

  isAuth(): boolean {
    return this.getToken() != null;
  }

  getToken(): string|null {
    return sessionStorage.getItem('token');
  }

  setToken(token: string): void {
    if (this.isAuth())
      this.delToken();
    return sessionStorage.setItem('token', token);
  }

  delToken(): void {
    return sessionStorage.removeItem('token');
  }

  login(username: string, password: string): Observable<void> {
    return new Observable(o => {
      this.api.postLogin(username, password).subscribe(
        res => {
          this.setToken(res['token']);
          o.next();
        },
        err => o.error(err),
        () => o.complete()
      );
    });
  }

  resgister(username: string, password: string): Observable<void> {
    return new Observable(o => {
      this.api.postResgister(username, password).subscribe(
        res => {
          this.setToken(res['token']);
          o.next();
        },
        err => o.error(err),
        () => o.complete()
      );
    });
  }

  logout(): void {
    this.delToken();
  }
}
