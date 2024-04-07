import { StarService } from './../../services/star.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Star } from '../../model/star';
import { Player } from '../../model/player';

@Component({
  selector: 'app-space-travel-interface',
  templateUrl: './space-travel-interface.component.html',
  styleUrl: './space-travel-interface.component.css'
})
export class SpaceTravelInterfaceComponent implements OnInit, OnDestroy {
  constructor(private starService: StarService) { }
  @Input() userData?: Player;
  currentStar : Star;
  ngOnInit(): void {
    // if(this.userData != null){
    //   this.starService.getStarDataBasedOnUser(this.userData.id).subscribe((star: Star) => this.currentStar = star);
    // }
  }
  ngOnDestroy(): void {
  }

}
