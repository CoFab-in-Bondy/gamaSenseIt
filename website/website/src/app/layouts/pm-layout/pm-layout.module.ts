import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FlexLayoutModule } from '@angular/flex-layout';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ClipboardModule } from 'ngx-clipboard';
import { NgCircleProgressModule,CircleProgressOptions } from 'ng-circle-progress';

import { PMLayoutRoutes } from './pm-layout.routing';
import { ComponentsModule } from '../../components/components.module';
import { SensorVersionService } from '../../pages/sensor-version/sensor-version-service';

// import { ToastrModule } from 'ngx-toastr';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(PMLayoutRoutes),
    FormsModule,
    HttpClientModule,
    NgbModule,
    ClipboardModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    ComponentsModule
  ],
  declarations: [
  ],
  exports: [
  ],
  providers:[SensorVersionService]
})

export class PMLayoutComponent {}
