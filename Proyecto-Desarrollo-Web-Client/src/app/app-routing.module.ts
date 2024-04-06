import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SpacecraftModelListComponent } from './spacecraftModel/spacecraft-model-list/spacecraft-model-list.component';
import { SpacecraftModelViewComponent } from './spacecraftModel/spacecraft-model-view/spacecraft-model-view.component';
import { SpacecraftModelEditComponent } from './spacecraftModel/spacecraft-model-edit/spacecraft-model-edit.component';
import { SpaceTravelInterfaceComponent } from './space-travel/space-travel-interface/space-travel-interface.component';

const routes: Routes = [
  { path: 'spacecraft-model/view/:id', component: SpacecraftModelViewComponent },
  { path: 'spacecraft-model/edit/:id', component: SpacecraftModelEditComponent },
  { path: 'spacecraft-model/list', component: SpacecraftModelListComponent },
  { path: 'space-travel', component: SpaceTravelInterfaceComponent },
  { path: '', pathMatch: 'full', redirectTo: 'person/list' },
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
