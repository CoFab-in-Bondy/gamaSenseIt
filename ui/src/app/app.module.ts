import { ChangeDetectorRef, NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { DatePipe, DecimalPipe } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { SharedModule } from "./shared/shared.module";
import { QameleoComponent } from './modules/qameleo/qameleo.component';
import { HomeComponent } from './modules/home/home.component';
import { SensorsComponent } from "./modules/sensors/sensors.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './modules/login/login.component';
import { JwtInterceptor } from "./shared/interceptors/auth.interceptor";

@NgModule({
  declarations: [AppComponent, QameleoComponent, HomeComponent, SensorsComponent, LoginComponent],
  imports: [
    SharedModule.forRoot(),
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    DatePipe,
    DecimalPipe
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
