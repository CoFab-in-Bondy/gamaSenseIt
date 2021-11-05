import { ServerService } from './services/server.service';
import { ParameterService } from './services/parameter.service';
import { ParameterMetadataService } from './services/parameter-metadata.service';
import { SensorService } from './services/sensor.service';
import { SensorMetadataService } from './services/sensor-metadata.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { SensorsListComponent } from './sensors-list/sensors-list.component';
import { SensorsItemComponent } from './sensors-item/sensors-item.component';
import { Error404Component } from './error404/error404.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SensorSingleComponent } from './sensor-single/sensor-single.component';
import { AppRoutingModule } from './app-routing.modules';
import { Error500Component } from './error500/error500.component';


@NgModule({
  declarations: [
    AppComponent,
    SensorsListComponent,
    SensorsItemComponent,
    Error404Component,
    SensorSingleComponent,
    Error500Component
  ],
  imports: [
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
      ParameterMetadataService,
      ParameterService,
      SensorMetadataService,
      SensorService,
      ServerService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
