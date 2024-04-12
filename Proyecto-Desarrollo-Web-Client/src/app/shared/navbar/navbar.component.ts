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
    const userData = localStorage.getItem('currentUserData');
    this.userLoginOn = !!userData; // !! convierte el valor a booleano
    if (this.userLoginOn) {
      this.loginService.currentUserLoginOn.next(true);
    }
  }
}

