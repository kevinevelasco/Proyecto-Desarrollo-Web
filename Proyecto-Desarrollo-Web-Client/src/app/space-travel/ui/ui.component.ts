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

@Component({
  selector: 'app-ui',
  templateUrl: './ui.component.html',
  styleUrls: ['./ui.component.css'],
})
export class UiComponent implements OnInit, OnDestroy {
  constructor(
    private engineService: EngineService,
    private router: Router,
    private spacecraftService: SpacecraftService
  ) {}

  @Input() userData?: Player;
  @Input() currentStar: Star;
  @Input() starPlanets: Planet[] = [];
  @Input() nearestStars: Star[] = [];
  booleanPlanet: boolean = false;
  inPlanet: Planet;
  currentPlanet: Planet;

  ngOnInit(): void {
    console.log('user info', this.userData);
    console.log(this.currentStar);
    console.log(this.starPlanets);
    console.log(this.nearestStars);
    this.engineService.getClicInPlanetObservable().subscribe(planeta => {
      this.currentPlanet = planeta;
      console.log("el planeta clickeado fue", this.currentPlanet);
      this.booleanPlanet = !this.booleanPlanet;
    });
    if (this.userData && this.userData.spacecraft) {
    this.spacecraftService.getPlanetBySpacecraft(this.userData?.spacecraft.id).subscribe((planet: Planet) => {
      console.log('El planeta es:', planet.name);
      this.inPlanet = planet;
    });
    this.userData.spacecraft.planet = this.inPlanet;
    }
  }

  ngOnDestroy(): void {}

  onPlanetClick(planet: Planet) {
    //ajustamos el ui del engine para mostrar ese planeta en específico
    this.engineService.showPlanet(planet);
    this.booleanPlanet = true;
    this.currentPlanet = planet;
  }
  onCancelClick() {
    this.booleanPlanet = false;
    this.engineService.setInfo(false);
  }
  onTravelClick(currentPlanet: Planet) {
    if(this.userData != null && this.userData != undefined){
      this.onOtherStarPlanetClick(currentPlanet, this.userData, true);
    }
    
    }
  onOtherStarPlanetClick(planet: Planet, userData: Player, viajarAPlaneta : boolean) {
    //convertimos la respuesta en la interfaz SpacecraftPlanet
    const spacecraftPlanet: SpacecraftPlanet = {idPlanet: planet.id, idUser: userData.id};

    this.spacecraftService.setPlanet(spacecraftPlanet).pipe(
      //delay(2000)
    ).subscribe(data => {
      console.log(data);
      if(viajarAPlaneta){
        this.router.navigate(['/space-travelling']).then(() => {
          // Redirigir a '/space-travel' después de 2 segundos
          setTimeout(() => {
            this.router.navigate(['/sell']); 
          }, 2000); // retraso de 2 segundos
        });
      } else{
        this.router.navigate(['/space-travelling']).then(() => {
          // Redirigir a '/space-travel' después de 5 segundos
          setTimeout(() => {
            this.router.navigate(['/space-travel']); 
          }, 5000); // retraso de 5 segundos
        });
      }
    });
  }

  
}
