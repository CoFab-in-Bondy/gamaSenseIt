import {LOCALE_ID, NgModule} from "@angular/core";
import { AppComponent } from "./app.component";
import { SharedModule } from "./shared/shared.module";
import { AccessModule } from "./modules/accesses/accesses.module";
import { SensorsModule } from "./modules/sensors/sensors.module";
import { AuthModule } from "./modules/auth/auth.module";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';

// the second parameter 'fr' is optional
registerLocaleData(localeFr, 'fr');

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    SharedModule.forRoot(),
    AccessModule.forRoot(),
    SensorsModule.forRoot(),
    AuthModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [ { provide: LOCALE_ID, useValue: "fr-FR" }],
  bootstrap: [AppComponent],
})
export class AppModule {}
