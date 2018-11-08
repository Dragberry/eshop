export enum MessageType {
    ERROR = 'ERROR',
    WARNING = 'WARNING',
    SUCCESS = 'SUCCESS',
    INFO = 'INFO'
}

export class Message {
    text: string;
    type: MessageType;
}
