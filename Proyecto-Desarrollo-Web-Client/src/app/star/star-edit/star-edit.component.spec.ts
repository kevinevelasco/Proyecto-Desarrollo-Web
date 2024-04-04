import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StarEditComponent } from './star-edit.component';

describe('StarEditComponent', () => {
  let component: StarEditComponent;
  let fixture: ComponentFixture<StarEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StarEditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StarEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
