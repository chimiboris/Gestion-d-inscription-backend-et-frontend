// 👉 AJOUT OBLIGATOIRE EN PREMIÈRE LIGNE
declare const require: any;

import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ChatService {
  private client: Client;
  public messages$ = new Subject<any>();

  constructor() {
    const SockJS = require('sockjs-client/dist/sockjs.min.js'); // ✅ CORRECT

    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-chat'),
      reconnectDelay: 5000,
      debug: str => console.log(str),
    });


    // this.client = new Client({
    //   webSocketFactory: () => new SockJS('http://localhost:8080/ws-chat'),
    //   //webSocketFactory: () => new SockJS('/ws-chat'),
    //   reconnectDelay: 5000,
    //   debug: str => console.log(str),
    // });

    this.client.onConnect = () => {
      this.client.subscribe('/topic/chat', (message: IMessage) => {
        const msg = JSON.parse(message.body);
        this.messages$.next(msg);
      });
    };

    this.client.onStompError = frame => {
      console.error('Erreur STOMP:', frame.headers['message']);
      console.error('Détails:', frame.body);
    };
  }

  connect(): void {
    this.client.activate();
  }

  sendMessage(contenu: string): void {
    const message = {
      contenu,
      destinataireLogin: 'admin',
    };

    this.client.publish({
      destination: '/ws/chat/send',
      body: JSON.stringify(message),
    });
  }
}
