import { PlayerService } from './../player.service';
import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { LoginRequest } from './loginRequest';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../../environments/environment.development';
import { Player } from '../../model/player';
import { catchError, throwError, BehaviorSubject, tap, map } from 'rxjs';
import { JwtAuthenticationResponse } from './jwt-authentication-response';
import { PlayerType } from '../../model/player-type';

const JWT_TOKEN = "jwt-token";
const USERNAME = "user-name";
const ID = "user-id";
const ROLE = "user-role";

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  currentUserLoginOn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );
  currentUserData: BehaviorSubject<JwtAuthenticationResponse> = new BehaviorSubject<JwtAuthenticationResponse>({id: 0, token:'', username:'', role: PlayerType.CAPTAIN});
  constructor(private http: HttpClient, private playerService: PlayerService) {
    this.currentUserLoginOn = new BehaviorSubject<boolean>(sessionStorage.getItem(JWT_TOKEN) != null)
    this.currentUserData = new BehaviorSubject<JwtAuthenticationResponse>({
      id: +(sessionStorage.getItem(ID)!), 
      token: sessionStorage.getItem(JWT_TOKEN) ?? '', 
      username: sessionStorage.getItem(USERNAME) ?? '', 
      role: sessionStorage.getItem(ROLE) as PlayerType
    })
  }

  login(credentials: LoginRequest): Observable<JwtAuthenticationResponse> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    console.log(credentials)
    return this.http.post<JwtAuthenticationResponse>(
      `${environment.serverUrl}/auth/login`,
      credentials,
      httpOptions
    ).pipe(
      tap((userData) => {
        console.log(userData);
        sessionStorage.setItem(ID, userData.id.toString());
        sessionStorage.setItem(JWT_TOKEN, userData.token);
        sessionStorage.setItem(USERNAME, userData.username);
        sessionStorage.setItem(ROLE, userData.role);

        this.currentUserData.next(userData);
        this.currentUserLoginOn.next(true);
      }),
      map(jwt => {
        // Importante: https://stackoverflow.com/questions/27067251/where-to-store-jwt-in-browser-how-to-protect-against-csrf
        sessionStorage.setItem(ID, jwt.id.toString());
        sessionStorage.setItem(JWT_TOKEN, jwt.token);
        sessionStorage.setItem(USERNAME, jwt.username);
        sessionStorage.setItem(ROLE, jwt.role);
        return jwt;
      }),
      catchError(this.handleError)
    );
  }
  

  private handleError(error:HttpErrorResponse){
    if(error.status===0){
      console.error('Se ha producio un error ', error.error);
    }
    else{
      console.error('Backend retornó el código de estado ', error);
    }
    return throwError(()=> new Error('Algo falló. Por favor intente nuevamente.'));
  }
  
  get userData(): Observable<JwtAuthenticationResponse> {
    return this.currentUserData.asObservable();
  }

  get userLoginOn(): Observable<boolean> {
    return this.currentUserLoginOn.asObservable();
  }
  logout() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('role');
    this.currentUserLoginOn.next(false);
  }

  isAuthenticated() {
    return sessionStorage.getItem(JWT_TOKEN) != null;
  }

  token() {
    return sessionStorage.getItem(JWT_TOKEN);
  }

  role() {
    return sessionStorage.getItem(ROLE);
  }

  // setCurrentUserData(userData: Player) {
  //   this.currentUserData.next(userData);
  // }
}
