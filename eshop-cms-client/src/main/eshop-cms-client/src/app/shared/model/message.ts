export enum MessageType {
    ERROR = 'ERROR',
    WARNING = 'WARNING'
}

export class Message {
    text: string;
    type: MessageType;
}
