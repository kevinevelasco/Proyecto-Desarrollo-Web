import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SpacecraftModelViewComponent } from './spacecraftModel/spacecraft-model-view/spacecraft-model-view.component';
import { SpacecraftModelListComponent } from './spacecraftModel/spacecraft-model-list/spacecraft-model-list.component';
import { SpacecraftModelEditComponent } from './spacecraftModel/spacecraft-model-edit/spacecraft-model-edit.component';
import { StarListComponent } from './star/star-list/star-list.component';
import { StarEditComponent } from './star/star-edit/star-edit.component';
import { StarViewComponent } from './star/star-view/star-view.component';
import { SpaceTravelInterfaceComponent } from './space-travel/space-travel-interface/space-travel-interface.component';
import { UiComponent } from './space-travel/ui/ui.component';
import { UiInfobarBottomComponent } from './space-travel/ui/ui-infobar-bottom/ui-infobar-bottom.component';
import { UiInfobarTopComponent } from './space-travel/ui/ui-infobar-top/ui-infobar-top.component';
import { UiInfobarLeftComponent } from './space-travel/ui/ui-infobar-left/ui-infobar-left.component';
import { UiInfobarRightComponent } from './space-travel/ui/ui-infobar-right/ui-infobar-right.component';
import { EngineComponent } from './space-travel/engine/engine.component';
import { SpaceTravelComponent } from './space-travel/space-travel.component';

@NgModule({
  declarations: [
    AppComponent,
    SpacecraftModelViewComponent,
    SpacecraftModelListComponent,
    SpacecraftModelEditComponent,
    StarListComponent,
    StarEditComponent,
    StarViewComponent,
    SpaceTravelInterfaceComponent,
    UiComponent,
    UiInfobarBottomComponent,
    UiInfobarTopComponent,
    UiInfobarLeftComponent,
    UiInfobarRightComponent,
    EngineComponent,
    SpaceTravelComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
