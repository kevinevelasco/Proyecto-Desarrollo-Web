import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { PlatformLocation } from '@angular/common';
import { Subscription } from 'rxjs';
import { LoginService } from '../../services/auth/login.service';
import { Player } from '../../model/player';

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

  constructor(private loginService: LoginService, private platformLocation: PlatformLocation) { 
    history.pushState(null, '', location.href);
    this.platformLocation.onPopState(() => {
      history.pushState(null, '', location.href);
    })
  }
  ngOnChanges(changes: SimpleChanges): void {
    throw new Error('Method not implemented.');
  }
  ngOnInit(): void {
    this.loginSubscription = this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    });
    this.userDataSubscription = this.loginService.currentUserData.subscribe({
      next: (userData) => {
        this.userData = userData;
      }
    });
  }
  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }

}
