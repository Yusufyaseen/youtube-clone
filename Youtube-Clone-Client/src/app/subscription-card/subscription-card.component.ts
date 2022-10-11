import { Component, Input, OnInit } from '@angular/core';
import { SubscriptionsDto } from '../subscriptions-dto';

@Component({
  selector: 'app-subscription-card',
  templateUrl: './subscription-card.component.html',
  styleUrls: ['./subscription-card.component.css']
})
export class SubscriptionCardComponent implements OnInit {

  @Input()
  sub!: SubscriptionsDto

  constructor() { }

  ngOnInit(): void {
  }

}
