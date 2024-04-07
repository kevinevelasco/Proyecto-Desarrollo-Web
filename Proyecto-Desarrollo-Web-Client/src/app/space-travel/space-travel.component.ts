import { Component, OnDestroy, OnInit } from '@angular/core';
import { Player } from '../model/player';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/auth/login.service';

@Component({
  selector: 'app-space-travel',
  templateUrl: './space-travel.component.html',
  styleUrls: ['./space-travel.component.css']
})
export class SpaceTravelComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  userData?: Player;
  private loginSubscription: Subscription;
  private userDataSubscription: Subscription;

  constructor(private loginService: LoginService) {}

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
    if (this.userDataSubscription) {
      this.userDataSubscription.unsubscribe();
    }
  }
}
