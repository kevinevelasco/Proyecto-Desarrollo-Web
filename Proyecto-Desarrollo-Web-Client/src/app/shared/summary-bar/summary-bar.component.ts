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

@Component({
  selector: 'app-summary-bar',
  templateUrl: './summary-bar.component.html',
  styleUrl: './summary-bar.component.css'
})
export class SummaryBarComponent {

  time: number ;
  private timeSubscription: Subscription;

  constructor(public dialog: MatDialog, private loginService: LoginService, private playerService: PlayerService,private spaceCraftService: SpacecraftService, private timeService: TimeService ) { }


  spaceCraftData?:Spacecraft;
  private spaceCraftSubscription: Subscription;

  userData?:Player;


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
    }
    );
    this.timeSubscription = this.timeService.counter$.subscribe({
      next: (time) => {
        this.time = time;
      }
    });
    this.spaceCraftSubscription = this.spaceCraftService.spaceCraftData$.subscribe({
      next: (spaceCraft) => {
        if(spaceCraft){
        this.spaceCraftData = spaceCraft;
        }
      }
    });


    this.getSpaceCraftData();
  }

  ngOnDestroy(): void {
    if (this.timeSubscription) {
      this.timeSubscription.unsubscribe();
    }
    if (this.spaceCraftSubscription) {
      this.spaceCraftSubscription.unsubscribe();
    }
  }

  getSpaceCraftData(): void {
    if (this.userData != null) {
      this.playerService.getPlayerSpacecraft(this.userData.id).subscribe((spacecraft: Spacecraft) => {
        console.log('La nave es:', spacecraft.name);
        this.spaceCraftData = spacecraft;
        if (this.userData) {
          this.userData.spacecraft = spacecraft;
        }
        this.timeService.loadTime(spacecraft.totalTime);
      });
    }
  }
  
  
  

  openInventoryDialog() : void{
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

  openMultiplayerDialog() : void{
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
  openSpacecraftDialog() : void{
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
}
