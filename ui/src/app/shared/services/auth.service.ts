import { Injectable } from "@angular/core";
import { CanActivate, Router, UrlTree } from "@angular/router";
import { ApiService } from "../services/api.service";

@Injectable()
export class AuthService {
  constructor(private api: ApiService ) { }

  async getState(): Promise<AuthMe>{
    return await this.api.getAuthMe().toPromise();
  }

  async isAuth(): Promise<boolean>{
    return (await this.getState()).auth;
  }
}
