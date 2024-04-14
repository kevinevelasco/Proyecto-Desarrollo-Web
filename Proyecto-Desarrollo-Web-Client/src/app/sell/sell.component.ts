import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { SpacecraftService } from '../services/spacecraft.service';
import { Player } from '../model/player';
import { Spacecraft } from '../model/spacecraft';
import { Planet } from '../model/planet';
import { PlayerService } from '../services/player.service';
import { MarketService } from '../services/market.service';
import { Market } from '../model/market';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit, OnDestroy {
    userLoginOn: boolean = false;
    userData?: Player;
    spaceCraftData?: Spacecraft;
    planetData?: Planet;
    marketData: Market[]= [];
  
    constructor(
        private router: Router,
        private loginService: LoginService,
        private playerService: PlayerService,
        private marketService: MarketService,
        private spaceCraftService: SpacecraftService
    ) {}

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
                this.getPlanetData();
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

    getMarketData(): void {
      if (this.planetData!= null) {
       this.marketService.getMarketsByPlanetId(this.planetData.id)
          .subscribe((markets: Market[]) => {
                  this.marketData = markets;
                  console.log("MarketData:", this.marketData);
            });
        }              
    }
    comprar() {
        console.log('Comprar acción iniciada');
        this.router.navigate(['/buy']);
    }

    vender() {
        console.log('Vender acción iniciada');
        this.router.navigate(['/sell']);
    }
}