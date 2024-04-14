import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { InventoryService } from '../services/inventory.service';
import { Inventory } from '../model/inventory';
import { LoginService } from '../services/auth/login.service';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { PlayerService } from '../services/player.service';
import { Planet } from '../model/planet';
import { Market } from '../model/market';
import { MarketService } from '../services/market.service';
import { SpacecraftService } from '../services/spacecraft.service';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrl: './buy.component.css'
})
export class BuyComponent {

  userData?: Player;
  spaceCraftData?: Spacecraft;
  inventoryData: Inventory[] = [];
  planetData?: Planet;
  marketData: Market[]= [];

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(
    private loginService: LoginService,
    private playerService: PlayerService,
    private inventoryService: InventoryService,
    private marketService: MarketService,
    private spaceCraftService: SpacecraftService) { }

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
  getMarketData(): void {
    if (this.planetData!= null) {
     this.marketService.getMarketsByPlanetId(this.planetData.id)
        .subscribe((markets: Market[]) => {
                this.marketData = markets;
                console.log("MarketData:", this.marketData);
          });
      }              
  }

  getPlanetData(): void {
    if (this.spaceCraftData!= null) {
        this.spaceCraftService.getPlanetBySpacecraft(this.spaceCraftData.id).subscribe((planet: Planet) => {
            this.planetData = planet;
            this.getMarketData();
        });
    }
}
  
  comprar() {
      console.log('Comprar acción iniciada');
  }

  vender() {
      console.log('Vender acción iniciada');
  }
}
