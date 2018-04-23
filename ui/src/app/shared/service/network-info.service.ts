import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { environment } from "../../../environments/environment";
import { NetworkInfo } from '../model/network-info';

@Injectable()
export class NetworkInfoService {
  private server_url: string;

  constructor(private http: HttpClient) {
    this.server_url = environment._SERVER_ENDPOINT;
  }

  public getAllNetworkInfo(): Observable<NetworkInfo[]> {
    const url = this.server_url + '/network-info';
    return this.http.get<NetworkInfo[]>(url);
  }

  public createNewNetworkInfo(networkInfo: NetworkInfo): Observable<NetworkInfo> {
    const url = this.server_url + '/network-info';
    return this.http.post<NetworkInfo>(url, networkInfo);
  }
}