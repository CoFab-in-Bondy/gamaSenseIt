import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class ParameterMetadataService {

    constructor(private httpClient: HttpClient) { }
}