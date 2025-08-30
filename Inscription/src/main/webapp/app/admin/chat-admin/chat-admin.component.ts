import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatService } from 'app/core/websocket/chat.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-chat-admin',
  standalone: true,
  templateUrl: './chat-admin.component.html',
  imports: [CommonModule, FormsModule],
})
export class ChatAdminComponent implements OnInit, OnDestroy {
  messages: any[] = [];
  subscription?: Subscription;

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.chatService.connect();

    this.subscription = this.chatService.messages$.subscribe(msg => {
      this.messages.push(msg);
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
