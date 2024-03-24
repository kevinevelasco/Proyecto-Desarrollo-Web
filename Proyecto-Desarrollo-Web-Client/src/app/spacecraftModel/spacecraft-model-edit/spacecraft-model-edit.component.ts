import { Component, Input } from '@angular/core';
import { SpacecraftService } from '../../shared/spacecraft.service';
import { Router } from '@angular/router';
import { SpacecraftModel } from '../../model/spacecraft-model';

@Component({
  selector: 'app-spacecraft-model-edit',
  templateUrl: './spacecraft-model-edit.component.html',
  styleUrl: './spacecraft-model-edit.component.css'
})
export class SpacecraftModelEditComponent {
  constructor(
    private spacecraftService: SpacecraftService,
    private router: Router,
  ){}

  @Input()
  set id(id: number) {
    console.log("id", id)
    this.spacecraftService.getSpacecraftModel(id).subscribe(spacecraftModel => { this.spacecraftModel = spacecraftModel } );
  }

  spacecraftModel: SpacecraftModel = new SpacecraftModel(0, "", 0, 0);

  saveSpacecraftModel() {
    this.spacecraftService.saveSpacecraftModel(this.spacecraftModel).subscribe(spacecraftModel => {console.log("spacecraftModel", spacecraftModel)});
    this.router.navigate(['/spacecraft-model/list']);
    }
}
