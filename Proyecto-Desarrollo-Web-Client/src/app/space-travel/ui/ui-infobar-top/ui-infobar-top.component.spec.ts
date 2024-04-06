import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiInfobarTopComponent } from './ui-infobar-top.component';

describe('UiInfobarTopComponent', () => {
  let component: UiInfobarTopComponent;
  let fixture: ComponentFixture<UiInfobarTopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UiInfobarTopComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UiInfobarTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
