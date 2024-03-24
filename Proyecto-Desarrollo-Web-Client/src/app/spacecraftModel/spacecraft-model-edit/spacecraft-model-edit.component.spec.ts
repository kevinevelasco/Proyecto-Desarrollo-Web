import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpacecraftModelEditComponent } from './spacecraft-model-edit.component';

describe('SpacecraftModelEditComponent', () => {
  let component: SpacecraftModelEditComponent;
  let fixture: ComponentFixture<SpacecraftModelEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpacecraftModelEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpacecraftModelEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
