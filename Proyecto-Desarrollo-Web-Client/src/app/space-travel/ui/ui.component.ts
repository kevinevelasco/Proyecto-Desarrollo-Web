import { StarService } from './../../services/star.service';
import { Component, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { Star } from '../../model/star';
import { Player } from '../../model/player';
import { Planet } from '../../model/planet';
import { PlanetService } from '../../services/planet.service';
import { EngineService } from '../engine/engine.service';
import { Router } from '@angular/router';
import { SpacecraftService } from '../../services/spacecraft.service';
import { SpacecraftPlanet } from './spacecraftPlanet';
import { AlertComponent } from '../../shared/alert/alert.component';
import { MarketService } from '../../services/market.service';
import { Market } from '../../model/market';
import { SpacecraftModelService } from '../../services/spacecraft-model.service';
import { SpacecraftModel } from '../../model/spacecraft-model';
import { TimeService } from '../../services/time.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-ui',
  templateUrl: './ui.component.html',
  styleUrls: ['./ui.component.css'],
})
export class UiComponent implements OnInit, OnDestroy, OnChanges {
  constructor(
    private engineService: EngineService,
    private router: Router,
    private spacecraftService: SpacecraftService,
    private marketService: MarketService,
    private starService: StarService,
    private spacecraftModelService: SpacecraftModelService,
    private timeService: TimeService,
    private planetService: PlanetService,
    public dialog: MatDialog,
  ) {}

  @Input() userData: Player;
  @Input() currentStar: Star;
  @Input() starPlanets: Planet[] = [];
  @Input() nearestStars: Star[] = [];
  booleanPlanet: boolean = false;
  inPlanet: Planet;
  currentPlanet: Planet;
  currentSpacecraftModel: SpacecraftModel;
  deployPlanetList: boolean = false;
  starClicked: Star;
  marketData: Market[]= [];
  distance: number;


  ngOnChanges(changes: SimpleChanges): void {
    console.log('changes', changes);
  
    // Verificar si hubo cambios en currentStar y asignarlo si es así
    if ('currentStar' in changes) {
      this.currentStar = changes["currentStar"].currentValue;
      console.log('currentStar', this.currentStar);
    }
  
    // Verificar si hubo cambios en starPlanets y asignarlo si es así
    if ('starPlanets' in changes) {
      this.starPlanets = changes["starPlanets"].currentValue;
    }
  
    // Verificar si hubo cambios en nearestStars y asignarlo si es así
    if ('nearestStars' in changes) {
      this.nearestStars = changes["nearestStars"].currentValue;
    }

    if(this.currentStar != undefined && this.starPlanets.length > 0 && this.nearestStars.length > 0){
      console.log("aqui fue")
      this.engineService.getClicInPlanetObservable().subscribe((planeta) => {
        this.currentPlanet = planeta;
        console.log('el planeta clickeado fue', this.currentPlanet);
        this.booleanPlanet = !this.booleanPlanet;
      });
        console.log("Info")
        this.spacecraftService
          .getPlanetBySpacecraft(this.userData.spacecraft!.id)
          .subscribe((planet: Planet) => {
            console.log('El planeta donde estoy es:', planet.name);
            this.inPlanet = planet;
          });
  
          this.spacecraftModelService.getSpacecraftModelsBySpacecraftId(this.userData.spacecraft!.id).subscribe(
            (spacecraftModel) => {
              console.log('SpacecraftModel:', spacecraftModel);
              this.currentSpacecraftModel = spacecraftModel;
            });
        this.userData.spacecraft!.planet = this.inPlanet;
        
        console.log('Spacecraft:', this.userData);
    }
  }
  

  ngOnInit(): void {
  }

  ngOnDestroy(): void {}

