import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwEventPriceIcon } from './bnw-event-price-icon';

describe('BnwEventPriceIcon', () => {
  let component: BnwEventPriceIcon;
  let fixture: ComponentFixture<BnwEventPriceIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwEventPriceIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwEventPriceIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
