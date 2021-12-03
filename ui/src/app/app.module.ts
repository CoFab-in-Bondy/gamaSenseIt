import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule } from "@angular/common/http";
import { DatePipe } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { SharedModule } from "./shared/shared.module";
import { QameleoComponent } from './modules/qameleo/qameleo.component';
import { HomeComponent } from './modules/home/home.component';
import { SensorsComponent } from "./modules/sensors/sensors.component";

@NgModule({
  declarations: [AppComponent, QameleoComponent, HomeComponent, SensorsComponent],
  imports: [
    SharedModule.forRoot(),
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    DatePipe
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
