import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEndUser, EndUser } from 'app/shared/model/end-user.model';
import { EndUserService } from './end-user.service';

@Component({
  selector: 'jhi-end-user-update',
  templateUrl: './end-user-update.component.html'
})
export class EndUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    email: [],
    firstName: [],
    lastName: [],
    roles: []
  });

  constructor(protected endUserService: EndUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endUser }) => {
      this.updateForm(endUser);
    });
  }

  updateForm(endUser: IEndUser): void {
    this.editForm.patchValue({
      id: endUser.id,
      email: endUser.email,
      firstName: endUser.firstName,
      lastName: endUser.lastName,
      roles: endUser.roles
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endUser = this.createFromForm();
    if (endUser.id !== undefined) {
      this.subscribeToSaveResponse(this.endUserService.update(endUser));
    } else {
      this.subscribeToSaveResponse(this.endUserService.create(endUser));
    }
  }

  private createFromForm(): IEndUser {
    return {
      ...new EndUser(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      roles: this.editForm.get(['roles'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndUser>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
