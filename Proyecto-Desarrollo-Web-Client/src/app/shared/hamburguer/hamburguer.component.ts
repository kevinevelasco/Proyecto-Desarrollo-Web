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
  userId: number;
  ID = "user-id";

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
  }

  ngOnDestroy(): void {
  }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  checkLoginStatus(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    if (userId != 0 && userId != null) {
      this.userLoginOn = true;
      this.userId = userId;
      this.getPlayerData();
    }
  }

  getPlayerData(): void {
    if (this.userId != null && this.userId != 0) {
      this.userDataSubscription = this.playerService.getPlayerById(this.userId).subscribe({
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
