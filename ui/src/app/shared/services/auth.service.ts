import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable()
export class AuthService {
  constructor(private http: HttpClient, private router: Router ) { }

  getStorage() {
    return localStorage;
  }

  isAuth(): boolean {
    return this.getToken() != null;
  }

  getToken(): string|null {
    return this.getStorage().getItem('token');
  }

  getBodyToken(): any|null {
    if (this.isAuth()) {
      const b64Payload = this.getToken()!.split(".", 3)[1];
      const payload = JSON.parse(Buffer.from(b64Payload, 'base64').toString('binary'));
      return payload;
    }
    return null;
  }

  extractToken(fieldName: string): any {
    return this.getBodyToken()?.[fieldName];
  }

  setToken(token: string): void {
    if (this.isAuth())
      this.delToken();
    return this.getStorage().setItem('token', token);
  }

  delToken(): void {
    return this.getStorage().removeItem('token');
  }

  private postLogin(username: string, password: string): Observable<any> {
    return this.http.post<any>("/auth/login", {
      username: username,
      password: password
    });
  }

  private postResgister(username: string, password: string): Observable<any> {
    return this.http.post<any>("/auth/register", {
      username: username,
      password: password
    });
  }

  login(username: string, password: string): Observable<void> {
    return new Observable(o => {
      this.postLogin(username, password).subscribe(
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
      this.postResgister(username, password).subscribe(
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

  roles(): string[] {
    return this.extractToken('roles') || [];
  }

  isAdmin(): boolean {
    return this.roles().includes('ADMIN');
  }

  isUser(): boolean {
    return this.roles().includes('USER') || this.isAdmin();
  }

  username(): string|null {
    return this.extractToken('sub');
  }

  withPermissionOrRedirect(check: (()=>boolean) = ()=>true): boolean {
    if (this.isAuth()) {
      if(check()) {
        return true
      } else {
        this.router.navigate(['/error403']);
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
