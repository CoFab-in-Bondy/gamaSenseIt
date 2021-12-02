import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { Error404Component } from "./shared/components/error404/error404.component";

const routes: Routes = [
  {
    path: "",
    redirectTo: "/sensor",
    pathMatch: "full",
  },
  {
    path: "sensor",
    loadChildren: () =>
      import("./modules/sensor/sensor.module").then((m) => m.SensorModule),
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
