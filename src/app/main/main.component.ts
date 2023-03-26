import {Component, OnInit} from '@angular/core';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {faHouseUser, faPiggyBank, faUsers} from '@fortawesome/free-solid-svg-icons';
import {Path} from '../app-routing.module';
import {User} from '../model/user';
import {AuthService} from '../services/auth.service';

export interface SidebarOption {
  name: string;
  link: string;
  icon?: IconProp;
}

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  sidebarOptions: SidebarOption[] = [
    {
      name: 'Event',
      link: Path.Event,
      icon: faUsers,
    },
    {
      name: 'Societies',
      link: Path.Society,
      icon: faHouseUser,
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
