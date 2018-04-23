import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ClarityModule } from '@clr/angular';
import { HomeComponent } from './home.component'
import { NetworkInfoService } from '../shared/service/network-info.service'
import { HttpClient } from "@angular/common/http";

@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    CommonModule,
    ClarityModule
  ],
  declarations: [HomeComponent],
  exports: [HomeComponent],
  providers: [NetworkInfoService, HttpClient, HttpModule]
})
export class HomeModule { }
