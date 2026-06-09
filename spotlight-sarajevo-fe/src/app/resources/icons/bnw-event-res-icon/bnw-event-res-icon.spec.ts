import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwEventResIcon } from './bnw-event-res-icon';

describe('BnwEventResIcon', () => {
  let component: BnwEventResIcon;
  let fixture: ComponentFixture<BnwEventResIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwEventResIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwEventResIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
