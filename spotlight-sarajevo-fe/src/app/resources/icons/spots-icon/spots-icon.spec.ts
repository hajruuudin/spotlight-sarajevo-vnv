import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpotsIcon } from './spots-icon';

describe('SpotsIcon', () => {
  let component: SpotsIcon;
  let fixture: ComponentFixture<SpotsIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpotsIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SpotsIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
