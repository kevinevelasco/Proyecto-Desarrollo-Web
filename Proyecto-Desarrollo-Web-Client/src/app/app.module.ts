import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { AdminComponent } from './pages/admin/admin.component';
import { InventoryComponent } from './inventory/inventory.component';
import { MultiplayerComponent } from './multiplayer/multiplayer.component';
import { SellComponent } from './sell/sell.component';
import { BackgroundComponent } from './shared/background/background.component';
import { BuyComponent } from './buy/buy.component';
import { SpacecraftModelComponent } from './spacecraft-model/spacecraft-model.component';
import { SummaryBarComponent } from './shared/summary-bar/summary-bar.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import { HamburguerComponent } from './shared/hamburguer/hamburguer.component';
import { TravelsComponent } from './shared/travels/travels.component';
import { TimeExpiredComponent } from './time-expired/time-expired.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { ErrorInterceptorService } from './interceptors/error-interceptor.service';

@NgModule({
  declarations: [
    AppComponent,
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
    SpaceTravelComponent,
    LoginComponent,
    DashboardComponent,
    NavbarComponent,
    AdminComponent,
    InventoryComponent,
    MultiplayerComponent,
    AdminComponent,
    SellComponent,
    BackgroundComponent,
    BuyComponent,
    SpacecraftModelComponent,
    SummaryBarComponent,
    HamburguerComponent,
    TravelsComponent,
    TimeExpiredComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true },
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
