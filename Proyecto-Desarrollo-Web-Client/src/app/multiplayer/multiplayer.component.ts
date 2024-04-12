import { Component } from '@angular/core';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { PlayerService } from '../services/player.service';
import { SpacecraftService } from '../services/spacecraft.service';
import { Planet } from '../model/planet';

@Component({
  selector: 'app-multiplayer',
  templateUrl: './multiplayer.component.html',
  styleUrl: './multiplayer.component.css'
})
export class MultiplayerComponent {

  userData?: Player;
  spaceCraftData?: Spacecraft;
  planet?: Planet;
  spacecrafts: Spacecraft[] = [];


  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService,private playerService: PlayerService, private spaceCraftService : SpacecraftService) { }

  ngOnInit(): void {
    const userData = localStorage.getItem('currentUserData');
    console.log(userData);
    if (userData) {
      this.loginService.currentUserData.next(JSON.parse(userData));
    }
    this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    });
    this.getSpaceCraftData();
}

ngOnDestroy(): void {
  if (this.loginSubscription) {
    this.loginSubscription.unsubscribe();
  }
  if (this.userDataSubscription) {
    this.userDataSubscription.unsubscribe();
  }
}
getSpaceCraftData(): void {
  console.log(this.userData);
  if (this.userData != null) {
    this.playerService.getPlayerSpacecraft(this.userData.id).subscribe((spacecraft: Spacecraft) => {
      console.log('La nave es:', spacecraft.name);
      this.spaceCraftData = spacecraft;
      this.loadPlanet();
    });
  }
}
loadPlanet(): void {
  if(this.spaceCraftData != null) {
  this.spaceCraftService.getPlanetBySpacecraft(this.spaceCraftData.id).subscribe((planet: Planet) => {
    console.log('El planeta es:', planet.name);
    this.planet = planet;
    this.loadSpacecrafts();
  });
}
}

loadSpacecrafts(): void {
  if(this.planet != null){
  this.spaceCraftService.getSpacecraftsByPlanet(this.planet.id).subscribe((spacecrafts: Spacecraft[]) => {
    console.log('Las naves son:', spacecrafts);
    this.spacecrafts = spacecrafts;
  });
  }
}
}
