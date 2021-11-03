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
import { RouterModule, Routes } from '@angular/router';
import { Error404Component } from './error404/error404.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

const appRoutes: Routes = [
    {path: "sensors", component: SensorsListComponent},
    {path: "", component: SensorsListComponent},
    {path: "error404", component: Error404Component},
    {path: "**", redirectTo: '/error404'}
]


@NgModule({
  declarations: [
    AppComponent,
    SensorsListComponent,
    SensorsItemComponent,
    Error404Component
  ],
  imports: [
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
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
