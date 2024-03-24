import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpacecraftModelViewComponent } from './spacecraft-model-view.component';

describe('SpacecraftModelViewComponent', () => {
  let component: SpacecraftModelViewComponent;
  let fixture: ComponentFixture<SpacecraftModelViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SpacecraftModelViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SpacecraftModelViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
