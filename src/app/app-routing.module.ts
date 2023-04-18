import {EventUpdateComponent} from './event/event-update/event-update.component';
import {AppComponent} from './app.component';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {FinanceCreateComponent} from './finance/finance-create/finance-create.component';
import {FinanceComponent} from './finance/finance.component';
import {AuthGuard} from './services/auth.guard';
import {MainComponent} from './main/main.component';
import {EventComponent} from './event/event.component';
import {EventViewComponent} from './event/event-view/event-view.component';
import {SocietyComponent} from './society/society.component';
import {SocietyViewComponent} from './society/society-view/society-view.component';
import {MainGuard} from './services/main.guard';

export enum Path {
  Main = 'main',
  SignIn = 'sign-in',
  Event = 'event',
  CreateEvent = 'create',
  UpdateEvent = 'update',
  ViewEvent = 'view',
  Finance = 'finance',
  CreateFinance = 'create',
  Society = 'society',
}

const routes: Routes = [
  {
    path: '',
    redirectTo: Path.Main,
    pathMatch: 'full',
  },
  {
    path: Path.Main,
    component: MainComponent,
    canActivate: [MainGuard],
    children: [
      {
        path: Path.Event,
        children: [
          {
            path: '',
            component: EventComponent,
          },
          {
            path: Path.CreateEvent,
            component: EventCreateComponent,
          },
          {
            path: Path.UpdateEvent,
            component: EventUpdateComponent,
          },
          {
            path: Path.ViewEvent,
            component: EventViewComponent,
          },
        ],
      },
      {
        path: Path.Finance,
        children: [
          {
            path: '',
            component: FinanceComponent,
          },
          {
            path: Path.CreateFinance,
            component: FinanceCreateComponent,
          },
        ],
      },
      {
        path: Path.Society,
        children: [
          {
            path: '',
            component: SocietyComponent,
          },
          {
            path: Path.ViewEvent,
            component: SocietyViewComponent,
          },
        ],
      },
    ],
  },
  {path: Path.SignIn, component: SignInComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
