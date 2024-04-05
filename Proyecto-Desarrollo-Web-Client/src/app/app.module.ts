import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SpacecraftModelViewComponent } from './spacecraftModel/spacecraft-model-view/spacecraft-model-view.component';
import { SpacecraftModelListComponent } from './spacecraftModel/spacecraft-model-list/spacecraft-model-list.component';
import { SpacecraftModelEditComponent } from './spacecraftModel/spacecraft-model-edit/spacecraft-model-edit.component';
import { StarComponent } from './star/star.component';
import { StarListComponent } from './star/star-list/star-list.component';
import { StarEditComponent } from './star/star-edit/star-edit.component';
import { StarViewComponent } from './star/star-view/star-view.component';

@NgModule({
  declarations: [
    AppComponent,
    SpacecraftModelViewComponent,
    SpacecraftModelListComponent,
    SpacecraftModelEditComponent,
    StarComponent,
    StarListComponent,
    StarEditComponent,
    StarViewComponent
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
