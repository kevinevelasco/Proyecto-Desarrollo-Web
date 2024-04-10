import { StarService } from './../../services/star.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Star } from '../../model/star';
import { Player } from '../../model/player';
import { Planet } from '../../model/planet';
import { PlanetService } from '../../services/planet.service';
import { EngineService } from '../engine/engine.service';

@Component({
  selector: 'app-ui',
  templateUrl: './ui.component.html',
  styleUrls: ['./ui.component.css'],
})
export class UiComponent implements OnInit, OnDestroy {
onStarClick(_t34: Star) {
throw new Error('Method not implemented.');
}
  constructor(private engineService: EngineService) {}

  @Input() userData?: Player;
  @Input() currentStar: Star;
  @Input() starPlanets: Planet[] = [];
  @Input() nearestStars : Star[] = [];
  booleanPlanet: boolean = false;
  currentPlanet: Planet;

  ngOnInit(): void {
    console.log(this.userData);
    console.log(this.currentStar);
    console.log(this.starPlanets);
    console.log(this.nearestStars);
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
  onTravelClick() {
    throw new Error('Method not implemented.');
  }
}
