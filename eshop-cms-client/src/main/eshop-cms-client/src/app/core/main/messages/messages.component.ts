import { Component, OnInit } from '@angular/core';
import { MessageService } from '../../service/message.service';

@Component({
    selector: 'app-messages',
    templateUrl: './messages.component.html'
})
export class MessagesComponent implements OnInit {

    constructor(private messageService: MessageService) {}

    ngOnInit() {
        this.messageService.subscribe(messages => {
            console.log('MessagesComponent', messages);
        });
    }
}
