import { Injectable } from "@angular/core";
import { CanActivate, Router, UrlTree } from "@angular/router";
import { AuthService } from "../services/auth.service";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService ) { }

  async canActivate(): Promise<boolean | UrlTree> {
    if (await this.auth.isAuth()) {
      return true;
    } else {
      window.location.href = 'https://localhost:8443/login';
      return false;
    }
  }
}
