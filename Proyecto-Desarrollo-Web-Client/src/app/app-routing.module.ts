import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SpaceTravelInterfaceComponent } from './space-travel/space-travel-interface/space-travel-interface.component';
import { SpaceTravelComponent } from './space-travel/space-travel.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './auth/login/login.component';
import { AdminComponent } from './pages/admin/admin.component';
import { InventoryComponent } from './inventory/inventory.component';
import { MultiplayerComponent } from './multiplayer/multiplayer.component';
import { SellComponent } from './sell/sell.component';
import { BuyComponent } from './buy/buy.component';
import { SpacecraftModelComponent } from './spacecraft-model/spacecraft-model.component';

const routes: Routes = [
  { path: 'space-travelling', component: SpaceTravelInterfaceComponent },
  { path: 'space-travel', component: SpaceTravelComponent },
  { path: 'home', component: DashboardComponent},
  { path: 'login', component: LoginComponent},
  { path: 'admin', component: AdminComponent},
  { path: 'inventory', component: InventoryComponent},
  { path: 'multiplayer', component: MultiplayerComponent},
  { path: 'sell', component: SellComponent},
  { path: 'buy', component: BuyComponent},
  { path: 'spacecraft-model', component: SpacecraftModelComponent},
  { path: '', pathMatch: 'full', redirectTo: '/home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    {
      bindToComponentInputs: true, // Para poder usar @Input en rutas https://angular.io/guide/router
      onSameUrlNavigation: 'reload' // https://stackoverflow.com/a/52512361
    })], 
  exports: [RouterModule]
})
export class AppRoutingModule { }
