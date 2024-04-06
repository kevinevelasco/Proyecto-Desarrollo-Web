import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiInfobarBottomComponent } from './ui-infobar-bottom.component';

describe('UiInfobarBottomComponent', () => {
  let component: UiInfobarBottomComponent;
  let fixture: ComponentFixture<UiInfobarBottomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UiInfobarBottomComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UiInfobarBottomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
