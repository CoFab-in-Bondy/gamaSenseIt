import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ClipboardModule } from 'ngx-clipboard';

import { AdminLayoutRoutes } from './admin-layout.routing';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { QrcodeComponent } from '../../pages/qrcode/qrcode.component';
import { UserProfileComponent } from '../../pages/user-profile/user-profile.component';
import { SensorVersionPage } from '../../pages/sensor-version/sensor-version.component';
import { ComponentsModule } from '../../components/components.module';
import { DiagrammeComponent } from '../../pages/diagramme/diagramme.component';
import { SensorVersionService } from '../../pages/sensor-version/sensor-version-service';
import { SearchPipe } from '../../pages/diagramme/search.pipe';

// import { ToastrModule } from 'ngx-toastr';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    HttpClientModule,
    NgbModule,
    ClipboardModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    ComponentsModule
  ],
  declarations: [

    DashboardComponent,
    UserProfileComponent,
//    MapsComponent,
    QrcodeComponent,
    SensorVersionPage,
   // DiagrammeComponent,
    SearchPipe
  ],
  exports: [

  ],
  providers: [SensorVersionService]
})

export class AdminLayoutModule { }
