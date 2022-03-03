import { CommonModule } from "@angular/common";
import { ModuleWithProviders, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SharedModule } from "@guards/shared.module";
import { SensorFormComponent } from "./sensor-form/sensor-form.component";
import { SensorPageComponent } from './sensor-page/sensor-page.component';

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    SensorPageComponent
  ],
  declarations: [
    SensorFormComponent,
    SensorPageComponent
  ],
  providers: [],
})
export class SensorModule {
  static forRoot(): ModuleWithProviders<SensorModule> {
    return {
      ngModule: SensorModule,
      providers: [],
    };
  }
}
