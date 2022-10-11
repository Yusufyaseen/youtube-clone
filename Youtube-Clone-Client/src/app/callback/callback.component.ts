import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) {
    console.log("Good");
    setTimeout(() => {
      this.userService.registerUser();
      this.router.navigateByUrl('/featured');
    }, 2000);
  }

  ngOnInit(): void {
  }

}
