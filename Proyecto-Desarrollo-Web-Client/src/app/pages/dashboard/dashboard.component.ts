import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { SpacecraftService } from '../../services/spacecraft.service';
import { Player } from '../../model/player';
import { Subscription } from 'rxjs';
import { Spacecraft } from '../../model/spacecraft';
import { Planet } from '../../model/planet';
import { Router } from '@angular/router';
import { PageType } from '../../shared/background/pageType';
import { TimeService } from '../../services/time.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})


export class DashboardComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData: number;
  userName: string;
  playerData?: Player;
  spaceCraftData?: Spacecraft;
  planetData?: Planet;
  pageType: PageType = { page: "home" };

  ID = "user-id";
  USERNAME = "user-name";

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;
  constructor(private loginService: LoginService, private playerService: PlayerService,
    private spaceCraftService: SpacecraftService, private router: Router, private timeService: TimeService) { 
    this.timeService.restartValues();
    }
  ngOnInit(): void {

    this.timeService.pauseCountdown();
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    this.userName = sessionStorage.getItem(this.USERNAME) || '';
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userLoginOn = true;
      this.userData = userId;
      this.getPlayerData();
    } else{
      this.router.navigate(['..//login']);
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
  getPlayerData(): void {
    console.log(this.userData);
    if (this.userData != null && this.userData != 0) {
      this.playerService.getPlayerById(this.userData).subscribe((player: Player) => {
        this.playerData = player;
        console.log('El jugador es:', this.playerData);
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
        if (this.userData) {
          this.playerData!.spacecraft = spacecraft;
        }
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
        if (this.playerData && this.playerData.spacecraft) {
          this.playerData.spacecraft.planet = planet;
        }
      });
    }
  }
  updateSpacecraftTime(): void {
    const time = localStorage.getItem('time');
    console.log('Tiempo en el localstorage:', time);
    if (time) {
      if (this.spaceCraftData) {
        this.spaceCraftData.totalTime = parseInt(time);
        this.spaceCraftService.setSpacecraftTime(this.spaceCraftData).subscribe((spacecraft: Spacecraft) => {
          this.spaceCraftData = spacecraft;
          this.spaceCraftService.updateSpaceCraftData(spacecraft);
        });
      }
    }
  }

  logoutAndRedirect(): void {
    this.updateSpacecraftTime();
    this.timeService.restartValues();
    this.loginService.logout();
    this.userLoginOn = false;
    this.router.navigate(['..//login']);
  }

  startGame(): void {
    this.router.navigate(['/sell']);
    this.timeService.resumeCountdown();
  }
}
