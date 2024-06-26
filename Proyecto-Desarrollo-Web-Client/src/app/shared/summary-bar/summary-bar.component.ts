import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { InventoryComponent } from '../../inventory/inventory.component';
import { MultiplayerComponent } from '../../multiplayer/multiplayer.component';
import { SpacecraftModelComponent } from '../../spacecraft-model/spacecraft-model.component';
import { BehaviorSubject, Subscription } from 'rxjs';
import { TimeService } from '../../services/time.service';
import { Player } from '../../model/player';
import { Spacecraft } from '../../model/spacecraft';
import { LoginService } from '../../services/auth/login.service';
import { PlayerService } from '../../services/player.service';
import { SpacecraftService } from '../../services/spacecraft.service';
import { TimeExpiredComponent } from '../../time-expired/time-expired.component';

@Component({
  selector: 'app-summary-bar',
  templateUrl: './summary-bar.component.html',
  styleUrl: './summary-bar.component.css'
})
export class SummaryBarComponent {

  time: number;
  private timeSubscription: Subscription;

  constructor(public dialog: MatDialog, private loginService: LoginService, private playerService: PlayerService, private spaceCraftService: SpacecraftService, private timeService: TimeService) { }

  spaceCraftData?: Spacecraft;
  private spaceCraftSubscription: Subscription;
  userLoginOn: boolean = false;
  userData: number;
  ID = "user-id";
  playerData?: Player;

  ngOnInit(): void {

    // if (!sessionStorage.getItem('reloaded')) {
    //   sessionStorage.setItem('reloaded', 'true');
    //   window.location.reload();
    // } else {
    //   sessionStorage.removeItem('reloaded'); // Asegurarse de limpiar para la próxima visita
    // }
    // // TODO esto es lo que daña space-travel

    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userLoginOn = true;
      this.userData = userId;
    }
    this.getPlayerData();
    // this.timeSubscription = this.timeService.counter$.subscribe({
    //   next: (time) => {
    //     this.time = time;
    //     if (this.time == 0) {
    //       this.TimeExpired();
    //     }
    //   } //TODO falta hacer esto bien
    // });
    this.spaceCraftSubscription = this.spaceCraftService.spaceCraftData$.subscribe({
      next: (spaceCraft) => {
        if (spaceCraft) {
          this.spaceCraftData = spaceCraft;
        }
      }
    });
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

  ngOnDestroy(): void {
    if (this.timeSubscription) {
      this.timeSubscription.unsubscribe();
    }
    if (this.spaceCraftSubscription) {
      this.spaceCraftSubscription.unsubscribe();
    }
    //hide dialog
    this.dialog.closeAll();
  }

  getSpaceCraftData(): void {
    if (this.playerData != null) {
      this.playerService.getPlayerSpacecraft(this.playerData.id).subscribe((spacecraft: Spacecraft) => {
        console.log('La nave es:', spacecraft.name);
        this.spaceCraftData = spacecraft;
        if (this.playerData) {
          this.playerData.spacecraft = spacecraft;
        }
        this.timeService.loadTime(spacecraft.totalTime);
      });
    }
  }

  openInventoryDialog(): void {
    const dialogRef = this.dialog.open(InventoryComponent, {
      panelClass: '.dialog-container',
      width: '80%',
      height: '80%',
      disableClose: false
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openMultiplayerDialog(): void {
    const dialogRef = this.dialog.open(MultiplayerComponent, {
      panelClass: '.dialog-container',
      width: '80%',
      height: '80%',
      disableClose: false
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
  openSpacecraftDialog(): void {
    const dialogRef = this.dialog.open(SpacecraftModelComponent, {
      panelClass: '.dialog-container',
      width: '80%',
      height: '80%',
      disableClose: false
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  TimeExpired(): void {
    if (this.spaceCraftData) {
      this.spaceCraftData.totalTime = 1;
      console.log('Tiempo desde el component:', this.spaceCraftData.totalTime);
      this.timeService.updateTimeBySpaceCraft(this.spaceCraftData)
        .subscribe((spacecraft: Spacecraft) => {
          console.log('Tiempo actualizado:', spacecraft.totalTime);
          this.spaceCraftData = spacecraft;
          this.spaceCraftService.updateSpaceCraftData(spacecraft);
        });
    }

    if (this.dialog.openDialogs.length == 0) {
      const dialogRef = this.dialog.open(TimeExpiredComponent, {
        panelClass: '.dialog-container',
        width: '80%',
        height: '80%',
        disableClose: true
      });
    }
    this.timeService.restartValues();
    this.timeSubscription.unsubscribe();

  }
  
}
