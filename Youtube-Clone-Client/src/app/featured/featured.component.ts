import {Component, OnInit} from '@angular/core';
import {VideoService} from "../video.service";
import {VideoDto} from "../video-dto";
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit {

  featuredVideos: Array<VideoDto> = [];

  constructor(private videoService: VideoService, private oidcSecurityService: OidcSecurityService) {
    this.oidcSecurityService.checkAuth()
      .subscribe(({ isAuthenticated, userData }) => {
        console.log(userData);
    });
  }

  ngOnInit(): void {
    this.videoService.getAllVideos().subscribe(response => {
      this.featuredVideos = response;
      // console.log("------------");
      // console.log(this.featuredVideos);
    })
  }

}
