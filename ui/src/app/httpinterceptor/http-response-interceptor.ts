import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router'
import 'rxjs/add/observable/throw'
import 'rxjs/add/operator/catch';
import { LoginService } from '../shared/service/login.service';

@Injectable()
export class HttpResponseInterceptor implements HttpInterceptor {
    constructor(private loginService: LoginService, private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log("intercepted request ... ");

        return next.handle(req)
            .do((response) => {
                return this.handleResponse(response);
            })
            .catch(error => {
                return this.handleError(req, error);
            })
    }

    private handleResponse(event: HttpEvent<any>): object {
        if (event instanceof HttpResponse) {
            if (event.status == 204) {
                return undefined;
            } else {
                this.loginService.loggedIn.next(true);
                return event.body || {};
            }
        }
    }

    private handleError(req, error): Observable<HttpEvent<any>> {
        if (error instanceof HttpErrorResponse) {
            if (error.status == 401) {
                this.loginService.loggedIn.next(false);
                this.router.navigate(['/login']);
            }
            return Observable.throw(error);
        }
    }
}