  onPlanetClick(planet: Planet) {
    //ajustamos el ui del engine para mostrar ese planeta en específico
    this.engineService.showPlanet(planet);
    this.booleanPlanet = true;
    this.currentPlanet = planet;
    this.marketService.getMarketsByPlanetId(this.currentPlanet.id)
    .subscribe((markets: Market[]) => {
            this.marketData = markets;
            // this.currentPlanet = this.marketData.
            console.log("MarketData:", this.marketData);
      });
  }
  onCancelClick() {
    this.booleanPlanet = false;
    this.engineService.setInfo(false);
  }
  onTravelClick(currentPlanet: Planet) {
    if (this.userData != null && this.userData != undefined) {
      this.onOtherStarPlanetClick(currentPlanet, this.userData, true);
    }
  }
  onOtherStarPlanetClick(
    planet: Planet,
    userData: Player,
    viajarAPlaneta: boolean
  ) {
    //convertimos la respuesta en la interfaz SpacecraftPlanet
    const spacecraftPlanet: SpacecraftPlanet = {
      idPlanet: planet.id,
      idUser: userData.id,
    };

    var tiempo;

    this.starService.getDistanceBetweenStars(planet.star.id, this.currentStar.id).subscribe((distance) => {
      console.log('Distancia entre estrellas:', distance);
      this.distance = distance;
    });
    this.spacecraftService
      .setPlanet(spacecraftPlanet)
      .pipe
      //delay(2000)
      ()
      .subscribe((data) => {
        console.log(data);
        if (viajarAPlaneta) {
          console.log("No se decrementa el tiempo")
          this.router.navigate(['/space-travelling']).then(() => {
            // Redirigir a '/space-travel' después de 2 segundos
            setTimeout(() => {
              this.router.navigate(['/sell']);
            }, 1000); // retraso de 2 segundos
          });
        } else {
          tiempo = Math.round(this.distance / this.currentSpacecraftModel.maxSpeed);
          console.log('Tiempo gastado:', tiempo);
          this.timeService.decrementTimeBy(tiempo);
          this.router.navigate(['/space-travelling']).then(() => {
            // Redirigir a '/space-travel' después de 5 segundos
            setTimeout(() => {
              this.router.navigate(['/sell']);
            }, 5000); // retraso de 5 segundos
          });
        }
      },
    (error) => {
      console.error('Error al viajar a otro planeta:', error);
      if (error.status === 403) {
        this.openAlertDialog('Al parecer eres un COMERCIANTE, por lo tanto no puedes pilotar hacia otros planetas y estrellas, dile a tus colegas que te lleven!');
      }
    });
  }

  onStarClick(star: Star) {
    console.log('star clickeada', star);
    if (this.starClicked === star) {
      this.deployPlanetList = !this.deployPlanetList; // Toggle para cerrar o abrir la lista de planetas
    } else {
      this.starClicked = star; // Almacena la estrella clicada
      this.deployPlanetList = true; // Abre la lista de planetas para la nueva estrella
    }
  }

  getCurrentStarAndPlanets(): void {
    console.log(this.userData);
    if(this.userData.id != 0){
      this.starService.getStarDataBasedOnUser(this.userData.id).subscribe((star: Star) => {
        this.currentStar = star;
        console.log('La estrella en la que estoy es:', this.currentStar); 

        this.planetService.getPlanetsByStarId(this.currentStar.id).subscribe(planets => {
          this.starPlanets = planets;
          console.log(this.starPlanets);
        });
        this.starService.getNearestStars(this.currentStar.id).subscribe(nearestStars => {
          this.nearestStars = nearestStars;
          //por cada una guardamos sus planetas
          this.nearestStars.forEach(nearestStar => {
            this.planetService.getPlanetsByStarId(nearestStar.id).subscribe(planets => {
              nearestStar.planets = planets;
            });
        });
    });
  });
}
}
openAlertDialog(message: string) {
  this.dialog.open(AlertComponent, {
      data: {
          message: message
      },
      panelClass: '.dialog-container',
      width: '80%',
      height: '80%',
      disableClose: false
  });
}

}
