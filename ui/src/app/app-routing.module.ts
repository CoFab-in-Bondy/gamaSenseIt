import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./modules/home/home.component";
import { Error403Component } from "@components/error403/error403.component";

import { Error404Component } from "@components/error404/error404.component";
import { Error500Component } from "@components/error500/error500.component";
import { UserGuard } from "@guards/user.guard";
import { SensorsPageComponent } from "./modules/sensors/sensors-page/sensors-page.component";
import { SensorsSingleComponent } from "./modules/sensors/sensors-single/sensors-single.component";
import { LoginPageComponent } from "./modules/auth/login-page/login-page.component";
import { AccessPageComponent } from "./modules/accesses/access-page/access-page.component";
import { AccessSingleComponent } from "./modules/accesses/access-single/access-single.component";

const routes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: HomeComponent,
  },
  {
    path: "sensors",
    component: SensorsPageComponent
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
    path: "accesses",
    canActivate: [UserGuard],
    component: AccessPageComponent,
    children: [
      {
        path: ":id",
        component: AccessSingleComponent
      }
    ]
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
