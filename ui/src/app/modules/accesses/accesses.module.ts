import { ModuleWithProviders, NgModule } from "@angular/core";
import { AccessListComponent } from "./access-list/access-list.component";
import { AccessPageComponent } from "./access-page/access-page.component";
import { AccessSingleComponent } from './access-single/access-single.component';
import { AccessItemComponent } from './access-item/access-item.component';
import { SharedModule } from "src/app/shared/shared.module";

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    AccessPageComponent
  ],
  declarations: [
    AccessPageComponent,
    AccessListComponent,
    AccessSingleComponent,
    AccessItemComponent
  ],
  providers: [],
})
export class AccessModule {
  static forRoot(): ModuleWithProviders<AccessModule> {
    return {
      ngModule: AccessModule,
      providers: [],
    };
  }
}
