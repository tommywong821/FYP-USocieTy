import {Component, OnInit} from '@angular/core';
import {faHome, faPiggyBank, faUsers} from '@fortawesome/free-solid-svg-icons';
import {SidebarOption} from './home/home';
import {User} from './model/user';
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
    {
      name: 'Finance',
      link: 'finance',
      icon: faPiggyBank,
    },
  ];

  user: User | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.user$.subscribe({
      next: user => {
        this.user = user;
      },
    });
  }
}
