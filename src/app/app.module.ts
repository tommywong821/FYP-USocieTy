import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AuthCasModComponent, AuthCasModModule } from 'auth-cas-mod';
import { environment } from 'src/environments/environment';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthCasModModule.forRoot(environment)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
