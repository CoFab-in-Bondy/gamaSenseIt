import { ModuleWithProviders, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Error404Component } from "./components/error404/error404.component";
import { HeaderComponent } from "./components/header/header.component";
import { Error500Component } from "./components/error500/error500.component";
import { ApiService } from "./services/api.service";
import { SensorListComponent } from "./components/sensor-list/sensor-list.component";
import { SensorSingleComponent } from "./components/sensor-single/sensor-single.component";
import { AuthGuard } from "./guards/auth.guard";
import { RouterModule } from "@angular/router";
import { AuthService } from "./services/auth.service";
import { SensorService } from "./services/sensor.service";
import { SensorMetadataService } from "./services/sensorMetadata.service";
import { HumanService } from "./services/human.service";
import { DataTableComponent} from './components/data-table/data-table.component';
import { SensorMapComponent } from "./components/sensor-map/sensor-map.component";
import { DialogComponent } from "./components/dialog/dialog.component";

@NgModule({
  imports: [CommonModule, RouterModule],
  exports: [
    HeaderComponent,
    Error404Component,
    Error500Component,
    SensorListComponent,
    SensorSingleComponent,
    DataTableComponent,
    SensorMapComponent,
    DialogComponent
  ],
  declarations: [
    HeaderComponent,
    Error404Component,
    Error500Component,
    SensorListComponent,
    SensorSingleComponent,
    DataTableComponent,
    SensorMapComponent,
    DialogComponent
  ],
  providers: [ApiService],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [
        SensorMetadataService,
        SensorService,
        AuthGuard,
        AuthService,
        HumanService,
        ApiService
      ],
    };
  }
}
