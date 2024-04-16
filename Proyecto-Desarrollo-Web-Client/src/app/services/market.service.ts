// src/app/services/market.service.ts

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Market } from '../model/market';
import { environment } from '../../environments/environment.development';
import { Spacecraft } from '../model/spacecraft';
import { SpacecraftModel } from '../model/spacecraft-model';

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

  actualizarCreditos(idNave: number, creditos: number): Observable<Spacecraft> {
    return this.http.put<Spacecraft>
    (`${environment.serverUrl}/api/market/sell/${idNave}/${creditos}`, idNave, { headers: this.headers });
  }

  sellProductStock(idProducto: number, idPlaneta: number, cantidad: number): Observable<Market> {
    return this.http.put<Market>
    (`${environment.serverUrl}/api/market/venta/${idProducto}/${idPlaneta}/${cantidad}`, idProducto, { headers: this.headers });
  }


}
