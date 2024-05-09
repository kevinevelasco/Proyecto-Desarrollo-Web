import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  private subscription: Subscription;
  ID = "user-id";
  constructor(private loginService: LoginService, private router:Router) {}

  ngOnInit(): void {
    this.checkUserLoginOn();
    this.subscription = this.loginService.currentUserLoginOn.subscribe(userLoginOn => {
      this.userLoginOn = userLoginOn;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();  // Desuscribirse de la suscripción
    }
  }

    logout() {
    this.loginService.logout();
    this.router.navigateByUrl('/home');
  }

  private checkUserLoginOn(): void {
    const userId: number = +(sessionStorage.getItem(this.ID) || 0);
    console.log(userId);
    if (userId != 0 && userId != null) {
      this.userLoginOn = true;
    }
  }
}

