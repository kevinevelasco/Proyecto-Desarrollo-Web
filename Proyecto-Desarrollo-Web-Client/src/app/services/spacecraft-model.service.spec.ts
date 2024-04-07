import { TestBed } from '@angular/core/testing';

import { SpacecraftModelService } from './spacecraft-model.service';

describe('SpacecraftModelService', () => {
  let service: SpacecraftModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpacecraftModelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
