import { Component, OnInit, OnDestroy } from '@angular/core';
import { ChatService } from 'app/core/websocket/chat.service';
import { Subscription } from 'rxjs';
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'jhi-chat',
  templateUrl: './chat.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class ChatComponent implements OnInit, OnDestroy {
  message = '';
  messages: any[] = [];
  subscription: Subscription | undefined;

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    if ('Notification' in window && Notification.permission !== 'granted') {
      Notification.requestPermission();
    }

    this.chatService.connect();

    this.subscription = this.chatService.messages$.subscribe((msg: any) => {
      this.messages.push(msg);
      console.log('📥 Message reçu :', msg); // 👍 tu auras le message ici
      if (document.hidden) {
        this.notify(msg);
      }
    });
  }


  send(): void {
    if (this.message.trim()) {
      this.chatService.sendMessage(this.message);
      this.message = '';
    }
  }

  notify(msg: any): void {
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification('📩 Nouveau message', {
        body: `De ${msg.auteur?.login ?? 'Inconnu'}: ${msg.contenu}`,
      });
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
