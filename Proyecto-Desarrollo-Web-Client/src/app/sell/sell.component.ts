import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { LoginService } from '../services/auth/login.service';
import { SpacecraftService } from '../services/spacecraft.service';
import { Player } from '../model/player';
import { Spacecraft } from '../model/spacecraft';
import { Planet } from '../model/planet';
import { PlayerService } from '../services/player.service';
import { MarketService } from '../services/market.service';
import { SpacecraftModelService } from '../services/spacecraft-model.service';
import { SpacecraftModel } from '../model/spacecraft-model';
import { Market } from '../model/market';
import { Router } from '@angular/router';
import { Product } from '../model/product';
import { InventoryService } from '../services/inventory.service';
import { Inventory } from '../model/inventory';

@Component({
    selector: 'app-sell',
    templateUrl: './sell.component.html',
    styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit, OnDestroy {
    userLoginOn: boolean = false;
    userData?: Player;
    spaceCraftData: Spacecraft;
    spacecraftModelsData?: SpacecraftModel;
    planetData?: Planet;
    marketData: Market[] = [];
    invetoryData: Inventory[] = [];
    currentSpacecraftStorage: number;

    constructor(
        private router: Router,
        private loginService: LoginService,
        private playerService: PlayerService,
        private marketService: MarketService,
        private spaceCraftService: SpacecraftService,
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
            this.playerService.getPlayerSpacecraft(this.userData.id).subscribe((spacecraft: Spacecraft) => {
                console.log('Spacecraft data:', spacecraft);
                this.spaceCraftData = spacecraft;
                this.getInventoryData();
                this.getPlanetData();
            });
        }
    }
    getInventoryData() {
        if (this.spaceCraftData != null) {
            this.inventoryService.getInventoryBySpacecraftId(this.spaceCraftData.id).subscribe((inventory) => {
                console.log('Inventory data:', inventory);
                this.invetoryData = inventory;
                this.inventoryService.getTotalBySpacecraftId(this.spaceCraftData.id).subscribe((total) => {
                    this.currentSpacecraftStorage = total;
                    console.log('total de almacenamiento actual de la nave', this.currentSpacecraftStorage);
                })
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

    getPlanetData(): void {
        if (this.spaceCraftData != null) {
            this.spaceCraftService.getPlanetBySpacecraft(this.spaceCraftData.id).subscribe((planet: Planet) => {
                this.planetData = planet;
                this.getMarketData();
            });
        }
    }

    getMarketData(): void {
        if (this.planetData != null) {
            this.marketService.getMarketsByPlanetId(this.planetData.id)
                .subscribe((markets: Market[]) => {
                    this.marketData = markets;
                    this.getSpacecraftModelsData();
                });
        }
    }

    comprarProducto(product: Product, market: Market, spacecraft: Spacecraft, spacecraftModel: SpacecraftModel) {
        console.log('Datos de compra:', market);

        var existe = false;
        //si el producto existe en el inventario, simplemente se le agrega 1 a la quantity
        for (var i = 0; i < this.invetoryData.length; i++) {
            if (this.invetoryData[i].product.id == product.id) {
                existe = true;
            }
        }
        console.log('el producto existe en el inventario: ', existe);

        if (market.stock == 0) {
            alert('No hay stock disponible.');
            return;
        }

        if (spacecraft.credit < market.sellPrice) {
            alert('No hay suficiente crédito para realizar esta compra.');
            return;
        }

        //si al hacer la compra se pasa del almacenamiento, no se puede hacer la compra
        if (this.currentSpacecraftStorage + product.size > spacecraftModel.storage) {
            console.log('el storage maximo es', spacecraftModel.storage, ' y se estaría pasando con la compra: ', this.currentSpacecraftStorage + product.size)
            alert('No hay suficiente espacio en la nave para realizar esta compra.');
            return;
        }

        if (existe) {
            let toDo = 'add';
            this.inventoryService.updateInventoryQuantity(spacecraft.id, product.id, toDo).subscribe((inventory: Inventory) => {
                console.log('Inventario actualizado:', inventory);
                this.getInventoryData();
            });
        }
        else {
            alert('El producto no existía en el inventario, se ha actualizado correctamente')
            this.inventoryService.createProductInInventory(spacecraft.id, product.id).subscribe((inventory: Inventory) => {
                console.log('Inventario creado:', inventory);
                this.getInventoryData();
            });
        }

        let toDoCredits = 'substract';
        this.marketService.actualizarCreditos(spacecraft.id, market.buyPrice, toDoCredits).subscribe((spacecraft: Spacecraft) => {
            console.log('Créditos actualizados:', spacecraft.credit);
            this.spaceCraftData = spacecraft;
        });
        let toDo = 'sell';
        this.marketService.changeProductStock(market.id, toDo).subscribe((market: Market) => {
            console.log('market actualizado', market);
            this.spaceCraftData = spacecraft;
            this.getMarketData();
        });
    }
}