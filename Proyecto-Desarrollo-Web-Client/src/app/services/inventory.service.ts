import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Inventory } from '../model/inventory';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  constructor(private http: HttpClient) {}
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  getInventoryBySpacecraftId(spacecraftId: number): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(
      `${environment.serverUrl}/api/spacecraft/${spacecraftId}/inventory`
    );
  }

  getTotalBySpacecraftId(id: number): Observable<number> {
    return this.http.get<number>(
      `${environment.serverUrl}/api/inventory/get/total/${id}`
    );
  }
  updateInventoryQuantity(spacecraftId: number, productId: number, toDo: string): Observable<Inventory> {
    console.log("Update inventory quantity", `${environment.serverUrl}/api/inventory/update/${spacecraftId}/${productId}`);
    return this.http.patch<Inventory>(
      `${environment.serverUrl}/api/inventory/update/${spacecraftId}/${productId}/${toDo}`,
      null
    );
  }

  createProductInInventory(spacecraftId: number, productId: number): Observable<Inventory> {
    return this.http.post<Inventory>(
      `${environment.serverUrl}/api/inventory/create/${spacecraftId}/${productId}`,
      null
    );
  }
}

