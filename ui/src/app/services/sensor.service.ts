import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class SensorService {

    constructor(private httpClient: HttpClient) { }
}