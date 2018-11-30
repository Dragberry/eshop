import { Message, MessageType } from 'src/app/shared/model';
import { Component, OnInit, ViewChildren, QueryList } from '@angular/core';
import { MessageService } from '../../service/message.service';
import { MessagesComponent } from './messages.component';
import { Router, NavigationEnd } from '@angular/router';

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

  constructor(private messageService: MessageService, private router: Router) {
    this.router.events.subscribe((navigation: any) => {
      if (navigation instanceof NavigationEnd) {
        this.messageService.clearAll();
      }
    });
  }

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
