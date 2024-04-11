import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { SpacecraftService } from '../../services/spacecraft.service';
import { Player } from '../../model/player';
import { Subscription } from 'rxjs';
import { Spacecraft } from '../../model/spacecraft';
import { Planet } from '../../model/planet';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData?:Player;
  playerData?:Player;
  spaceCraftData?:Spacecraft;
  planetData?:Planet;

  private loginSubscription: Subscription; 
  private userDataSubscription: Subscription;
  constructor(private loginService: LoginService, private playerService: PlayerService,
     private spaceCraftService: SpacecraftService, private router: Router) { }
  ngOnInit(): void {

    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
      this.loginService.currentUserData.next(JSON.parse(userData));
      this.userLoginOn =true;
    }
    this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        //this.userLoginOn = userLoginOn;
      }
    }
    );
    this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    }
    );
    this.getPlayerData();
  }
  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
    if (this.userDataSubscription) {
      this.userDataSubscription.unsubscribe();
    }
  }
  getPlayerData(): void {
    console.log(this.userData);
    if (this.userData != null) {
      this.playerService.getPlayerById(this.userData.id).subscribe((player: Player) => {
        console.log('El jugador es:', player.type);
        this.playerData = player;
        this.getSpaceCraftData();
      });    
    }

  }
  getSpaceCraftData(): void {
    console.log(this.playerData);
    if (this.playerData != null) {
      this.playerService.getPlayerSpacecraft(this.playerData.id).subscribe((spacecraft: Spacecraft) => {
        console.log('La nave es:', spacecraft.name);
        this.spaceCraftData = spacecraft;
        this.getPlanetData();
      });
    }
  }
  getPlanetData(): void {
    console.log(this.spaceCraftData);
    if (this.spaceCraftData != null) {
      this.spaceCraftService.getPlanetBySpacecraft(this.spaceCraftData.id).subscribe((planet: Planet) => {
        console.log('El planeta es:', planet.name);
        this.planetData = planet;
      });
    }
  }

  logoutAndRedirect(): void {
    this.loginService.logout();
    this.userLoginOn = false;
    this.router.navigate(['/login']);
  }
}
