import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdaptNgoTestModule } from '../../../test.module';
import { EndUserDetailComponent } from 'app/entities/end-user/end-user-detail.component';
import { EndUser } from 'app/shared/model/end-user.model';

describe('Component Tests', () => {
  describe('EndUser Management Detail Component', () => {
    let comp: EndUserDetailComponent;
    let fixture: ComponentFixture<EndUserDetailComponent>;
    const route = ({ data: of({ endUser: new EndUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AdaptNgoTestModule],
        declarations: [EndUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EndUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EndUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load endUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.endUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
