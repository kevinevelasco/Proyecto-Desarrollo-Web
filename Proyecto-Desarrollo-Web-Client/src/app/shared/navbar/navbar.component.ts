import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../../services/auth/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
  userLoginOn: boolean = false;
  constructor(private loginService: LoginService, private router:Router) {}
  ngOnDestroy(): void {
    this.loginService.currentUserLoginOn.unsubscribe();
  }
  ngOnInit(): void {
    this.loginService.currentUserLoginOn.subscribe({
      next: (userLoginOn) => {
        this.userLoginOn = userLoginOn;
      }
    }
    )
  }
    logout() {
    this.loginService.logout();
    this.router.navigateByUrl('/home');
  }
}

