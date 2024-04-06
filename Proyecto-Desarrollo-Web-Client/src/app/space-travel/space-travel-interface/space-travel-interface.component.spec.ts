import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpaceTravelInterfaceComponent } from './space-travel-interface.component';

describe('SpaceTravelInterfaceComponent', () => {
  let component: SpaceTravelInterfaceComponent;
  let fixture: ComponentFixture<SpaceTravelInterfaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpaceTravelInterfaceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpaceTravelInterfaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
