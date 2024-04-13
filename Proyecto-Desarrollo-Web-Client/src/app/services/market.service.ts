// src/app/services/market.service.ts

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Market } from '../model/market';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class MarketService {

  marketData: BehaviorSubject<Market[]> = new BehaviorSubject<Market[]>([]);

  constructor(private http: HttpClient) {}

    private headers = new HttpHeaders(
      {
        'Content-Type': 'application/json'
      }
  ) 
    
  getMarketsByPlanetId(planetId: number): 
    Observable<Market[]> {
      return this.http.get<Market[]>
      (`${environment.serverUrl}/api/market/planet/${planetId}`
      );
  }

  get getMarketData(): Observable<Market[]> {
    return this.marketData.asObservable();
  }

}
