import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollectionIcon } from './collection-icon';

describe('CollectionIcon', () => {
  let component: CollectionIcon;
  let fixture: ComponentFixture<CollectionIcon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CollectionIcon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CollectionIcon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
