import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { ClarityModule } from '@clr/angular';
import { AppComponent } from './app.component';
import { ROUTING } from "./app.routing";
import { HomeComponent } from "./home/home.component";
import { HomeModule } from './home/home.module';
import { LoginComponent } from './login/login.component';
import { HttpResponseInterceptor } from './httpinterceptor/http-response-interceptor'
import { UserService } from './shared/service/user.service';
import { RegisterComponent } from './register/register.component'
import { LoginService } from "./shared/service/login.service";

const HttpResponseInterceptorProvider = {
    provide: HTTP_INTERCEPTORS,
    useClass: HttpResponseInterceptor,
    multi: true
};

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        FormsModule,
        HttpModule,
        ClarityModule,
        ROUTING,
        HomeModule
    ],
    providers: [HttpResponseInterceptorProvider, UserService, LoginService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
