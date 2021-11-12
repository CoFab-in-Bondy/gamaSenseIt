import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class ParameterService {

  constructor(private httpClient: HttpClient) {}

  getSensorParameters(sensorId: number): Observable<any[]>{
    return this.httpClient.get<any[]>(
      "/public/parameters", 
      {params: {sensorId: sensorId}}
    )
  }

  dowloadSensorParameters(sensorId: number, type: "csv" | "json"): void {
    window.open("/public/parameters?sensorId=" + sensorId + "&type=" + type, '_blank');
  }

  private download(data: Blob): void {
    // const blob = new Blob([data], { type: "text/plain" });
    console.log(data);
    const url = window.URL.createObjectURL(data);
    window.open(url);
    window.URL.revokeObjectURL(url);
  }
}