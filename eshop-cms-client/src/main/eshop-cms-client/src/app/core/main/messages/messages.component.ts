import { MessageType } from '../../../shared/model/message';
import { Message } from 'src/app/shared/model/message';
import { Component, Input, Output, EventEmitter } from '@angular/core';

const TYPES = new Map<string, MessageType>();
TYPES.set('danger', MessageType.ERROR);
TYPES.set('warning', MessageType.WARNING);
TYPES.set('success', MessageType.SUCCESS);
TYPES.set('info', MessageType.INFO);

@Component({
    selector: 'app-messages',
    templateUrl: './messages.component.html'
})
export class MessagesComponent {

  @Input()
  type: MessageType;

  @Input()
  messages: Message[];

  @Output()
  cleared: EventEmitter<MessageType> = new EventEmitter();

  clear(): void {
    this.messages = null;
    this.cleared.emit(TYPES.get(this.type));
  }
}
