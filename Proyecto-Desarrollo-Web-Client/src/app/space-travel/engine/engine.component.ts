import {Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {EngineService} from './engine.service';
import { Planet } from '../../model/planet';
import { Star } from '../../model/star';

@Component({
  selector: 'app-engine',
  templateUrl: './engine.component.html'
})
export class EngineComponent implements OnInit, OnChanges, OnDestroy{

  @Input() starPlanets: Planet[] = [];
  @Input() nearestStars : Star[] = [];
  @ViewChild('rendererCanvas', {static: true}) 
  public rendererCanvas: ElementRef<HTMLCanvasElement>

  public constructor(private engServ: EngineService) {
  }

  public ngOnInit(): void {
    this.engServ.createScene(this.rendererCanvas);
    this.engServ.animate();
  }

  ngOnDestroy(): void {
    this.engServ.ngOnDestroy();
    console.log("ngOnDestroy");
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
    let planets: Planet[] = [];
    let nearestStars: Star[] = [];
    for (let i in changes){
      if(i === 'starPlanets'){
        planets = changes[i].currentValue;
      }
      else if (i === 'nearestStars'){
        nearestStars = changes[i].currentValue;
      }
    }
    console.log(planets);
    console.log(nearestStars);
    if(planets.length > 0){
      this.engServ.addPlanets(planets);
    }
  }

}