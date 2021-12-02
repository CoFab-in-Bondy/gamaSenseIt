import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SensorSingleComponent } from "./components/sensor-single/sensor-single.component";
import { SensorsItemComponent } from "./components/sensors-item/sensors-item.component";
import { SensorsListComponent } from "./components/sensors-list/sensors-list.component";
import { SensorRoutingModule } from "./sensor-routing.module";
import { DetailPage } from "./pages/detail.page";
import { SensorPage } from "./pages/sensor.page";

@NgModule({
  imports: [CommonModule, SensorRoutingModule],
  exports: [DetailPage, SensorPage],
  declarations: [
    DetailPage,
    SensorPage,
    SensorSingleComponent,
    SensorsItemComponent,
    SensorsListComponent,
  ],
})
export class SensorModule {}
