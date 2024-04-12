import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { InventoryService } from '../services/inventory.service';
import { Inventory } from '../model/inventory';
import { LoginService } from '../services/auth/login.service';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { PlayerService } from '../services/player.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})


export class InventoryComponent {

  userData?: Player;
  spaceCraftData?: Spacecraft;
  inventoryData: Inventory[] = [];

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService,private playerService: PlayerService, private inventoryService: InventoryService) { }

  ngOnInit(): void {
    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
      this.loginService.currentUserData.next(JSON.parse(userData));
    }
    this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    });
    this.getSpaceCraftData();
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    if (this.userDataSubscription) {
      this.userDataSubscription.unsubscribe();
    }
  }
  getSpaceCraftData(): void {
    console.log(this.userData);
    if (this.userData != null) {
      this.playerService.getPlayerSpacecraft(this.userData.id).subscribe((spacecraft: Spacecraft) => {
        console.log('La nave es:', spacecraft.name);
        this.spaceCraftData = spacecraft;
        this.loadInventory();
      });
    }
  }
  loadInventory(): void {
    if(this.spaceCraftData != null) {
    this.inventoryService.getInventoryBySpacecraftId(this.spaceCraftData.id).subscribe(
      (inventory: Inventory[]) => {
      this.inventoryData = inventory;
      console.log('Inventario:', this.inventoryData);
      
    });
    }
  }

}
