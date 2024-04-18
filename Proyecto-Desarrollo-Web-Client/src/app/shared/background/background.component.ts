import {Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import { BackgroundService } from './background.service';
import { PageType } from './pageType';

@Component({
  selector: 'app-background',
  templateUrl: './background.component.html',
  styleUrl: './background.component.css'
})
export class BackgroundComponent implements OnInit, OnDestroy, OnChanges {
  
  @Input() pageType: PageType;

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
    var page: PageType;
    for (let i in changes){
      if(i === 'pageType'){
        page = changes[i].currentValue;
        this.backgroundService.page = page;
      }
  }
}
}
