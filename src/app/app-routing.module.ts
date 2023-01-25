import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './services/auth.guard';

export enum Path {
  Home = 'home',
  SignIn = 'sign-in',
  CreateEvent = 'event/create',
  Finance = 'finance',
}

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: Path.Home,
        component: HomeComponent,
        outlet: 'aux',
      },
      {
        path: Path.CreateEvent,
        component: EventCreateComponent,
        outlet: 'aux',
      },
    ],
    canActivate: [AuthGuard],
  },
  {path: Path.SignIn, component: SignInComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
