import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {HomeComponent} from './home/home.component';
import {SignInComponent} from './auth/sign-in/sign-in.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {EventCreateComponent} from './event/event-create/event-create.component';
import {AuthService} from './services/auth.service';

@NgModule({
  declarations: [AppComponent, HomeComponent, SignInComponent, EventCreateComponent],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FontAwesomeModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
