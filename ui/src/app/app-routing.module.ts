import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./modules/home/home.component";
import { Error403Component } from "@components/error403/error403.component";

import { Error404Component } from "@components/error404/error404.component";
import { Error500Component } from "@components/error500/error500.component";
import { UserGuard } from "@guards/user.guard";
import { SensorsSingleComponent } from "./modules/sensors/sensors-single/sensors-single.component";
import { LoginPageComponent } from "./modules/auth/login-page/login-page.component";
import { AccessListComponent } from "./modules/accesses/access-list/access-list.component";
import { AccessSingleComponent } from "./modules/accesses/access-single/access-single.component";
import {SensorsMapComponent} from "./modules/sensors/sensors-map/sensors-map.component";
import {SensorsListComponent} from "./modules/sensors/sensors-list/sensors-list.component";
import {AccessCreateComponent} from "./modules/accesses/access-create/access-create.component";

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: HomeComponent,
  },
  {
    path: "sensors",
    pathMatch: "full",
    redirectTo: "/sensors/map"
  },
  {
    path: "sensors/map",
    component: SensorsMapComponent
  },
  {
    path: "sensors/list",
    component: SensorsListComponent
  },
  {
    path: "sensors/:id",
    component: SensorsSingleComponent
  },
  {
    path: "qameleo",
    redirectTo: "sensors"
  },
  {
    path: "login",
    pathMatch: "full",
    component: LoginPageComponent,
  },
  {
    path: "accesses/create",
    pathMatch: "full",
    component: AccessCreateComponent,
  },
  {
    path: "accesses/:id",
    canActivate: [UserGuard],
    component: AccessSingleComponent
  },
  {
    path: "accesses",
    canActivate: [UserGuard],
    component: AccessListComponent
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
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule
  ],
})
export class AppRoutingModule {}
