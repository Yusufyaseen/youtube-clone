import { Component, OnInit } from '@angular/core';
import { SubscriptionsDto } from '../subscriptions-dto';
import { UserService } from '../user.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.css']
})
export class SubscriptionsComponent implements OnInit {

  subscriptions: Array<SubscriptionsDto> = [];
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getSubscribeToUser().subscribe((data) => {
      this.subscriptions = data;
      console.log("-------------");
      console.log(this.subscriptions);
    });
  }

}
