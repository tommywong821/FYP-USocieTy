import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './services/auth.guard';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'signIn'},
  {path: 'signIn', component: SignInComponent},
  {path: 'home', component: HomeComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
