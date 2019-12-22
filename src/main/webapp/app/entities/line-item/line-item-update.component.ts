import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILineItem, LineItem } from 'app/shared/model/line-item.model';
import { LineItemService } from './line-item.service';
import { RolesService } from 'app/entities/roles/roles.service';
import {  IRoles } from 'app/shared/model/roles.model';
import { CategoryService } from 'app/entities/category/category.service';
import { ICategory } from 'app/shared/model/category.model';

@Component({
  selector: 'jhi-line-item-update',
  templateUrl: './line-item-update.component.html'
})
export class LineItemUpdateComponent implements OnInit {
  isSaving = false;
  rolesList : IRoles[]=[];
  categoriesList : ICategory[]=[];
  editForm = this.fb.group({
    id: [],
    name: [],
    link: [],
    categories: [],
    roles: [],
    desc: [],
    viewCount: [],
    type: [],
    rolesList: [],
    categoriesList: []
  });

  constructor(protected categoryService: CategoryService,protected roleService: RolesService,protected lineItemService: LineItemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lineItem }) => {
      this.updateForm(lineItem);
      this.roleService.query().subscribe((res: HttpResponse<IRoles[]>) => {
        this.rolesList = res.body ? res.body : [];
        this.editForm.controls.rolesList.patchValue(this.rolesList);
      });
      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => {
        this.categoriesList = res.body ? res.body : [];
        this.editForm.controls.categoriesList.patchValue(this.categoriesList);
      });

    });
  }

  updateForm(lineItem: ILineItem): void {
    this.editForm.patchValue({
      id: lineItem.id,
      name: lineItem.name,
      link: lineItem.link,
      categories: lineItem.categories?lineItem.categories.split(","):[],
      roles: lineItem.roles?lineItem.roles.split(","):[],
      desc: lineItem.desc,
      viewCount: lineItem.viewCount,
      type: lineItem.type
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    window.console.log(this.editForm)
    this.isSaving = true;
    const lineItem = this.createFromForm();
    if (lineItem.id !== undefined) {
      this.subscribeToSaveResponse(this.lineItemService.update(lineItem));
    } else {
      this.subscribeToSaveResponse(this.lineItemService.create(lineItem));
    }
  }

  private createFromForm(): ILineItem {
    return {
      ...new LineItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      link: this.editForm.get(['link'])!.value,
      
      categories: this.editForm.get(['categories'])!.value?this.editForm.get(['categories'])!.value.join(","):"",
      roles: this.editForm.get(['roles'])!.value?this.editForm.get(['roles'])!.value.join(","):"",
      desc: this.editForm.get(['desc'])!.value,
      viewCount: this.editForm.get(['viewCount'])!.value,
      type: this.editForm.get(['type'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILineItem>>): void {
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
