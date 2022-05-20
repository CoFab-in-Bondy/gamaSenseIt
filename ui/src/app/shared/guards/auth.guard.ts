import { Injectable } from "@angular/core";
import { CanActivate, UrlTree } from "@angular/router";
import { AuthService } from "@services/auth.service";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService) { }

  async canActivate(): Promise<boolean | UrlTree> {
    return this.auth.withPermissionOrRedirect();
  }
}
