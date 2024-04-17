import { Component } from '@angular/core';
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
export class HamburguerComponent {
  menuAbierto: boolean = false;
  userLoginOn: boolean = false;
  userData?:Player;
  playerData?:Player;

  private loginSubscription: Subscription; 
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService, private playerService: PlayerService,
    private spaceCraftService: SpacecraftService, private router: Router) { }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  ngOnInit(): void {

    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
      this.loginService.currentUserData.next(JSON.parse(userData));
      this.userLoginOn =true;
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
      });    
    }
  }

  logoutAndRedirect(): void {
    this.loginService.logout();
    this.userLoginOn = false;
    this.router.navigate(['/login']);
  }
  
}
