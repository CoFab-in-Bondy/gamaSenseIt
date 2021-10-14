import { Component, OnInit, SimpleChanges } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { PMLayoutComponent } from '../../layouts/pm-layout/pm-layout.component';

export interface PMSensor {
  sensorName: string;
  displayName: string;
  subDisplayName: string;
  pm1: number;
  pm25: number;
  pm10: number;
  temperature: number;
  humidity: number;
}

@Component({
  selector: 'qr-qameleo',
  templateUrl: './qr-qameleo.component.html',
  styleUrls: ['./qr-qameleo.component.scss']
})
export class QrQameleoComponent implements OnInit {

  PM1_THRESHOLD: number = 25;
  PM2_5_THRESHOLD: number = 25;
  PM10_THRESHOLD: number = 50;

  sensorName: string = "";
  displayName: string = "";
  subDisplayName: string= "";
  pm1: number = 0;
  pm25: number = 0;
  pm10: number = 0;
  PMUnit: string = "μg/m³";
  temperature: number = 0;
  temperatureUnit: string = "°C";
  humidity: number = 0;
  humidityUnit: string = "%";
  url = 'http://vmpams.ird.fr:8080';
  browseQRcode = true;
  httpGetSucceed = true;

  constructor(
    private router: Router,
    private http: HttpClient,
    private parent: PMLayoutComponent) { }

  ngOnInit() {
    if( this.router.url !== '/qameleo') {
      this.browseQRcode = false;
      let i = this.router.url.lastIndexOf('/') + 1;
      this.url = this.url + this.router.url.slice(0,i) +
        'airQualityIndicator?id=' + this.router.url.slice(i);
      this.initValues(this.url);
    } else {
      this.browseQRcode = true;
    }
    setTimeout(() => {
      this.parent.browseQRcode = this.browseQRcode ;
    });
  }

  initValues(url: string){
    this.http.get(url).subscribe(
      (data: PMSensor) => {
        if (data) {
          this.sensorName = data.sensorName;
          this.displayName = data.displayName;
          this.subDisplayName = data.subDisplayName;
          this.pm1 = data.pm1/this.PM1_THRESHOLD*50;
          // this.pm1 = Math.round(this.pm1*100)/100;
          this.pm25 = data.pm25/this.PM2_5_THRESHOLD*50;
          // this.pm25 = Math.round(this.pm25*100)/100;
          this.pm10 = data.pm10/this.PM10_THRESHOLD*50;
          // this.pm10 = Math.round(this.pm10*100)/100;
          this.temperature = data.temperature;
          this.temperature = Math.round(this.temperature);
          this.humidity = data.humidity;
          this.humidity = Math.round(this.humidity);
        } else {
          this.httpGetSucceed = false;
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error instanceof Error) {
          console.log('An error occurred in initValues:', err.error.message);
        } else {
          console.log(`Backend returned code ${err.status},
            body was: ${err.error}`);
        }
        this.httpGetSucceed = false;
      }
    );
  }

  // ngOnChanges(changes: SimpleChanges){
  //   this.pm1 = changes[this.pm1].currentValue;
  //   this.pm25 = changes[this.pm25].currentValue;
  //   this.pm10 = changes[this.pm10].currentValue;
  //   this.temperature = changes[this.temperature].currentValue;
  //   this.humidity = changes[this.humidity].currentValue;
  // }
}
