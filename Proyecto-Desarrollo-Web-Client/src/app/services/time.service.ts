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
  started: boolean = false;
  private counterSubject = new BehaviorSubject<number>(0);
  public counter$ = this.counterSubject.asObservable();

  constructor() { 
  }

  public loadTime( time:number): void {
    if(this.started){
      return
    }
      console.log('Tiempo:', time);
      this.started = true;
      if (time) {
        this.counterSubject.next(time);
      }
      this.startCountdown();
  }

  decrementTime(): void {
    //console.log('Decrementando tiempo');
    let currentCounter = this.counterSubject.getValue();
    console.log('Tiempo actual:', currentCounter);
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

    startCountdown(): void {
      setInterval(() => { //
        this.decrementTime();
      }, 1000);
    }
    
}
