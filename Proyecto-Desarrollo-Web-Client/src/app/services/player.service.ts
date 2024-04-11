import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Player } from '../model/player';
import { Spacecraft } from '../model/spacecraft';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  constructor(
    private http: HttpClient
  ) { }
  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
      //'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
  )
  getPlayerById(playerId: number): Observable<Player> {
    return this.http.get<Player>(`${environment.serverUrl}/api/player/${playerId}`);
  }
  getPlayerSpacecraft(playerId: number): Observable<Spacecraft> {
    return this.http.get<Spacecraft>(`${environment.serverUrl}/api/player/${playerId}/spacecraft`);
  }

}
