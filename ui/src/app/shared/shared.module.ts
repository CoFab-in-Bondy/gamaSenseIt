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
import { JwtInterceptor } from "@interceptors/jwt.interceptor";
import { XsrfInterceptor } from "@interceptors/xsrf.interceptor";
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

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        MatSidenavModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatIconModule,
        MatListModule,
        MatButtonModule

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
    HttpClientXsrfModule,
    FontAwesomeModule,
  ],
  declarations: [
    HeaderComponent,
    Error403Component,
    Error404Component,
    Error500Component,
    DataTableComponent,
    DialogComponent,
    ButtonComponent,
    DateAgoPipe,
    SecurePipe,
    TributtonComponent,
    InputImageComponent,
    SafePipe,
    UnsafePipe,
  ],
  providers: [],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [
        // shared providers
        SensorMetadataService,
        SensorService,
        AccessService,
        AuthGuard,
        UserGuard,
        AdminGuard,
        AuthService,
        HumanService,
        ErrorService,
        StateService,
        // global providers
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: XsrfInterceptor, multi: true },
        { provide: ErrorHandler, useClass: ErrorInterceptor },
        DatePipe,
        SecurePipe,
        DateAgoPipe,
        DecimalPipe,
      ],
    };
  }
}
