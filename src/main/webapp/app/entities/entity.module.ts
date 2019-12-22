import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.AdaptNgoCategoryModule)
      },
      {
        path: 'line-item',
        loadChildren: () => import('./line-item/line-item.module').then(m => m.AdaptNgoLineItemModule)
      },
      {
        path: 'roles',
        loadChildren: () => import('./roles/roles.module').then(m => m.AdaptNgoRolesModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AdaptNgoEntityModule {}
