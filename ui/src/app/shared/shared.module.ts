import { ModuleWithProviders, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ParameterService } from "./services/parameter.service";
import { SensorService } from "./services/sensor.service";
import { Error404Component } from "./components/error404/error404.component";
import { HeaderComponent } from "./components/header/header.component";
import { Error500Component } from "./components/error500/error500.component";
import { AppRoutingModule } from "../app-routing.module";
import { ApiService } from "./services/api.service";

@NgModule({
  imports: [CommonModule, AppRoutingModule],
  exports: [HeaderComponent, Error404Component, Error500Component],
  declarations: [HeaderComponent, Error404Component, Error500Component],
  providers: [ApiService],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [ParameterService, SensorService],
    };
  }
}
