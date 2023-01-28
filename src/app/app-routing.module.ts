import {AppComponent} from './app.component';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './services/auth.guard';
import {MainComponent} from './main/main.component';

export enum Path {
  Main = 'main',
  Home = 'home',
  SignIn = 'sign-in',
  CreateEvent = 'event/create',
  Finance = 'finance',
}

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: AppComponent,
    canActivate: [AuthGuard],
  },
  {
    path: Path.Main,
    component: MainComponent,
    children: [
      {
        path: Path.Home,
        component: HomeComponent,
      },
      {
        path: Path.CreateEvent,
        component: EventCreateComponent,
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
