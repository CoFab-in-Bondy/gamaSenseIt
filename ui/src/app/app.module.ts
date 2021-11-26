import { ServerService } from './services/server.service';
import { ParameterService } from './services/parameter.service';
import { SensorService } from './services/sensor.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { SensorsListComponent } from './components/sensors-list/sensors-list.component';
import { SensorsItemComponent } from './components/sensors-item/sensors-item.component';
import { Error404Component } from './components/error404/error404.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SensorSingleComponent } from './components/sensor-single/sensor-single.component';
import { AppRoutingModule } from './app-routing.modules';
import { Error500Component } from './components/error500/error500.component';
import { HeaderComponent } from './components/header/header.component';
import { ApiService } from './services/api.service';
import { DatePipe } from '@angular/common';


@NgModule({
  declarations: [
    AppComponent,
    SensorsListComponent,
    SensorsItemComponent,
    Error404Component,
    SensorSingleComponent,
    Error500Component,
    HeaderComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
      ParameterService,
      SensorService,
      ServerService,
      ApiService,
      DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
