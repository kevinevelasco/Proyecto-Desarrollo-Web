import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiInfobarRightComponent } from './ui-infobar-right.component';

describe('UiInfobarRightComponent', () => {
  let component: UiInfobarRightComponent;
  let fixture: ComponentFixture<UiInfobarRightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UiInfobarRightComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UiInfobarRightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
