import { StarService } from './../../services/star.service';
import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Star } from '../../model/star';
import { Player } from '../../model/player';
import { Planet } from '../../model/planet';
import { PlanetService } from '../../services/planet.service';
import { EngineService } from '../engine/engine.service';
import { Router } from '@angular/router';
import { SpacecraftService } from '../../services/spacecraft.service';
import { SpacecraftPlanet } from './spacecraftPlanet';
import { delay } from 'rxjs/operators';
import { MarketService } from '../../services/market.service';
import { Market } from '../../model/market';
import { SpacecraftModelService } from '../../services/spacecraft-model.service';
import { SpacecraftModel } from '../../model/spacecraft-model';
import { TimeService } from '../../services/time.service';

@Component({
  selector: 'app-ui',
  templateUrl: './ui.component.html',
  styleUrls: ['./ui.component.css'],
})
export class UiComponent implements OnInit, OnDestroy {
  constructor(
    private engineService: EngineService,
    private router: Router,
    private spacecraftService: SpacecraftService,
    private marketService: MarketService,
    private starService: StarService,
    private spacecraftModelService: SpacecraftModelService,
    private timeService: TimeService
  ) {}

  @Input() userData?: Player;
  @Input() currentStar: Star;
  @Input() starPlanets: Planet[] = [];
  @Input() nearestStars: Star[] = [];
  booleanPlanet: boolean = false;
  inPlanet: Planet;
  currentPlanet: Planet;
  currentSpacecraftModel: SpacecraftModel;
  deployPlanetList: boolean = false;
  starClicked: Star;
  marketData: Market[]= [];
  distance: number;

  ngOnInit(): void {
    console.log('user info', this.userData);
    console.log(this.currentStar);
    console.log(this.starPlanets);
    console.log(this.nearestStars);
    this.engineService.getClicInPlanetObservable().subscribe((planeta) => {
      this.currentPlanet = planeta;
      console.log('el planeta clickeado fue', this.currentPlanet);
      this.booleanPlanet = !this.booleanPlanet;
    });
    if (this.userData && this.userData.spacecraft) {
      this.spacecraftService
        .getPlanetBySpacecraft(this.userData?.spacecraft.id)
        .subscribe((planet: Planet) => {
          console.log('El planeta es:', planet.name);
          this.inPlanet = planet;
        });

        this.spacecraftModelService.getSpacecraftModelsBySpacecraftId(this.userData.spacecraft.id).subscribe(
          (spacecraftModel) => {
            console.log('SpacecraftModel:', spacecraftModel);
            this.currentSpacecraftModel = spacecraftModel;
          });
      this.userData.spacecraft.planet = this.inPlanet;7
      
      console.log('Spacecraft:', this.userData);
    }
  }

  ngOnDestroy(): void {}

  onPlanetClick(planet: Planet) {
    //ajustamos el ui del engine para mostrar ese planeta en específico
    this.engineService.showPlanet(planet);
    this.booleanPlanet = true;
    this.currentPlanet = planet;
    this.marketService.getMarketsByPlanetId(this.currentPlanet.id)
    .subscribe((markets: Market[]) => {
            this.marketData = markets;
            // this.currentPlanet = this.marketData.
            console.log("MarketData:", this.marketData);
      });
  }
  onCancelClick() {
    this.booleanPlanet = false;
    this.engineService.setInfo(false);
  }
  onTravelClick(currentPlanet: Planet) {
    if (this.userData != null && this.userData != undefined) {
      this.onOtherStarPlanetClick(currentPlanet, this.userData, true);
    }
  }
  onOtherStarPlanetClick(
    planet: Planet,
    userData: Player,
    viajarAPlaneta: boolean
  ) {
    //convertimos la respuesta en la interfaz SpacecraftPlanet
    const spacecraftPlanet: SpacecraftPlanet = {
      idPlanet: planet.id,
      idUser: userData.id,
    };

    this.starService.getDistanceBetweenStars(planet.star.id, this.currentStar.id).subscribe((distance) => {
      console.log('Distancia entre estrellas:', distance);
      this.distance = distance;
      var tiempo = Math.round(distance / this.currentSpacecraftModel.maxSpeed);
      console.log('Tiempo gastado:', tiempo);
      this.timeService.decrementTimeBy(tiempo);
    });
    this.spacecraftService
      .setPlanet(spacecraftPlanet)
      .pipe
      //delay(2000)
      ()
      .subscribe((data) => {
        console.log(data);
        if (viajarAPlaneta) {
          this.router.navigate(['/space-travelling']).then(() => {
            // Redirigir a '/space-travel' después de 2 segundos
            setTimeout(() => {
              this.router.navigate(['/sell']);
            }, 1000); // retraso de 2 segundos
          });
        } else {
          this.router.navigate(['/space-travelling']).then(() => {
            // Redirigir a '/space-travel' después de 5 segundos
            setTimeout(() => {
              this.router.navigate(['/sell']);
            }, 5000); // retraso de 5 segundos
          });
        }
      });
  }

  onStarClick(star: Star) {
    console.log('star clickeada', star);
    if (this.starClicked === star) {
      this.deployPlanetList = !this.deployPlanetList; // Toggle para cerrar o abrir la lista de planetas
    } else {
      this.starClicked = star; // Almacena la estrella clicada
      this.deployPlanetList = true; // Abre la lista de planetas para la nueva estrella
    }
  }
}
