import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwEventLangIcon } from './bnw-event-lang-icon';

describe('BnwEventLangIcon', () => {
  let component: BnwEventLangIcon;
  let fixture: ComponentFixture<BnwEventLangIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwEventLangIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwEventLangIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
