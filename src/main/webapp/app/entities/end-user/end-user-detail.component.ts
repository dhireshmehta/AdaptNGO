import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEndUser } from 'app/shared/model/end-user.model';

@Component({
  selector: 'jhi-end-user-detail',
  templateUrl: './end-user-detail.component.html'
})
export class EndUserDetailComponent implements OnInit {
  endUser: IEndUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endUser }) => {
      this.endUser = endUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
