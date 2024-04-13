import {Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import { BackgroundService } from './background.service';

@Component({
  selector: 'app-background',
  templateUrl: './background.component.html',
  styleUrl: './background.component.css'
})
export class BackgroundComponent implements OnInit, OnDestroy {
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

}
