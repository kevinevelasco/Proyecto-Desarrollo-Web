import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { SpacecraftService } from '../../services/spacecraft.service';
import { Player } from '../../model/player';

@Component({
  selector: 'app-travels',
  templateUrl: './travels.component.html',
  styleUrl: './travels.component.css'  // Correcto: styleUrls y en array
})
export class TravelsComponent implements OnInit, OnDestroy {
  menuAbierto: boolean = false;
  userLoginOn: boolean = false;
  userData?: Player;
  playerData?: Player;
  userId: number;
  ID = "user-id";

  private loginSubscription: Subscription = new Subscription();
  private userDataSubscription: Subscription = new Subscription();

  constructor(
    private loginService: LoginService, 
    private playerService: PlayerService,
    private spaceCraftService: SpacecraftService,  
    private router: Router
  ) {}

  ngOnInit(): void {
    this.manageUserLoginStatus();
  }

  ngOnDestroy(): void {
    this.loginSubscription.unsubscribe();
    this.userDataSubscription.unsubscribe();
  }

  manageUserLoginStatus(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userId = userId;
      this.getPlayerData();
      this.userLoginOn = true;
    }
  }

  getPlayerData(): void {
    console.log(this.userId);
    if (this.userId != null && this.userId != 0) {
      this.playerService.getPlayerById(this.userId).subscribe((player: Player) => {
        this.userData = player;
        console.log('El jugador es:', this.userData);
      });
    }
  
  }

  logoutAndRedirect(): void {
    this.loginService.logout();
    this.userLoginOn = false;
    this.router.navigate(['/login']);
  }

  navegar() {
    this.router.navigate(['/space-travel']);
  }
}
