import { ErrorHandler, ModuleWithProviders, NgModule } from "@angular/core";
import { CommonModule, DatePipe, DecimalPipe } from "@angular/common";
import { Error404Component } from "@components/error404/error404.component";
import { HeaderComponent } from "@components/header/header.component";
import { Error500Component } from "@components/error500/error500.component";
import { AuthGuard } from "@guards/auth.guard";
import { RouterModule } from "@angular/router";
import { AuthService } from "@services/auth.service";
import { SensorService } from "@services/sensor.service";
import { SensorMetadataService } from "@services/sensorMetadata.service";
import { HumanService } from "@services/human.service";
import { DataTableComponent } from "@components/data-table/data-table.component";
import { DialogComponent } from "@components/dialog/dialog.component";
import { Error403Component } from "@components/error403/error403.component";
import { AdminGuard } from "@guards/admin.guard";
import { UserGuard } from "@guards/user.guard";
import { ButtonComponent } from "@components/button/button.component";
import { AccessService } from "@services/access.service";
import { StateService } from "@services/state.service";
import { DateAgoPipe } from "@pipes/date-ago.pipe";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import {
  HttpClientModule,
  HttpClientXsrfModule,
  HTTP_INTERCEPTORS,
} from "@angular/common/http";
import { AppRoutingModule } from "../app-routing.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { TokenInterceptor } from "@interceptors/token.interceptor";
import { TributtonComponent } from "@components/tributton/tributton.component";
import { SecurePipe } from "@pipes/secure.pipe";
import { InputImageComponent } from "@components/input-image/input-image.component";
import { SafePipe } from "@pipes/safe.pipe";
import { UnsafePipe } from "@pipes/unsafe.pipe";
import { ErrorInterceptor } from "@interceptors/error.interceptor";
import { ErrorService } from "@services/error.service";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from '@angular/material/list';
import {MatButtonModule} from "@angular/material/button";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DndDirective} from "@directives/dnd.directive";
import { MatOptionModule } from "@angular/material/core";
import {MatSelectModule} from '@angular/material/select';
import { NgxEchartsModule } from 'ngx-echarts';
import {ServerGuard} from "@guards/server.guard";
import {ServerService} from "@services/server.service";
import { FakeComponent } from '@components/fake/fake.component';
import { ResponsiveDirective } from '@directives/responsive.directive';
import {BinaryService} from "@services/binary.service";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatRadioModule} from "@angular/material/radio";


@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        MatSidenavModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatIconModule,
        MatListModule,
        MatButtonModule,
        MatTooltipModule,
        MatOptionModule,
        MatInputModule,
        MatSelectModule,
        NgxEchartsModule.forRoot({
          echarts: () => import('echarts')
        }),
        HttpClientXsrfModule.withOptions({
          cookieName: 'XSRF-TOKEN',
          headerName: 'X-XSRF-TOKEN',
        }),
    ],
  exports: [
    // shared exports
    HeaderComponent,
    Error403Component,
    Error404Component,
    Error500Component,
    DataTableComponent,
    DialogComponent,
    ButtonComponent,
    FakeComponent,
    DateAgoPipe,
    SecurePipe,
    TributtonComponent,
    InputImageComponent,
    // global exports
    CommonModule,
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatOptionModule,
    MatInputModule,
    MatSelectModule,
    NgxEchartsModule,
    ResponsiveDirective,
    DndDirective
  ],
  declarations: [
    HeaderComponent,
    Error403Component,
    Error404Component,
    Error500Component,
    DataTableComponent,
    DialogComponent,
    ButtonComponent,
    FakeComponent,
    DateAgoPipe,
    SecurePipe,
    TributtonComponent,
    InputImageComponent,
    SafePipe,
    UnsafePipe,
    DndDirective,
    ResponsiveDirective,
    FakeComponent,
  ],
  providers: [],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [
        // shared providers
        BinaryService,
        SensorMetadataService,
        SensorService,
        AccessService,
        AuthGuard,
        UserGuard,
        AdminGuard,
        ServerGuard,
        ServerService,
        AuthService,
        HumanService,
        ErrorService,
        StateService,
        // global providers
        { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
        { provide: ErrorHandler, useClass: ErrorInterceptor },
        { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'} },
        DatePipe,
        SecurePipe,
        DateAgoPipe,
        DecimalPipe,
      ],
    };
  }
}
