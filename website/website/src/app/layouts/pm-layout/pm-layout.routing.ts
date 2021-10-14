import { Routes } from '@angular/router';

import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { SensorVersionPage } from '../../pages/sensor-version/sensor-version.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { DiagrammeComponent } from '../../pages/diagramme/diagramme.component';

export const PMLayoutRoutes: Routes = [
  { path: 'dashboard',    component: DashboardComponent },
  { path: 'user-profile', component: SensorVersionPage },
  { path: 'maps',         component: MapsComponent },
  { path: 'diagramme',    component: DiagrammeComponent }
];
