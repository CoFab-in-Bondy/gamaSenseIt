import { ModuleWithProviders, NgModule } from "@angular/core";
import { SharedModule } from "src/app/shared/shared.module";
import { SensorsListComponent } from "./sensors-list/sensors-list.component";
import { SensorsMapComponent } from "./sensors-map/sensors-map.component";
import { SensorsPageComponent } from "./sensors-page/sensors-page.component";
import { SensorsSingleComponent } from "./sensors-single/sensors-single.component";

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    SensorsPageComponent
  ],
  declarations: [
    SensorsListComponent,
    SensorsMapComponent,
    SensorsPageComponent,
    SensorsSingleComponent
  ],
  providers: [],
})
export class SensorsModule {
  static forRoot(): ModuleWithProviders<SensorsModule> {
    return {
      ngModule: SensorsModule,
      providers: [],
    };
  }
}
