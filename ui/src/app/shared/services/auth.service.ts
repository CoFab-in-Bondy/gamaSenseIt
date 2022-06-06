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
    return this.getRole() != null;
  }

  getRole(): Roles|null {
    return <Roles|null>this.getStorage().getItem('role');
  }

  setRole(role: Roles): void {
    return this.getStorage().setItem('role', role);
  }

  delRole(): void {
    return this.getStorage().removeItem('role');
  }

  login(username: string, password: string): Observable<void> {
    return new Observable(o => {
      this.http.post<any>('/auth/login', {
        username: username,
        password: password
      }).subscribe(
        res => {
          this.setRole(res['role']);
          o.next();
        },
        err => o.error(err),
        () => o.complete()
      );
    });
  }

  logout(): Observable<any> {
    this.delRole();
    return this.http.post<any>('/auth/logout', '');
  }

  refresh(): Observable<any> {
    return this.http.post<any>('/auth/refresh', '');
  }

  isAdmin(): boolean {
    return this.getRole() == 'ADMIN';
  }

  isUser(): boolean {
    return this.getRole() == 'USER' || this.isAdmin();
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
