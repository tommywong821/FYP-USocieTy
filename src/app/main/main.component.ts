import {Component, OnInit} from '@angular/core';
import {faHome, faUsers} from '@fortawesome/free-solid-svg-icons';
import {Path} from '../app-routing.module';
import {SidebarOption} from '../home/home';
import {User} from '../model/user';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  sidebarOptions: SidebarOption[] = [
    {
      name: 'Home',
      link: Path.Home,
      icon: faHome,
    },
    {
      name: 'Event',
      link: Path.CreateEvent,
      icon: faUsers,
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
