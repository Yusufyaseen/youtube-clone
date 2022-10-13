import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { SubscriptionsDto } from './subscriptions-dto';
import { VideoDto } from './video-dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userId: string = '';

  constructor(private httpClient: HttpClient) {
  }

  subscribeToUser(authorId: string): Observable<boolean> {
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/subscribe/" + authorId, null);
  }

  unSubscribeUser(authorId: string): Observable<boolean> {
    return this.httpClient.post<boolean>("http://localhost:8080/api/user/unsubscribe/" + authorId, null);
  }
  getSubscribeToUser(): Observable<SubscriptionsDto[]> {
    return this.httpClient.get<SubscriptionsDto[]>("http://localhost:8080/api/user/get-subscriptions");
  }
  getVideosOfUser(id: string): Observable<VideoDto[]> {
    return this.httpClient.get<VideoDto[]>("http://localhost:8080/api/user/get-videos-of-user/" + id);
  }
  getHistoryOfUser(): Observable<VideoDto[]> {
    return this.httpClient.get<VideoDto[]>("http://localhost:8080/api/user/history");
  }

  registerUser() {
    this.httpClient.get("http://localhost:8080/api/user/register", {responseType: "text"})
      .subscribe(data => {
        this.userId = data;
      })
  }

  getUserId(): string {
    return this.userId;
  }
}
