import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwEventAgeIcon } from './bnw-event-age-icon';

describe('BnwEventAgeIcon', () => {
  let component: BnwEventAgeIcon;
  let fixture: ComponentFixture<BnwEventAgeIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwEventAgeIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwEventAgeIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
