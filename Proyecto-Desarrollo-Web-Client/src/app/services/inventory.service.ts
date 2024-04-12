import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Inventory } from '../model/inventory';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  constructor(
    private http: HttpClient
  ) { }
  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
    }
  )
 getInventoryBySpacecraftId(spacecraftId: number): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(`${environment.serverUrl}/api/spacecraft/${spacecraftId}/inventory`);
  }
}