import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { PlayerService } from '../services/player.service';
import { SpacecraftModel } from '../model/spacecraft-model';
import { SpacecraftModelService } from '../services/spacecraft-model.service';
import { SpacecraftService } from '../services/spacecraft.service';

@Component({
  selector: 'app-spacecraft-model',
  templateUrl: './spacecraft-model.component.html',
  styleUrls: ['./spacecraft-model.component.css']
})
export class SpacecraftModelComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData?: Player;
  spaceCraftData?: Spacecraft;
  spacecraftModelsData?: SpacecraftModel;
  playersInSpacecraft: Player[] = [];


  constructor(
    private loginService: LoginService, 
    private playerService: PlayerService, 
    private spacecraftService: SpacecraftService,  // Asegúrate de que este servicio esté disponible
    private spacecraftModelService: SpacecraftModelService
  ) { }

  ngOnInit(): void {
    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
        this.loginService.currentUserData.next(JSON.parse(userData));
        this.userLoginOn = true;
    }
    this.loginService.currentUserData.subscribe({
        next: (userData) => {
            this.userData = userData;
        }
    });
    this.getSpaceCraftData();
  }

  ngOnDestroy(): void {
  }

  getSpaceCraftData(): void {
    if (this.userData!=null) {
        this.playerService.getPlayerSpacecraft(this.userData.id).subscribe((spacecraft: Spacecraft) => {
            console.log('Spacecraft data:', spacecraft);
            this.spaceCraftData = spacecraft;
            this.getSpacecraftModelsData();
        });
    }
  }

  getSpacecraftModelsData(): void {
    if (this.spaceCraftData) {
      this.spacecraftModelService.getSpacecraftModelsBySpacecraftId(this.spaceCraftData.id)
        .subscribe((spacecraftModel: SpacecraftModel) => {
          this.spacecraftModelsData = spacecraftModel;
          console.log('Spacecraft model:', this.spacecraftModelsData);
        });
    }
  }
  
}
