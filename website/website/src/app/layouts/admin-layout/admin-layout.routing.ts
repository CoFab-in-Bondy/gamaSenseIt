import { Routes } from '@angular/router';

import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { SensorVersionPage } from '../../pages/sensor-version/sensor-version.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { DiagrammeComponent } from '../../pages/diagramme/diagramme.component';
import { QrcodeComponent } from '../../pages/qrcode/qrcode.component';

export const AdminLayoutRoutes: Routes = [
    { path: 'dashboard',      component: DashboardComponent },
    { path: 'user-profile',   component: SensorVersionPage },
    { path: 'maps',           component: MapsComponent },
    { path: 'maps/:lat&:lng', component: MapsComponent },
    { path: 'diagramme',      component: DiagrammeComponent },
    { path: 'qrcode',         component: QrcodeComponent }
];
