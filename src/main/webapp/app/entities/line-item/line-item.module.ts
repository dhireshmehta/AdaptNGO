import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdaptNgoSharedModule } from 'app/shared/shared.module';
import { LineItemComponent } from './line-item.component';
import { LineItemDetailComponent } from './line-item-detail.component';
import { LineItemUpdateComponent } from './line-item-update.component';
import { LineItemDeleteDialogComponent } from './line-item-delete-dialog.component';
import { lineItemRoute } from './line-item.route';

@NgModule({
  imports: [AdaptNgoSharedModule, RouterModule.forChild(lineItemRoute)],
  declarations: [LineItemComponent, LineItemDetailComponent, LineItemUpdateComponent, LineItemDeleteDialogComponent],
  entryComponents: [LineItemDeleteDialogComponent]
})
export class AdaptNgoLineItemModule {}
