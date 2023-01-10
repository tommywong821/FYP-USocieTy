import {Component, OnInit} from '@angular/core';
import {SidebarOption} from './home';
import {AuthService} from '../services/auth.service';
import {faHome, faUsers} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  sidebarOptions: SidebarOption[] = [
    {
      name: 'Home',
      icon: faHome,
    },
    {
      name: 'Event',
      icon: faUsers,
    },
  ];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // TODO
  }
}
