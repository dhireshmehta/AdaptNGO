import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AdaptNgoTestModule } from '../../../test.module';
import { EndUserUpdateComponent } from 'app/entities/end-user/end-user-update.component';
import { EndUserService } from 'app/entities/end-user/end-user.service';
import { EndUser } from 'app/shared/model/end-user.model';

describe('Component Tests', () => {
  describe('EndUser Management Update Component', () => {
    let comp: EndUserUpdateComponent;
    let fixture: ComponentFixture<EndUserUpdateComponent>;
    let service: EndUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AdaptNgoTestModule],
        declarations: [EndUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EndUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EndUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EndUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EndUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new EndUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
