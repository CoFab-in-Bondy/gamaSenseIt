import { CommonModule } from "@angular/common";
import { ModuleWithProviders, NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@guards/shared.module";
import { ViewListComponent } from "./view-list/view-list.component";
import { ViewMapComponent } from "./view-map/view-map.component";
import { ViewPageComponent } from "./view-page/view-page.component";
import { ViewSingleComponent } from "./view-single/view-single.component";

@NgModule({
  imports: [
    SharedModule.forRoot()
  ],
  exports: [
    ViewPageComponent
  ],
  declarations: [
    ViewListComponent,
    ViewMapComponent,
    ViewPageComponent,
    ViewSingleComponent
  ],
  providers: [],
})
export class ViewModule {
  static forRoot(): ModuleWithProviders<ViewModule> {
    return {
      ngModule: ViewModule,
      providers: [],
    };
  }
}
