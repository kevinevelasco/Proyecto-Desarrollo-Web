import { Component, OnInit, OnDestroy } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { SpacecraftService } from '../../services/spacecraft.service';
import { Router } from '@angular/router';
import { Player } from '../../model/player';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-hamburguer',
  templateUrl: './hamburguer.component.html',
  styleUrls: ['./hamburguer.component.css']
})
export class HamburguerComponent implements OnInit, OnDestroy {
  menuAbierto: boolean = false;
  userLoginOn: boolean = false;
  userData?: Player;
  playerData?: Player;

  private loginSubscription: Subscription; 
  private userDataSubscription: Subscription;

  constructor(
    private loginService: LoginService, 
    private playerService: PlayerService,
    private spaceCraftService: SpacecraftService, 
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkLoginStatus();
    this.subscribeToUserData();
  }

  ngOnDestroy(): void {
    this.loginSubscription.unsubscribe();
    this.userDataSubscription.unsubscribe();
  }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  checkLoginStatus(): void {
    const userData = localStorage.getItem('currentUserData');
    if (userData) {
      this.userLoginOn = true;
      this.loginService.currentUserData.next(JSON.parse(userData));
    }
  }

  subscribeToUserData(): void {
    this.loginSubscription = this.loginService.currentUserData.subscribe({
      next: (data) => {
        this.userData = data;
        this.getPlayerData();
      }
    });
  }

  getPlayerData(): void {
    if (this.userData) {
      this.userDataSubscription = this.playerService.getPlayerById(this.userData.id).subscribe({
        next: (player: Player) => {
          this.playerData = player;
        },
        error: (err) => console.error('Error fetching player data:', err),
        complete: () => console.log('Player data fetched successfully')
      });
    }
  }

  logoutAndRedirect(): void {
    this.loginService.logout();
    this.userLoginOn = false;
    this.router.navigate(['/login']);
  }
}
