import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {EngineService} from './engine.service';
import { Planet } from '../../model/planet';

@Component({
  selector: 'app-engine',
  templateUrl: './engine.component.html'
})
export class EngineComponent implements OnInit, OnChanges{

  @Input() starPlanets: Planet[] = [];
  @ViewChild('rendererCanvas', {static: true}) 
  public rendererCanvas: ElementRef<HTMLCanvasElement>

  public constructor(private engServ: EngineService) {
  }

  public ngOnInit(): void {
    this.engServ.createScene(this.rendererCanvas);
    this.engServ.animate();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
    let planets: Planet[] = [];
    for (let i in changes){
      let chng = changes[i];
      let cur = JSON.stringify(chng.currentValue);
      let prev = JSON.stringify(chng.previousValue);

      //convertimos el valor actual en un array de planetas
      planets = JSON.parse(cur);
    }
    console.log(planets[0].name, planets[0].position, planets[0].ring, planets[0].size, planets[0].texture);
    if(planets.length > 0){
      //para aplicar una textura y demás atributos que no cambien cuando cambio de componentes //this.engServ.addPlanets(planets);
      this.engServ.addPlanets(planets);
    }
  }

}