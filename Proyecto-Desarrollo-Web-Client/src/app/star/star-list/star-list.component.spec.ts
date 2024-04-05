import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StarListComponent } from './star-list.component';

describe('StarListComponent', () => {
  let component: StarListComponent;
  let fixture: ComponentFixture<StarListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StarListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StarListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
