import { PlayerService } from './../services/player.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Player } from '../model/player';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { Planet } from '../model/planet';
import { Star } from '../model/star';
import { PlanetService } from '../services/planet.service';
import { StarService } from '../services/star.service';
import { Spacecraft } from '../model/spacecraft';
import { Router } from '@angular/router';

@Component({
  selector: 'app-space-travel',
  templateUrl: './space-travel.component.html',
  styleUrls: ['./space-travel.component.css']
})
export class SpaceTravelComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;

  //recolectamos los datos que necesitemos para la interfaz para después pasarselas a los componentes hijos
  userData: Player;
  userId: number;
  currentStar: Star;
  starPlanets: Planet[] = [];
  nearestStars: Star[] = [];
  ID = "user-id";

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;
  parsedUserData: any;

  constructor(private loginService: LoginService, private starService: StarService, private planetService: PlanetService, private playerService: PlayerService, private router: Router) {}

  ngOnInit(): void {
    this.loginSubscription = this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });
  
    // Recuperar userData desde el almacenamiento local al inicio
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
      if (userId != 0 && userId != null) {
        this.userLoginOn = true;
        this.userId = userId;
        this.getPlayerData();
      }else{
        this.router.navigate(['..//login']);
      }
  
    this.userDataSubscription = this.loginService.currentUserData.subscribe({
      next: (userData) => {
        //todo this.userData = userData;
        // Guardar userData en el almacenamiento local cuando cambie
        localStorage.setItem('userData', JSON.stringify(userData));
        
      }
    });
    window.onbeforeunload = () => this.ngOnDestroy();
  }

  getPlayerData(): void {
    console.log(this.userId);
    if (this.userId != null && this.userId != 0) {
      this.playerService.getPlayerById(this.userId).subscribe((player: Player) => {
        this.userData = player;
        console.log('El jugador es:', this.userData);
        this.getCurrentStarAndPlanets();
      });
    }
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
          console.log("los planetas de la estrella en la que estoy son: ", this.starPlanets);
        });
        this.starService.getNearestStars(this.currentStar.id).subscribe(nearestStars => {
          this.nearestStars = nearestStars;
          //por cada una guardamos sus planetas
          this.nearestStars.forEach(nearestStar => {
            this.planetService.getPlanetsByStarId(nearestStar.id).subscribe(planets => {
              nearestStar.planets = planets;
            });
        });
        console.log("los estrellas más cercanas la estrella en la que estoy son: ", this.nearestStars);
    });
  });
  this.getSpaceCraftData();
}
}

getSpaceCraftData(): void {
  console.log(this.userData);
  if (this.userData != null) {
    this.playerService
      .getPlayerSpacecraft(this.userData.id)
      .subscribe((spacecraft: Spacecraft) => {
        console.log('La nave es:', spacecraft.name);
        this.userData.spacecraft = spacecraft;
      });
  }
}

}

