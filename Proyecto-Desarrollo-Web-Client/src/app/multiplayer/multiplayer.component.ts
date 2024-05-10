import { Component } from '@angular/core';
import { Spacecraft } from '../model/spacecraft';
import { Player } from '../model/player';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';
import { PlayerService } from '../services/player.service';
import { SpacecraftService } from '../services/spacecraft.service';
import { Planet } from '../model/planet';
import { Router } from '@angular/router';

@Component({
  selector: 'app-multiplayer',
  templateUrl: './multiplayer.component.html',
  styleUrl: './multiplayer.component.css'
})
export class MultiplayerComponent {

  userData?: Player;
  userId: number
  spaceCraftData?: Spacecraft;
  planet?: Planet;
  spacecrafts: Spacecraft[] = [];
  ID = "user-id";


  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private router: Router,private playerService: PlayerService, private spaceCraftService : SpacecraftService) { }

  ngOnInit(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userId = userId;
      this.getPlayerData();
    }
}
getPlayerData(): void {
  console.log(this.userId);
  if (this.userId != null && this.userId != 0) {
    this.playerService.getPlayerById(this.userId).subscribe((player: Player) => {
      this.userData = player;
      console.log('El jugador es:', this.userData);
      this.getSpaceCraftData();
    });
  }

}

ngOnDestroy(): void {
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
    console.log('Las naves son:', spacecrafts); //TODO no funciona porque no se mapea bien el Player de Spring con el Player de Angular
    this.spacecrafts = spacecrafts;
    //Por cada spacecraft, asignamos su lista de players
    this.spacecrafts.forEach(spacecraft => {
      this.loadPlayers(spacecraft);
    });
  });
  }
}
  loadPlayers(spacecraft: Spacecraft) {
    if (spacecraft) {
      this.playerService.getPlayersBySpacecraft(spacecraft.id).subscribe({
          next: (players: Player[]) => {
            spacecraft.players = players;
          },
          error: (error) => {
              console.log('Error fetching players:', error);
          }
      });
  }
  }
}
