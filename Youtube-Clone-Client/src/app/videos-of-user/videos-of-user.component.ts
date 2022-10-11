import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../user.service';
import { VideoDto } from '../video-dto';

@Component({
  selector: 'app-videos-of-user',
  templateUrl: './videos-of-user.component.html',
  styleUrls: ['./videos-of-user.component.css']
})
export class VideosOfUserComponent implements OnInit {


  featuredVideos: Array<VideoDto> = [];
  userId!: string;


  constructor(private userService: UserService, private activatedRoute: ActivatedRoute) {
      this.userId = this.activatedRoute.snapshot.params['userId'];
   }

  ngOnInit(): void {
    this.userService.getVideosOfUser(this.userId).subscribe((data) => {
        this.featuredVideos = data;
    });
  }

}
