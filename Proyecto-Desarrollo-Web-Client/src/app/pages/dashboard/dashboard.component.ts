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
  userData?: Player;
  playerData?: Player;
  spaceCraftData?: Spacecraft;
  planetData?: Planet;
  pageType: PageType = { page: "home" };

  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;
  constructor(private loginService: LoginService, private playerService: PlayerService,
    private spaceCraftService: SpacecraftService, private router: Router, private timeService: TimeService) { }
  ngOnInit(): void {

    this.timeService.pauseCountdown();
    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
      this.loginService.currentUserData.next(JSON.parse(userData));
      this.userLoginOn = true;
    }
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
        if (this.userData) {
          this.userData.spacecraft = spacecraft;
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
        if (this.userData && this.userData.spacecraft) {
          this.userData.spacecraft.planet = planet;
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
        console.log('Tiempo desde el component:', this.spaceCraftData.totalTime);
        this.spaceCraftService.setSpacecraftTime(this.spaceCraftData).subscribe((spacecraft: Spacecraft) => {
          console.log('Tiempo actualizado:', spacecraft.totalTime);
          this.spaceCraftData = spacecraft;
          this.spaceCraftService.updateSpaceCraftData(spacecraft);
        });
      }
    }
  }

  logoutAndRedirect(): void {
    this.loginService.logout();
    this.userLoginOn = false;
    this.updateSpacecraftTime();
    this.router.navigate(['/login']);
    this.timeService.restartValues();
  }

  startGame(): void {
    this.router.navigate(['/sell']);
    this.timeService.resumeCountdown();
  }
}
