import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideosOfUserComponent } from './videos-of-user.component';

describe('VideosOfUserComponent', () => {
  let component: VideosOfUserComponent;
  let fixture: ComponentFixture<VideosOfUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VideosOfUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideosOfUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
