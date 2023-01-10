import {Component, OnInit} from '@angular/core';
import {faHome, faUsers} from '@fortawesome/free-solid-svg-icons';
import {SidebarOption} from './home/home';
import {AuthService} from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  sidebarOptions: SidebarOption[] = [
    {
      name: 'Home',
      link: 'home',
      icon: faHome,
    },
    {
      name: 'Event',
      link: 'events/create',
      icon: faUsers,
    },
  ];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Dummy
  }
}
