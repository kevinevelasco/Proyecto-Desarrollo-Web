import { Component, OnDestroy, OnInit } from '@angular/core';
import { Player } from '../model/player';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { Planet } from '../model/planet';
import { Star } from '../model/star';
import { PlanetService } from '../services/planet.service';
import { StarService } from '../services/star.service';

@Component({
  selector: 'app-space-travel',
  templateUrl: './space-travel.component.html',
  styleUrls: ['./space-travel.component.css']
})
export class SpaceTravelComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;

  //recolectamos los datos que necesitemos para la interfaz para después pasarselas a los componentes hijos
  userData?: Player;
  currentStar: Star;
  starPlanets: Planet[] = [];
  nearestStars: Star[] = [];

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService, private starService: StarService, private planetService: PlanetService) {}

  ngOnInit(): void {
    this.loginSubscription = this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });

    this.userDataSubscription = this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    });
    this.getCurrentStarAndPlanets();
    window.onbeforeunload = () => this.ngOnDestroy();
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    if (this.userDataSubscription) {
      this.userDataSubscription.unsubscribe();
    }
  }

  getCurrentStarAndPlanets(): void {
    console.log(this.userData);
    if(this.userData != null){
      this.starService.getStarDataBasedOnUser(this.userData.id).subscribe((star: Star) => {
        this.currentStar = star;
        console.log('La estrella en la que estoy es:', this.currentStar); 

        this.planetService.getPlanetsByStarId(this.currentStar.id).subscribe(planets => {
          this.starPlanets = planets;
          console.log(this.starPlanets);
        });
        this.starService.getNearestStars(this.currentStar.id).subscribe(nearestStars => {
          this.nearestStars = nearestStars;
          console.log('las estrellas más cercanas son', this.nearestStars);
        });
      });
    }
}
}
