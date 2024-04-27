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
  userData: Player;
  currentStar: Star;
  starPlanets: Planet[] = [];
  nearestStars: Star[] = [];

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;
  parsedUserData: any;

  constructor(private loginService: LoginService, private starService: StarService, private planetService: PlanetService) {}

  ngOnInit(): void {
    this.loginSubscription = this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });
  
    // Recuperar userData desde el almacenamiento local al inicio
    const storedUserData = localStorage.getItem('userData');
    console.log(storedUserData);
    if (storedUserData) {
      if(this.userData?.id == 0 || this.userData?.id == undefined || this.userData?.id == null){
      this.parsedUserData = JSON.parse(storedUserData);
      this.userData = this.parsedUserData;
      console.log(this.userData);
      if (this.userData) {
        this.loginService.setCurrentUserData(this.userData);
      }
      this.getCurrentStarAndPlanets();
    }
    }
  
    this.userDataSubscription = this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
        // Guardar userData en el almacenamiento local cuando cambie
        localStorage.setItem('userData', JSON.stringify(userData));
        
      }
    });
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
}
}
}

