import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { SpacecraftModel } from '../model/spacecraft-model';
import { environment } from '../../environments/environment.development';
import { Spacecraft } from '../model/spacecraft';
import { SpacecraftPlanet } from '../space-travel/ui/spacecraftPlanet';
import { Planet } from '../model/planet';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpacecraftService {


  private spaceCraftSubject = new BehaviorSubject<Spacecraft | null>(null);
  spaceCraftData$ = this.spaceCraftSubject.asObservable();
  
  constructor(
    private http: HttpClient
  ) { }

  updateSpaceCraftData(spaceCraft: Spacecraft): void {
    this.spaceCraftSubject.next(spaceCraft);
  }

  getCurrentSpaceCraftData(): Spacecraft | null {
    return this.spaceCraftSubject.getValue();
  }

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

  //mediante una petición patch al servidor hacemos update
  setPlanet(spacecraftPlanet: SpacecraftPlanet): Observable<Spacecraft> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    
    return this.http.patch<Spacecraft>(`${environment.serverUrl}/api/spacecraft/player/${spacecraftPlanet.idUser}/planet/${spacecraftPlanet.idPlanet}`, null);
  }

  getPlanetBySpacecraft(spacecraftId: number): Observable<Planet> {
    return this.http.get<Planet>(`${environment.serverUrl}/api/spacecraft/${spacecraftId}/planet`);
  }

  getSpacecraftsByPlanet(planetId: number): Observable<Spacecraft[]> {
    return this.http.get<Spacecraft[]>(`${environment.serverUrl}/api/spacecraft/${planetId}/spacecrafts`);
  }

}



