import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpacecraftModelListComponent } from './spacecraft-model-list.component';

describe('SpacecraftModelListComponent', () => {
  let component: SpacecraftModelListComponent;
  let fixture: ComponentFixture<SpacecraftModelListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpacecraftModelListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpacecraftModelListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
