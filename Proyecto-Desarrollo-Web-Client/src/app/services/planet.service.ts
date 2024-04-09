import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Planet } from '../model/planet';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class PlanetService {

  constructor(
    private http: HttpClient
  ) { }

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
      //'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
  )

  getPlanetsByStarId(starId: number): Observable<Planet[]> {
    return this.http.get<Planet[]>(`${environment.serverUrl}/api/star/${starId}/planets`);
  }
}
