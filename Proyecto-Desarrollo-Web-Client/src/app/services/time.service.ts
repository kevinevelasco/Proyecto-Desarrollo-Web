import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginService } from './auth/login.service';
import { Player } from '../model/player';
import { PlayerService } from './player.service';
import { Spacecraft } from '../model/spacecraft';
import { SpacecraftService } from './spacecraft.service';

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  userData: Player;
  spaceCraftData?: Spacecraft;
  started: boolean = false;
  isPaused: boolean = false;
  private counterSubject = new BehaviorSubject<number>(0);
  public counter$ = this.counterSubject.asObservable();
  private interval: any;

  constructor(private spaceCraftService: SpacecraftService) { 
    const storedTime = localStorage.getItem('time');
    if (storedTime) {
      this.counterSubject.next(parseInt(storedTime));
      this.started = true;
      this.startCountdown();
    }
  }
  restartValues(): void {
    this.started = false;
    this.isPaused = false;
    localStorage.removeItem('time');
  }

  public loadTime( time:number): void {
    if(this.started){
      console.log('Ya se ha iniciado el tiempo');
      return
    }
      console.log('Tiempo:', time);
      this.started = true;
      if (time) {
        this.counterSubject.next(time);
        localStorage.setItem('time', time.toString());
      }
  }

  decrementTime(): void {
    let currentCounter = this.counterSubject.getValue();
    currentCounter=Math.max(0, currentCounter-1);
    localStorage.setItem('time', currentCounter.toString());
    this.counterSubject.next(currentCounter);
  }

  decrementTimeBy(time: number): void {
    console.log('Decrementando tiempo');
    let currentCounter = this.counterSubject.getValue();
    console.log('Tiempo actual:', currentCounter);
    currentCounter=Math.max(0, currentCounter-time);
    localStorage.setItem('time', currentCounter.toString());
    this.counterSubject.next(currentCounter);
  }

  private startCountdown(): void {
    if (!this.interval) {
      this.interval = setInterval(() => {
        if (!this.isPaused) {
          this.decrementTime();
        }
      }, 1000);
    }
  }
   pauseCountdown(): void {
    this.isPaused = true;
  }
  resumeCountdown(): void {
    this.isPaused = false;
  }
    updateTimeBySpaceCraft(spaceCraft: Spacecraft): Observable<Spacecraft> {
      return this.spaceCraftService.setSpacecraftTime(spaceCraft);
    }
    
}
