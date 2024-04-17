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
    this.getPlayerData();
  }

  ngOnDestroy(): void {
    this.loginSubscription.unsubscribe();
    this.userDataSubscription.unsubscribe();
  }

  manageUserLoginStatus(): void {
    const userData = localStorage.getItem('currentUserData');
    if (userData) {
      const parsedData = JSON.parse(userData);
      this.loginService.currentUserData.next(parsedData);
      this.userLoginOn = true;
      this.userData = parsedData;
    }

    this.userDataSubscription = this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
        this.userLoginOn = !!userData;
      }
    });
  }

  getPlayerData(): void {
    if (this.userData) {
      this.loginSubscription.add(
        this.playerService.getPlayerById(this.userData.id).subscribe({
          next: (player: Player) => {
            console.log('El jugador es:', player.type);
            this.playerData = player;
          },
          error: (error) => {
            console.error('Error al obtener datos del jugador:', error);
          }
        })
      );
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
