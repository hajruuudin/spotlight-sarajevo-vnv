import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BnwCategoryIcon } from './bnw-category-icon';

describe('BnwCategoryIcon', () => {
  let component: BnwCategoryIcon;
  let fixture: ComponentFixture<BnwCategoryIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BnwCategoryIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BnwCategoryIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
