import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./modules/home/home.component";
import { LoginComponent } from "./modules/login/login.component";
import { QameleoComponent } from "./modules/qameleo/qameleo.component";
import { SensorsComponent } from "./modules/sensors/sensors.component";

import { Error404Component } from "./shared/components/error404/error404.component";
import { SensorSingleComponent } from "./shared/components/sensor-single/sensor-single.component";
import { AuthGuard } from "./shared/guards/auth.guard";

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: HomeComponent,
  },/*
  {
    path: "sensors",
    canActivate: [AuthGuard],
    loadChildren: () =>
      import("./modules/user/user.module").then((m) => m.UserModule),
  },
  {
    path: "qameleo",
    loadChildren: () =>
      import("./modules/qameleo/qameleo.module").then((m) => m.QameleoModule),
  },*/
  {
    path: "sensors",
    component: SensorsComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: ":id",
        component: SensorSingleComponent
      }
    ]
  },
  {
    path: "qameleo",
    component: QameleoComponent,
  },
  {
    path: "login",
    component: LoginComponent,
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
