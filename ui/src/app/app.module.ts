import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { SharedModule } from "./shared/shared.module";
import { HomeComponent } from "./modules/home/home.component";
import { AccessModule } from "./modules/accesses/accesses.module";
import { ViewModule } from "./modules/view/view.module";
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
    AuthModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
