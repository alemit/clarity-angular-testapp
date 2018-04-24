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
    closeSuccessAlert: Boolean = true;
    closeErrorAlert: Boolean = true;
    modalTitle: string;
    descSort = ClrDatagridSortOrder.DESC;
    validIpInput: Boolean = true;
    validHostnameInput: Boolean = true;
    ipValidationMsg: string = '';
    hostnameValidationMsg: string = '';

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
                    this.showSuccessAlert();
                    this.isModalOpen = false;
                }, (error) => {
                    if (error.status == 400 && error.error.fieldValidation) {
                        this.showIncorrectFields(error);
                    } else {
                        this.showErrorAlert();
                        console.log(error);
                        this.isModalOpen = false;
                    }
                });
        } else {
            this.networkInfoService.editNetworkInfo(this.networkInfoObj).subscribe(
                (response) => {
                    this.loadAll();
                    this.showSuccessAlert();
                    this.isModalOpen = false;
                }, (error) => {
                    if (error.status == 400 && error.error.fieldValidation) {
                        this.showIncorrectFields(error);
                    } else {
                        this.showErrorAlert();
                        console.log(error);
                        this.isModalOpen = false;
                    }
                });
        }
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
                this.showSuccessAlert();
                this.loadAll();
            }, (error) => {
                console.log(error);
                this.showErrorAlert();
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

    private showSuccessAlert() {
        this.closeSuccessAlert = false;
        setTimeout(() => {
            this.closeSuccessAlert = true;
        }, 3000);
    }

    private showErrorAlert() {
        this.closeErrorAlert = false;
        setTimeout(() => {
            this.closeErrorAlert = true;
        }, 3000);
    }

    private clearValidation() {
        this.ipValidationMsg = '';
        this.validIpInput = true;
        this.hostnameValidationMsg = '';
        this.validHostnameInput = true;
    }

    private showIncorrectFields(error: any) {
        this.clearValidation();
        let fieldsErrors = error.error.errors;
        if (fieldsErrors) {
            console.log(fieldsErrors);
            fieldsErrors.forEach(err => {
                let field = err.field;
                let errorMessage = err.errorMsg;
                console.log(field + ': ' + errorMessage);
                if (field == 'ip') {
                    this.ipValidationMsg = errorMessage;
                    this.validIpInput = false;
                }
                if (field == 'hostname') {
                    this.hostnameValidationMsg = errorMessage;
                    this.validHostnameInput = false;
                }
            });
        }
    }
}