import { Component } from '@angular/core';
import { SpacecraftModel } from '../../model/spacecraft-model';
import { SpacecraftService } from '../../shared/spacecraft.service';

@Component({
  selector: 'app-spacecraft-model-list',
  templateUrl: './spacecraft-model-list.component.html',
  styleUrl: './spacecraft-model-list.component.css'
})
export class SpacecraftModelListComponent {
spacecraftModels: SpacecraftModel[] = [];

  constructor(
    private spacecraftService: SpacecraftService
  ){}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.spacecraftService.getSpacecraftModels().subscribe(
      (spacecraftModels) => {
        this.spacecraftModels = spacecraftModels;
      }
    );
  }
}
