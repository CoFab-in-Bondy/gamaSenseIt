import { Routes } from '@angular/router';

import { MapsComponent } from '../../pages/maps/maps.component';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { QrcodeComponent } from '../../pages/qrcode/qrcode.component';

export const UserLayoutRoutes: Routes = [
    // { path: '', component: DashboardUserComponent },
    { path: 'userDashboard', component: DashboardComponent },
    { path: 'userMaps', component: MapsComponent },
    {path: 'qrcode1', component: QrcodeComponent}
    // { path: 'maps/:lat&:lng', component: MapsComponent },
    // { path: 'diagramme-user', component: DiagrammeComponent },
];
