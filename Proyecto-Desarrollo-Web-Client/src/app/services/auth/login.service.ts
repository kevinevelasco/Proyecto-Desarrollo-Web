import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { LoginRequest } from './loginRequest';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../../environments/environment.development';
import { SpacecraftModel } from '../../model/spacecraft-model';
import { Player } from '../../model/player';
import { catchError, throwError, BehaviorSubject, tap } from 'rxjs';
import { PlayerType } from '../../model/player-type';
import { Spacecraft } from '../../model/spacecraft';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  currentUserLoginOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );
  currentUserData: BehaviorSubject<Player> = new BehaviorSubject<Player>(new Player(0, '', '', PlayerType.CAPTAIN));
  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<Player> {
    {
      return this.http.post<any>(`${environment.serverUrl}/api/auth/login`, credentials).pipe(
        tap((userData) => {
          this.currentUserData.next(userData);
          this.currentUserLoginOn.next(true);
        }),
        catchError(this.handleError)
      );
    }
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Se ha producido un error: ' + error.error);
    } else {
      console.error(
        'Backend retornó el código de estado: ' + error.status,
        error.error
      );
    }

    return throwError(
      () => new Error('Algo falló. Por favor intente nuevamente')
    );
  }

  get userData(): Observable<Player> {
    return this.currentUserData.asObservable();
  }

  get userLoginOn(): Observable<boolean> {
    return this.currentUserLoginOn.asObservable();
  }
  logout() {
    this.currentUserLoginOn.next(false);
    this.currentUserData.next(new Player(0, '', '', PlayerType.CAPTAIN));
  }
}
