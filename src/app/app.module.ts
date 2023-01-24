import {NgZorroAntdModule} from './ng-zorro-antd.module';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {HomeComponent} from './home/home.component';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {NetworkInterceptor} from './interceptor/network-interceptor';
import {NZ_I18N} from 'ng-zorro-antd/i18n';
import {en_US} from 'ng-zorro-antd/i18n';
import {registerLocaleData} from '@angular/common';
import en from '@angular/common/locales/en';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FinanceComponent} from './finance/finance.component';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {FinanceBarChartComponent} from './finance/finance-bar-chart/finance-bar-chart.component';
import {FinancePieChartComponent} from './finance/finance-pie-chart/finance-pie-chart.component';
import {FinanceTableComponent} from './finance/finance-table/finance-table.component';

registerLocaleData(en);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SignInComponent,
    EventCreateComponent,
    FinanceComponent,
    FinanceBarChartComponent,
    FinancePieChartComponent,
    FinanceTableComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FontAwesomeModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgZorroAntdModule,
    NgxChartsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NetworkInterceptor,
      multi: true,
    },
    {provide: NZ_I18N, useValue: en_US},
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
