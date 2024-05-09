import {Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import { BackgroundService } from './background.service';
import { PageType } from './pageType';
import { Planet } from '../../model/planet';

@Component({
  selector: 'app-background',
  templateUrl: './background.component.html',
  styleUrl: './background.component.css'
})
export class BackgroundComponent implements OnInit, OnDestroy, OnChanges {
  
  @Input() pageType: PageType;
  @Input() planetData : Planet;

  @ViewChild('rendererCanvas', {static: true})
  public rendererCanvas: ElementRef<HTMLCanvasElement>

  public constructor(private backgroundService: BackgroundService) {
  }

  ngOnInit(): void {
    this.backgroundService.createScene(this.rendererCanvas);
    this.backgroundService.animate();
  }
  ngOnDestroy(): void {
    this.backgroundService.ngOnDestroy();
    console.log("ngOnDestroy");
  }
  ngOnChanges(changes: SimpleChanges): void {
    console.log("ngOnChanges");
    console.log(changes);
    var page: PageType | undefined;
    var planet : Planet;
    for (let i in changes){
      if(i === 'pageType'){
        page = changes[i].currentValue;
        if(page){
          this.backgroundService.page = page;
        }
      } else if(i === 'planetData'){
        this.planetData = changes[i].currentValue;
        this.planetData ? this.backgroundService.addCharacterBasedOnPlanet(this.planetData) : console.log("no hay planeta");
      }
  }
  if(page){
    console.log("entra")
    this.planetData ? console.log(this.planetData) : console.log("no hay planeta");
      // 
    }
}
}
