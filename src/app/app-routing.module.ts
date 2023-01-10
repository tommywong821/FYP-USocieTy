import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './services/auth.guard';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'home',
        component: HomeComponent,
      },
      {path: 'events/create', component: EventCreateComponent},
    ],
    // canActivate: [AuthGuard],
  },
  // {path: 'sign-in', component: SignInComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
