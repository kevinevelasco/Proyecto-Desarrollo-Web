import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { PlatformLocation } from '@angular/common';
import { Subscription } from 'rxjs';
import { LoginService } from '../../services/auth/login.service';
import { Player } from '../../model/player';
import { Router } from '@angular/router';

@Component({
  selector: 'app-space-travel-interface',
  templateUrl: './space-travel-interface.component.html',
  styleUrl: './space-travel-interface.component.css'
})
export class SpaceTravelInterfaceComponent implements OnInit, OnDestroy, OnChanges {
  userLoginOn: boolean = false;
  userData?: Player;
  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;
  ID = "user-id";

  constructor(private router: Router, private platformLocation: PlatformLocation) { 
    history.pushState(null, '', location.href);
    this.platformLocation.onPopState(() => {
      history.pushState(null, '', location.href);
    })
  }
  ngOnChanges(changes: SimpleChanges): void {
    throw new Error('Method not implemented.');
  }
  ngOnInit(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userLoginOn = true;
    }else{
      this.router.navigate(['..//login']);
    }
  }
  ngOnDestroy(): void {
  }

}
