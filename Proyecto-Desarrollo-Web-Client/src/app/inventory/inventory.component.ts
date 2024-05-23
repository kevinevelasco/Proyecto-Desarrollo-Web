import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { InventoryService } from '../services/inventory.service';
import { Inventory } from '../model/inventory';
import { LoginService } from '../services/auth/login.service';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { PlayerService } from '../services/player.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})


export class InventoryComponent {

  userData?: Player;
  userId: number;
  spaceCraftData?: Spacecraft;
  inventoryData: Inventory[] = [];
  ID = "user-id";

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService,private playerService: PlayerService, private inventoryService: InventoryService, public dialogRef : MatDialogRef<InventoryComponent>) { }

  ngOnInit(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userId = userId;
      this.getPlayerData();
    }
  }

  getPlayerData(): void {
    console.log(this.userId);
    if (this.userId != null && this.userId != 0) {
      this.playerService.getPlayerById(this.userId).subscribe((player: Player) => {
        this.userData = player;
        console.log('El jugador es:', this.userData);
        this.getSpaceCraftData();
      });
    }
  
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

  closeDialog() {
    this.dialogRef.close();
  }

}
