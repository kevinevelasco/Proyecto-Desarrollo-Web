import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { PlayerService } from '../services/player.service';
import { SpacecraftModel } from '../model/spacecraft-model';
import { SpacecraftModelService } from '../services/spacecraft-model.service';
import { SpacecraftService } from '../services/spacecraft.service';
import { Planet } from '../model/planet';
import { Inventory } from '../model/inventory';
import { InventoryService } from '../services/inventory.service';

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
  planet?: Planet;
  inventoryData: Inventory[] = [];
  currentSpacecraftStorage: number;


  constructor(
    private loginService: LoginService, 
    private playerService: PlayerService, 
    private spacecraftService: SpacecraftService,  // Asegúrate de que este servicio esté disponible
    private spacecraftModelService: SpacecraftModelService,
    private inventoryService: InventoryService
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
    if (this.userData != null) {
        this.playerService.getPlayerSpacecraft(this.userData.id).subscribe({
            next: (spacecraft: Spacecraft) => {
                console.log('Spacecraft data:', spacecraft);
                this.spaceCraftData = spacecraft;
                this.getSpacecraftModelsData();
                this.loadPlayers();  // Cargar jugadores
            },
            error: (error) => {
                console.log('Error fetching spacecraft data:', error);
            }
        });
    }
}

loadPlayers(): void {
  if (this.spaceCraftData) {
      this.playerService.getPlayersBySpacecraft(this.spaceCraftData.id).subscribe({
          next: (players: Player[]) => {
              this.playersInSpacecraft = players;
          },
          error: (error) => {
              console.log('Error fetching players:', error);
          }
      });
  }
}

  getSpacecraftModelsData(): void {
    if (this.spaceCraftData) {
      this.spacecraftModelService.getSpacecraftModelsBySpacecraftId(this.spaceCraftData.id)
        .subscribe((spacecraftModel: SpacecraftModel) => {
          this.spacecraftModelsData = spacecraftModel;
          console.log('Spacecraft model:', this.spacecraftModelsData);
          this.loadPlanet();
        });
    }
  }

  loadPlanet(): void {
    if(this.spaceCraftData != null) {
    this.spacecraftService.getPlanetBySpacecraft(this.spaceCraftData.id).subscribe((planet: Planet) => {
      console.log('El planeta es:', planet.name);
      this.planet = planet;
      this.getInventoryData();
    });
  }
  }
  
  getInventoryData() {
    if (this.spaceCraftData != null) {
      var spacecraftId = this.spaceCraftData.id;
        this.inventoryService.getInventoryBySpacecraftId(spacecraftId).subscribe((inventory) => {
            console.log('Inventory data:', inventory);
            this.inventoryData = inventory;
            this.inventoryService.getTotalBySpacecraftId(spacecraftId).subscribe((total) => {
                this.currentSpacecraftStorage = Math.round(total);
                console.log('total de almacenamiento actual de la nave', this.currentSpacecraftStorage);
            })
        });
    }
}
}
