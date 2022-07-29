import { ModuleWithProviders, NgModule } from "@angular/core";
import { AccessListComponent } from "./access-list/access-list.component";
import { AccessSingleComponent } from './access-single/access-single.component';
import { AccessItemComponent } from './access-item/access-item.component';
import { SharedModule } from "src/app/shared/shared.module";
import { AccessCreateComponent } from './access-create/access-create.component';

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    AccessListComponent
  ],
  declarations: [
    AccessListComponent,
    AccessSingleComponent,
    AccessItemComponent,
    AccessCreateComponent
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
