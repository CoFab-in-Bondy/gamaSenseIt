import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {ServerService} from "@services/server.service";

@Injectable()
export class ServerGuard implements CanActivate {

  constructor(private server: ServerService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    document.location.href = this.server.getServerUrl();
    return true;
  }

}
