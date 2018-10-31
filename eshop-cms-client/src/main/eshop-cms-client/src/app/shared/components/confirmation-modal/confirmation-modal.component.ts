import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';

const DEFAULT_TITLE_KEY = 'common.messages.confirmationTitle';
const DEFAULT_MESSAGE_KEY = 'common.messages.confirmationDefaultMessage';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent implements OnInit {

  titleKey: string;
  title: string;
  messageKey: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;

  constructor(public bsModalRef: BsModalRef, private translate: TranslateService) {}

  ngOnInit() {
    if (!this.title) {
      if (!this.titleKey) {
        this.titleKey = DEFAULT_TITLE_KEY;
      }
      this.translate.get(this.titleKey).subscribe(msg => this.title = msg);
    }
    if (!this.message) {
      if (!this.messageKey) {
        this.messageKey = DEFAULT_MESSAGE_KEY;
      }
      this.translate.get(this.messageKey).subscribe(msg => this.message = msg);
    }
  }

  confirm(): void {
    if (this.onConfirm) {
      this.onConfirm();
    }
    this.bsModalRef.hide();
  }

  cancel(): void {
    if (this.onCancel) {
      this.onCancel();
    }
    this.bsModalRef.hide();
  }

}
