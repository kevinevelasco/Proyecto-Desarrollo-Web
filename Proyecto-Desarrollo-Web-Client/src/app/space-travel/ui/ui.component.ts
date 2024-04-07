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
  constructor(private starService: StarService, private planetService: PlanetService) { }
  
  @Input() userData?: Player;
  currentStar: Star;
  starPlanets: Planet[] = [];

  ngOnInit(): void {
    console.log(this.userData);
    if(this.userData != null){
      this.starService.getStarDataBasedOnUser(this.userData.id).subscribe((star: Star) => {
        this.currentStar = star;
        console.log(this.currentStar); 

        this.planetService.getPlanetsByStarId(this.currentStar.id).subscribe(planets => {
          this.starPlanets = planets;
          console.log(this.starPlanets);
        });
      });
    }
  }

  ngOnDestroy(): void {
  }
}
