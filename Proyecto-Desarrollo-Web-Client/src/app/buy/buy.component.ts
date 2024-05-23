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
import { Router } from '@angular/router';
import { Product } from '../model/product';
import { PageType } from '../shared/background/pageType';
import { AlertComponent } from '../shared/alert/alert.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrl: './buy.component.css',
})
export class BuyComponent {

  userData?: Player;
  userId: number
  spaceCraftData?: Spacecraft;
  inventoryData: Inventory[] = [];
  planetData: Planet;
  marketData: Market[] = [];
  productosNuevos: Product[] = [];
  pageType : PageType = {page : "market"};
  ID = "user-id";

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(
    public dialog: MatDialog,
    private playerService: PlayerService,
    private inventoryService: InventoryService,
    private marketService: MarketService,
    private spaceCraftService: SpacecraftService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userId = userId;
      this.getPlayerData();
    }else{
      this.router.navigate(['..//login']);
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
  }

  venderProductos(market: Market, producto: Product, sellPrice: number, planetId: number) {
    console.log(
      'se va a vender el producto ',
      producto.id,
      ', al planeta ',
      planetId,
      'con un precio de venta de: ',
      sellPrice
    );

    //primero decrementamos el stock del inventario de la nave
    let toDo = 'remove';
    if (this.spaceCraftData) {
      this.inventoryService
        .updateInventoryQuantity(this.spaceCraftData.id, producto.id, toDo)
        .subscribe((inventory) => {
          console.log('Inventario actualizado:', inventory);
          this.loadInventory();
        },
        (error) => {
          console.error('Error al actualizar el inventario:', error);
          if (error.status === 403) {
            this.openAlertDialog('Al parecer eres un PILOTO, por lo tanto no puedes comercializar, dile a tus colegas que compren por ti!');
          }
        });
      //ahora realizamos la lógica para agregarle créditos al spacecraft
      let toDoCredits = 'add';
      this.marketService
        .actualizarCreditos(this.spaceCraftData.id, sellPrice, toDoCredits)
        .subscribe((spacecraft) => {
          console.log('Créditos actualizados:', spacecraft.credit);
          this.spaceCraftData = spacecraft;
          this.spaceCraftService.updateSpaceCraftData(spacecraft);
        },
        (error) => {
          console.error('Error al actualizar los créditos:', error);
        });
    }

    //si el producto está en this.productosNuevos, al que se seleccione se crea una tupla nueva en Market de la BD, si no, se actualiza el stock en la tupla existente
    let productFound = false;
      if (this.productosNuevos.find(p => p === producto)) {
        productFound = true;
      }

    if (productFound) {
      console.log('producto nuevo: ' + producto.id);
      this.marketService.createNewMarket(planetId, producto.id, market).subscribe((market) => {
        console.log("tupla de market insertada: ", market);
      },
      (error) => {
        console.error('Error al crear la tupla de market:', error);
      });
    } else{
      console.log("producto existente: " + producto.id);
      let toDo = 'buy';
        this.marketService.changeProductStock(market.id, toDo).subscribe((market) => {
            console.log('market actualizado', market);
            this.getMarketData();
        },
        (error) => {
          console.error('Error al actualizar el stock del producto:', error);
        });
    }
  }

  getSpaceCraftData(): void {
    console.log(this.userData);
    if (this.userData != null) {
      this.playerService
        .getPlayerSpacecraft(this.userData.id)
        .subscribe((spacecraft: Spacecraft) => {
          console.log('La nave es:', spacecraft.name);
          this.spaceCraftData = spacecraft;
          this.getPlanetData();
        });
    }
  }

  getPlanetData(): void {
    if (this.spaceCraftData != null) {
      this.spaceCraftService
        .getPlanetBySpacecraft(this.spaceCraftData.id)
        .subscribe((planet: Planet) => {
          this.planetData = planet;
          this.getMarketData();
          console.log('Planeta:', this.planetData);
        });
    }
  }

  getMarketData(): void {
    if (this.planetData != null) {
      this.marketService
        .getMarketsByPlanetId(this.planetData.id)
        .subscribe((markets: Market[]) => {
          this.marketData = markets;
          console.log('MarketData:', this.marketData);
          this.loadInventory();
        });
    }
  }

  loadInventory(): void {
    if (this.spaceCraftData != null) {
      this.inventoryService
        .getInventoryBySpacecraftId(this.spaceCraftData.id)
        .subscribe(
          (inventory: Inventory[]) => {
            this.inventoryData = inventory;
            console.log('Inventario:', this.inventoryData);
            this.inventoryManipulation();
            console.log('marketDataFinal: ', this.marketData);
          },
          (error: any) => {
            if (error.status === 404) {
              this.inventoryData = [];
            } else {
              console.error('Error al cargar el inventario:', error);
            }
          }
        );
    }
  }

  inventoryManipulation() {
    // Verificar si los valores ya están almacenados en el localStorage
    let storedFactors = localStorage.getItem('factors');
    let factors;

    if (!storedFactors) {
      factors = {};
    } else {
      factors = JSON.parse(storedFactors);
    }

    for (let i = 0; i < this.inventoryData.length; i++) {
      let productFound = false;
      for (let j = 0; j < this.marketData.length; j++) {
        if (
          this.inventoryData[i].product.id === this.marketData[j].product.id
        ) {
          productFound = true;
          break; // No es necesario continuar buscando si ya se encontró el producto en marketData
        }
      }

      if (!productFound) {
        console.log('producto nuevo: ' + this.inventoryData[i].product.id);
        this.productosNuevos.push(this.inventoryData[i].product);
        if (this.planetData) {
          if (!factors.hasOwnProperty(this.inventoryData[i].product.id)) {
            // Si no hay factores almacenados para este producto, generamos nuevos valores aleatorios y los almacenamos
            factors[this.inventoryData[i].product.id] = {
              demandFactor: Math.floor(Math.random() * 300) + 1,
              supplyFactor: Math.floor(Math.random() * 600) + 1,
            };
            localStorage.setItem('factors', JSON.stringify(factors));
          }

          const productFactors = factors[this.inventoryData[i].product.id];
          // Crear un nuevo objeto Market
          const newMarket: Market = new Market(
            this.marketData.length + 1,
            this.planetData,
            this.inventoryData[i].product,
            0, // Stock inicial
            productFactors.demandFactor,
            productFactors.supplyFactor,
            productFactors.supplyFactor,
            productFactors.demandFactor
          );
          this.marketData.push(newMarket);
          console.log('productos nuevos: ', this.productosNuevos);
        }
      }
    }
  }

  volverViaje() {
    this.router.navigate(['/space-travel']);
    }
    sell() {
      this.router.navigate(['/buy']);
  }
  openAlertDialog(message: string) {
    this.dialog.open(AlertComponent, {
        data: {
            message: message
        },
        panelClass: '.dialog-container',
        width: '80%',
        height: '80%',
        disableClose: false
    });
}
}
