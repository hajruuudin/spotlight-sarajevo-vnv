import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpotMapOverview } from './spot-map-overview';

describe('SpotMapOverview', () => {
  let component: SpotMapOverview;
  let fixture: ComponentFixture<SpotMapOverview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpotMapOverview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SpotMapOverview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
