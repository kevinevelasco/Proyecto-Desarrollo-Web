import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpacecraftModelComponent } from './spacecraft-model.component';

describe('SpacecraftModelComponent', () => {
  let component: SpacecraftModelComponent;
  let fixture: ComponentFixture<SpacecraftModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpacecraftModelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpacecraftModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
