import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AdaptNgoTestModule } from '../../../test.module';
import { EndUserComponent } from 'app/entities/end-user/end-user.component';
import { EndUserService } from 'app/entities/end-user/end-user.service';
import { EndUser } from 'app/shared/model/end-user.model';

describe('Component Tests', () => {
  describe('EndUser Management Component', () => {
    let comp: EndUserComponent;
    let fixture: ComponentFixture<EndUserComponent>;
    let service: EndUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AdaptNgoTestModule],
        declarations: [EndUserComponent],
        providers: []
      })
        .overrideTemplate(EndUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EndUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EndUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new EndUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.endUsers && comp.endUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
