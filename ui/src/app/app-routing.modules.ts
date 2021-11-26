import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Error404Component } from './components/error404/error404.component';
import { SensorSingleComponent } from './components/sensor-single/sensor-single.component';
import { SensorsListComponent } from './components/sensors-list/sensors-list.component';

const routes: Routes = [
  {path: "", redirectTo: '/sensors', pathMatch: 'full'},
  {path: "sensors", component: SensorsListComponent},
  {path: "sensors/:id", component: SensorSingleComponent},
  {path: "error404", component: Error404Component},
  {path: "**", redirectTo: '/error404'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
