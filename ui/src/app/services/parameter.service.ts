import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class ParameterService {

    constructor(private httpClient: HttpClient) { }
}