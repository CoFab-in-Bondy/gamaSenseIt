import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./modules/home/home.component";
import { LoginComponent } from "./modules/login/login.component";
import { FormSensorComponent } from "./modules/form-sensor/form-sensor.component";
import { QameleoComponent } from "./modules/qameleo/qameleo.component";
import { SensorsComponent } from "./modules/sensors/sensors.component";
import { Error403Component } from "./shared/components/error403/error403.component";

import { Error404Component } from "./shared/components/error404/error404.component";
import { Error500Component } from "./shared/components/error500/error500.component";
import { SensorSingleComponent } from "./shared/components/sensor-single/sensor-single.component";
import { AuthGuard } from "./shared/guards/auth.guard";
import { UserGuard } from "./shared/guards/user.guard";

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: HomeComponent,
  },
  {
    path: "sensors",
    component: SensorsComponent,
    children: [
      {
        path: ":id",
        component: SensorSingleComponent
      }
    ]
  },
  {
    path: "qameleo",
    pathMatch: "full",
    component: QameleoComponent,
  },
  {
    path: "login",
    pathMatch: "full",
    component: LoginComponent,
  },
  {
    path: "management",
    canActivate: [UserGuard],
    pathMatch: "full",
    component: FormSensorComponent,
  },
  {
    path: "qameleo/:id",
    component: QameleoComponent,
  },
  {
    path: "error404",
    component: Error404Component,
  },
  {
    path: "error403",
    component: Error403Component,
  },
  {
    path: "error500",
    component: Error500Component,
  },
  {
    path: "**",
    redirectTo: "/error404",
  },
];

/* {preloadingStrategy: PreloadAllModules} | permet le chergent en plusieurs temps*/
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
