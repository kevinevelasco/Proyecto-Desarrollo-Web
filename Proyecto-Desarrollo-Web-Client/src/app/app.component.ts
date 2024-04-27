import { Component } from '@angular/core';
import { TimeService } from './services/time.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Proyecto-Desarrollo-Web-Client';
  constructor(private timeService: TimeService) {
    this.timeService.restartValues();
  }
}
