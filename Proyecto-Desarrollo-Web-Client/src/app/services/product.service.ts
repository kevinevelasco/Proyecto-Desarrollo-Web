// src/app/services/product.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../model/product';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
    private headers = new HttpHeaders({
        'Content-Type': 'application/json'
        // 'Authorization': 'Bearer ' + localStorage.getItem('token') // Descomentar si es necesario
    });

    constructor(private http: HttpClient) {}

    /**
     * Obtiene los detalles de un producto por su ID.
     * @param productId El ID del producto para buscar sus detalles.
     * @return Un Observable de un objeto Product.
     */
    getProductById(productId: number): Observable<Product> {
      return this.http.get<Product>(`${environment.serverUrl}/api/product/${productId}`, {
          headers: this.headers
      });
    }
}
