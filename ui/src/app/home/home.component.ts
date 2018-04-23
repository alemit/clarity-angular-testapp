import { Component, OnInit } from "@angular/core";
import { NetworkInfo } from "../shared/model/network-info"
import { ClrDatagridSortOrder } from '@clr/angular';
import { NetworkInfoService } from '../shared/service/network-info.service'
@Component({
    styleUrls: ['./home.component.scss'],
    templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
    networkInfoList: NetworkInfo[] = [];
    networkInfoObj: NetworkInfo;
    isModalOpen: Boolean = false;
    isModalCreate: Boolean = true;
    modalTitle: string;
    descSort = ClrDatagridSortOrder.DESC;

    constructor(private networkInfoService: NetworkInfoService) { }

    ngOnInit() {
        this.networkInfoObj = new NetworkInfo();
        this.loadAll();
    }

    save() {
        if (this.isModalCreate) {
            this.networkInfoService.createNewNetworkInfo(this.networkInfoObj).subscribe(
                (response) => {
                    this.loadAll();
                }, (error) => {
                    console.log(error);
                });
        } else {
            this.networkInfoService.editNetworkInfo(this.networkInfoObj).subscribe(
                (response) => {
                    this.loadAll();
                }, (error) => {
                    console.log(error);
                });
        }
        this.isModalOpen = false;
    }

    onEdit(networkInfo: NetworkInfo) {
        this.networkInfoObj = networkInfo;
        this.openModal(false);
        console.log('Edited object with id:' + networkInfo.id);
    }

    onDelete(networkInfo: NetworkInfo) {
        this.networkInfoService.deleteNetworkInfoById(networkInfo.id).subscribe(
            (response) => {
                console.log('Deleted object with id:' + networkInfo.id);
                this.loadAll();
            }, (error) => {
                console.log(error);
            }
        );
    }

    openModal(isModalCreate: Boolean) {
        this.isModalCreate = isModalCreate;
        if (isModalCreate) {
            this.networkInfoObj = new NetworkInfo();
            this.modalTitle = 'Add New Item';
        } else {
            this.modalTitle = 'Edit Item';
        }
        this.isModalOpen = true;
    }

    private loadAll() {
        this.networkInfoService.getAllNetworkInfo().subscribe(
            (response) => {
                this.networkInfoList = response;
            }, (error) => {
                console.log(error);
            });
    }
}