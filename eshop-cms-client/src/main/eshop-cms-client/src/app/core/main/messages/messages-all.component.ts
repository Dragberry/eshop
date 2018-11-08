import { MessageType } from '../../../shared/model/message';
import { Message } from 'src/app/shared/model/message';
import { Component, OnInit, ViewChildren, QueryList } from '@angular/core';
import { MessageService } from '../../service/message.service';
import { MessagesComponent } from './messages.component';

@Component({
    selector: 'app-messages-all',
    templateUrl: './messages-all.component.html'
})
export class MessagesAllComponent implements OnInit {

  @ViewChildren(MessagesComponent)
  messagesComponents: QueryList<MessagesComponent>;

  errors: Message[];
  warnings: Message[];
  success: Message[];
  infos: Message[];

  constructor(private messageService: MessageService) {}

  ngOnInit() {
      this.messageService.subscribe(messages => {
        this.errors = messages.get(MessageType.ERROR);
        this.warnings = messages.get(MessageType.WARNING);
        this.success = messages.get(MessageType.SUCCESS);
        this.infos = messages.get(MessageType.INFO);
      });
  }

  clear(messageType: MessageType): void {
    console.log('Clear message type ', messageType);
  }
}
