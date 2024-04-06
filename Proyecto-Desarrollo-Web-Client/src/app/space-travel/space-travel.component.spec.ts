import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpaceTravelComponent } from './space-travel.component';

describe('SpaceTravelComponent', () => {
  let component: SpaceTravelComponent;
  let fixture: ComponentFixture<SpaceTravelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpaceTravelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpaceTravelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
