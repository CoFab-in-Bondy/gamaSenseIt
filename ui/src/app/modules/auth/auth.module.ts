import { ModuleWithProviders, NgModule } from "@angular/core";
import { SharedModule } from "src/app/shared/shared.module";
import { LoginPageComponent } from "./login-page/login-page.component";

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    LoginPageComponent
  ],
  declarations: [
    LoginPageComponent
  ],
  providers: [],
})
export class AuthModule {
  static forRoot(): ModuleWithProviders<AuthModule> {
    return {
      ngModule: AuthModule,
      providers: [],
    };
  }
}
