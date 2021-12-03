import { ModuleWithProviders, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ParameterService } from "./services/parameter.service";
import { SensorService } from "./services/sensor.service";
import { Error404Component } from "./components/error404/error404.component";
import { HeaderComponent } from "./components/header/header.component";
import { Error500Component } from "./components/error500/error500.component";
import { ApiService } from "./services/api.service";
import { SensorItemComponent } from "./components/sensor-item/sensor-item.component";
import { SensorListComponent } from "./components/sensor-list/sensor-list.component";
import { SensorSingleComponent } from "./components/sensor-single/sensor-single.component";
import { AuthGuard } from "./guards/auth.guard";
import { RouterModule } from "@angular/router";
import { AuthService } from "./services/auth.service";

@NgModule({
  imports: [CommonModule, RouterModule],
  exports: [
    HeaderComponent,
    Error404Component,
    Error500Component,
    SensorItemComponent,
    SensorListComponent,
    SensorSingleComponent
  ],
  declarations: [
    HeaderComponent,
    Error404Component,
    Error500Component,
    SensorItemComponent,
    SensorListComponent,
    SensorSingleComponent,
  ],
  providers: [ApiService],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [ParameterService, SensorService, AuthGuard, AuthService],
    };
  }
}
