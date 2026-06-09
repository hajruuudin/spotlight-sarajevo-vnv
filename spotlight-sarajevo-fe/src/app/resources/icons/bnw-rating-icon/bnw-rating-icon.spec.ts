import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwRatingIcon } from './bnw-rating-icon';

describe('BnwRatingIcon', () => {
  let component: BnwRatingIcon;
  let fixture: ComponentFixture<BnwRatingIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwRatingIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwRatingIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
