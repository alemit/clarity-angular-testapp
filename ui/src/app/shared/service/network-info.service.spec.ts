import { TestBed, inject } from '@angular/core/testing';

import { NetworkInfoService } from './network-info.service';

describe('NetworkInfoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NetworkInfoService]
    });
  });

  it('should be created', inject([NetworkInfoService], (service: NetworkInfoService) => {
    expect(service).toBeTruthy();
  }));
});
