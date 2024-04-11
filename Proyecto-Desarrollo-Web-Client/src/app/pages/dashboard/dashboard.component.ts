import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { Player } from '../../model/player';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData?:Player;
  playerData?:Player;
  private loginSubscription: Subscription; 
  private userDataSubscription: Subscription;
  constructor(private loginService: LoginService, private playerService: PlayerService) { }
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
        console.log('El jugador es:', player.spacecraft?.name);
        this.playerData = player;
      });    }

  }
}
