import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { ApiService } from "@services/api.service";

@Injectable()
export class AuthService {
  constructor(private api: ApiService, private router: Router ) { }

  isAuth(): boolean {
    return this.getToken() != null;
  }

  getToken(): string|null {
    return sessionStorage.getItem('token');
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
