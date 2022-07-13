import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AuthCasModModule} from 'auth-cas-mod';
import {environment} from 'src/environments/environment';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthCasModModule.forRoot(environment),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
