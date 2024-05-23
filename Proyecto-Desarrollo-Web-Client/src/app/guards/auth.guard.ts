import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/auth/login.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard  {

  constructor(private auth: LoginService,
    private router: Router) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.auth.isAuthenticated()) {
      return true;
    } else {
      this.router.navigateByUrl("/login");
      return false;
    }
  }

}
