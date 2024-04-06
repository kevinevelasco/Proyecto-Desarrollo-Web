import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiInfobarLeftComponent } from './ui-infobar-left.component';

describe('UiInfobarLeftComponent', () => {
  let component: UiInfobarLeftComponent;
  let fixture: ComponentFixture<UiInfobarLeftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UiInfobarLeftComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UiInfobarLeftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
