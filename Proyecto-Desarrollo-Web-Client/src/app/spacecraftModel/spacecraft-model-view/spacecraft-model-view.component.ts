import { Component, Input } from '@angular/core';
import { SpacecraftService } from '../../shared/spacecraft.service';
import { SpacecraftModel } from '../../model/spacecraft-model';

@Component({
  selector: 'app-spacecraft-model-view',
  templateUrl: './spacecraft-model-view.component.html',
  styleUrl: './spacecraft-model-view.component.css'
})
export class SpacecraftModelViewComponent {
  constructor(private spacecraftService: SpacecraftService) {}

  @Input()
  set id(id: number) {
    console.log("id", id)
    this.spacecraftService.getSpacecraftModel(id).subscribe(spacecraftModel => { this.spacecraftModel = spacecraftModel } );
  }

  spacecraftModel: SpacecraftModel = new SpacecraftModel(0, "", 0, 0);
}
