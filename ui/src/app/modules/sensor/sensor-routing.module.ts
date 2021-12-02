import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DetailComponent } from "./pages/detail/detail.component";
import { SensorComponent } from "./pages/sensor/sensor.component";

const routes: Routes = [
  {
    path: "",
    component: SensorComponent,
  },
  {
    path: ":id",
    component: DetailComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SensorRoutingModule {}
