import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeExpiredComponent } from './time-expired.component';

describe('TimeExpiredComponent', () => {
  let component: TimeExpiredComponent;
  let fixture: ComponentFixture<TimeExpiredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TimeExpiredComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TimeExpiredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
