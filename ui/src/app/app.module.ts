import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import {
  HttpClientModule,
  HttpClientXsrfModule,
  HTTP_INTERCEPTORS,
} from "@angular/common/http";
import { DatePipe, DecimalPipe } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { SharedModule } from "./shared/shared.module";
import { HomeComponent } from "./modules/home/home.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { JwtInterceptor } from "./shared/interceptors/jwt.interceptor";
import { XsrfInterceptor } from "./shared/interceptors/xsrf.interceptor";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AccessModule } from "./modules/accesses/accesses.module";
import { ViewModule } from "./modules/view/view.module";
import { SensorModule } from "./modules/sensors/sensors.module";
import { AuthModule } from "./modules/auth/auth.module";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    SharedModule.forRoot(),
    AccessModule.forRoot(),
    ViewModule.forRoot(),
    SensorModule.forRoot(),
    AuthModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
