import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { SpacecraftModel } from '../model/spacecraft-model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class SpacecraftModelService {

  spacecraftModelData: BehaviorSubject<SpacecraftModel[]> = new BehaviorSubject<SpacecraftModel[]>([]);

  constructor(private http: HttpClient) { }

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/json'
    }
  )

  getSpacecraftModelsBySpacecraftId(spacecraftId: number): Observable<SpacecraftModel> {
    return this.http.get<SpacecraftModel>(
      `${environment.serverUrl}/api/spacecraft-model/${spacecraftId}`
    );
  }

  get getSpacecraftModelData(): Observable<SpacecraftModel[]> {
    return this.spacecraftModelData.asObservable();
  }

}
