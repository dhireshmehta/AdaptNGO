import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEndUser } from 'app/shared/model/end-user.model';
import { EndUserService } from './end-user.service';
import { EndUserDeleteDialogComponent } from './end-user-delete-dialog.component';

@Component({
  selector: 'jhi-end-user',
  templateUrl: './end-user.component.html'
})
export class EndUserComponent implements OnInit, OnDestroy {
  endUsers?: IEndUser[];
  eventSubscriber?: Subscription;

  constructor(protected endUserService: EndUserService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.endUserService.query().subscribe((res: HttpResponse<IEndUser[]>) => {
      this.endUsers = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEndUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEndUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEndUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('endUserListModification', () => this.loadAll());
  }

  delete(endUser: IEndUser): void {
    const modalRef = this.modalService.open(EndUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.endUser = endUser;
  }
}
