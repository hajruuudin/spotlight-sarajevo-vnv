import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TextHolder } from './text-holder';

describe('TextHolder', () => {
  let component: TextHolder;
  let fixture: ComponentFixture<TextHolder>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TextHolder]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TextHolder);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
