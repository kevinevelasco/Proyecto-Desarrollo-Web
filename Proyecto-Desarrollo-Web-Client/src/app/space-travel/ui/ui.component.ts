import { StarService } from './../../services/star.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Star } from '../../model/star';
import { Player } from '../../model/player';
import { Planet } from '../../model/planet';
import { PlanetService } from '../../services/planet.service';

@Component({
  selector: 'app-ui',
  templateUrl: './ui.component.html',
  styleUrls: ['./ui.component.css']
})
export class UiComponent implements OnInit, OnDestroy {
  constructor() { }
  
  @Input() userData?: Player;
  @Input() currentStar: Star;
  @Input() starPlanets: Planet[] = [];


  ngOnInit(): void {
    console.log(this.userData);
    console.log(this.currentStar);
    console.log(this.starPlanets);
  }

  ngOnDestroy(): void {
  }
}
