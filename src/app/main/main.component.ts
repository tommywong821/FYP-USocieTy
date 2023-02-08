import {Component, OnInit} from '@angular/core';
import {faHome, faPiggyBank, faUsers} from '@fortawesome/free-solid-svg-icons';
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
      link: Path.Event,
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
