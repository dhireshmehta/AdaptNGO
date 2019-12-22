import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { AdaptNgoSharedModule } from 'app/shared/shared.module';
import { AdaptNgoCoreModule } from 'app/core/core.module';
import { AdaptNgoAppRoutingModule } from './app-routing.module';
import { AdaptNgoHomeModule } from './home/home.module';
import { AdaptNgoEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    AdaptNgoSharedModule,
    AdaptNgoCoreModule,
    AdaptNgoHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AdaptNgoEntityModule,
    AdaptNgoAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class AdaptNgoAppModule {}
