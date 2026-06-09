import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwLocationIcon } from './bnw-location-icon';

describe('BnwLocationIcon', () => {
  let component: BnwLocationIcon;
  let fixture: ComponentFixture<BnwLocationIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwLocationIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwLocationIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
