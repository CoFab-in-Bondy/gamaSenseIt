import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class ServerService {
  constructor(private httpClient: HttpClient) {}
}
