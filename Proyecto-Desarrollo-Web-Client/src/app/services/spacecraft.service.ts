import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { SpacecraftModel } from '../model/spacecraft-model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class SpacecraftService {
  constructor(
    private http: HttpClient
  ) { }

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
      //'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
  )

  getSpacecraftModels(): Observable<SpacecraftModel[]> {
    return this.http.get<SpacecraftModel[]>(`${environment.serverUrl}/api/spacecraft-model/list`);
  }

  getSpacecraftModel(id: number): Observable<SpacecraftModel>  {
    return this.http.get<SpacecraftModel>(`${environment.serverUrl}/api/spacecraft-model/${id}`);
  }
  saveSpacecraftModel(spacecraftModel: SpacecraftModel): Observable<SpacecraftModel> {
    return this.http.put<SpacecraftModel>(`${environment.serverUrl}/api/spacecraft-model`, spacecraftModel, { headers: this.headers });
  }
}
