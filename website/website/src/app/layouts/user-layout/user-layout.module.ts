import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ClipboardModule } from 'ngx-clipboard';

import { UserLayoutRoutes } from './user-layout.routing';
import { ComponentsModule } from '../../components/components.module';
import { SensorVersionService } from '../../pages/sensor-version/sensor-version-service';
import { AdminLayoutModule } from '../admin-layout/admin-layout.module';

// import { ToastrModule } from 'ngx-toastr';

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(UserLayoutRoutes),
        HttpClientModule,
        NgbModule,
        ClipboardModule,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        ComponentsModule,
        AdminLayoutModule
    ],
    declarations: [

        // SearchPipe
    ],
    exports: [

    ],
    providers: [SensorVersionService]
})

export class UserLayoutModule { }