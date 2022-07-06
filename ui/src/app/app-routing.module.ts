import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
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
import {SensorsImageComponent} from "./modules/sensors/sensor-image/sensors-image.component";
import {SensorsGraphComponent} from "./modules/sensors/sensors-graph/sensors-graph.component";
import {ServerGuard} from "@guards/server.guard";
import {FakeComponent} from "@components/fake/fake.component";

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    canActivate: [ServerGuard],
    component: FakeComponent
  },
  {
    path: "sensors",
    pathMatch: "full",
    redirectTo: "/sensors/map"
  },
  {
    path: "sensors/map",
    pathMatch: "full",
    component: SensorsMapComponent
  },
  {
    path: "sensors/list",
    pathMatch: "full",
    component: SensorsListComponent
  },{
    path: "sensors/:id/graph",
    pathMatch: "full",
    component: SensorsGraphComponent
  }, {
    path: "sensors/:id/photo",
    pathMatch: "full",
    component: SensorsImageComponent
  },
  {
    path: "sensors/:id",
    pathMatch: "full",
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
    pathMatch: "full",
    canActivate: [UserGuard],
    component: AccessSingleComponent
  },
  {
    path: "accesses",
    pathMatch: "full",
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
    component: Error404Component,
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule
  ],
})
export class AppRoutingModule {}
