import {registerLocaleData} from '@angular/common';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import en from '@angular/common/locales/en';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {en_US, NZ_I18N} from 'ng-zorro-antd/i18n';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {EventUpdateComponent} from './event/event-update/event-update.component';
import {EventComponent} from './event/event.component';
import {FinanceBarChartComponent} from './finance/finance-bar-chart/finance-bar-chart.component';
import {FinanceCreateComponent} from './finance/finance-create/finance-create.component';
import {FinancePieChartComponent} from './finance/finance-pie-chart/finance-pie-chart.component';
import {FinanceTableComponent} from './finance/finance-table/finance-table.component';
import {FinanceComponent} from './finance/finance.component';
import {HomeComponent} from './home/home.component';
import {NetworkRequestInterceptor} from './interceptor/network-request-interceptor';
import {NetworkResponseInterceptor} from './interceptor/network-response-interceptor';
import {MainComponent} from './main/main.component';
import {NgZorroAntdModule} from './ng-zorro-antd.module';
import {EventViewComponent} from './event/event-view/event-view.component';
import { SocietyComponent } from './society/society.component';

registerLocaleData(en);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SignInComponent,
    EventCreateComponent,
    MainComponent,
    EventUpdateComponent,
    EventComponent,
    FinanceComponent,
    FinanceBarChartComponent,
    FinancePieChartComponent,
    FinanceTableComponent,
    FinanceCreateComponent,
    EventViewComponent,
    SocietyComponent,
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
      useClass: NetworkRequestInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NetworkResponseInterceptor,
      multi: true,
    },
    {provide: NZ_I18N, useValue: en_US},
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
