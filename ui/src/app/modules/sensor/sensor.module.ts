import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DetailComponent } from "./pages/detail/detail.component";
import { SensorComponent } from "./pages/sensor/sensor.component";
import { SensorSingleComponent } from "./components/sensor-single/sensor-single.component";
import { SensorsItemComponent } from "./components/sensors-item/sensors-item.component";
import { SensorsListComponent } from "./components/sensors-list/sensors-list.component";
import { SensorRoutingModule } from "./sensor-routing.module";

@NgModule({
  imports: [CommonModule, SensorRoutingModule],
  exports: [DetailComponent, SensorComponent],
  declarations: [
    DetailComponent,
    SensorComponent,
    SensorSingleComponent,
    SensorsItemComponent,
    SensorsListComponent,
  ],
})
export class SensorModule {}
