import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LoginService } from './auth/login.service';
import { Player } from '../model/player';
import { PlayerService } from './player.service';
import { Spacecraft } from '../model/spacecraft';

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  userData: Player;
  spaceCraftData?: Spacecraft;
  private counterSubject = new BehaviorSubject<number>(0);
  public counter$ = this.counterSubject.asObservable();

  constructor() { 
  }

  public loadTime( time:number): void {
      console.log('Tiempo:', time);
      if (time) {
        this.counterSubject.next(time);
      }
      this.startCountdown();
  }

  decrementTime(): void {
    console.log('Decrementando tiempo');
    let currentCounter = this.counterSubject.getValue();
    console.log('Tiempo actual:', currentCounter);
    currentCounter=Math.max(0, currentCounter-1);
    localStorage.setItem('time', currentCounter.toString());
    this.counterSubject.next(currentCounter);
  }

    startCountdown(): void {
      setInterval(() => { //
        this.decrementTime();
      }, 1000);
    }
    
}
