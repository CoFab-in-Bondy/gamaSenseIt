import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DetailPage } from "./pages/detail.page";
import { SensorPage } from "./pages/sensor.page";

const routes: Routes = [
  {
    path: "",
    component: SensorPage,
  },
  {
    path: ":id",
    component: DetailPage,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SensorRoutingModule {}
