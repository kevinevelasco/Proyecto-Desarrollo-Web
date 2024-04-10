import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Star } from '../model/star';
import { environment } from '../../environments/environment.development';
import { Player } from '../model/player';

@Injectable({
  providedIn: 'root'
})
export class StarService {
  constructor(
    private http: HttpClient
  ) { }

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
      //'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
  )

  getStars(): Observable<Star[]> {
    return this.http.get<Star[]>(`${environment.serverUrl}/api/star/list`);
  }
  getStar(id: number): Observable<Star>  {
    return this.http.get<Star>(`${environment.serverUrl}/api/star/${id}`);
  }
  getStarDataBasedOnUser(id: number): Observable<Star> {
    return this.http.get<Star>(`${environment.serverUrl}/api/star/player/${id}`);
  }

  getNearestStars(id: number): Observable<Star[]> {
    return this.http.get<Star[]>(`${environment.serverUrl}/api/star/${id}/nearest`);
  }

}
