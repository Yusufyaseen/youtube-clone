import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { VideoDto } from '../video-dto';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  featuredVideos: Array<VideoDto> = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getHistoryOfUser().subscribe(response => {
      this.featuredVideos = response;
      // console.log("------------");
      // console.log(this.featuredVideos);
    })
  }

}
