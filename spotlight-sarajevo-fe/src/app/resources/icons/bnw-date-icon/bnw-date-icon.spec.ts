import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwDateIcon } from './bnw-date-icon';

describe('BnwDateIcon', () => {
  let component: BnwDateIcon;
  let fixture: ComponentFixture<BnwDateIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwDateIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwDateIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
