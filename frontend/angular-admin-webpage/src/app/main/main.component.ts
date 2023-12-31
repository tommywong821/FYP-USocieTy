import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {faHouseUser, faPiggyBank, faPowerOff, faUsers} from '@fortawesome/free-solid-svg-icons';
import {Path} from '../app-routing.module';
import {User} from '../model/user';
import {AuthService} from '../services/auth.service';
import { environment } from 'src/environments/environment';

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
  faPowerOff = faPowerOff;
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

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.user$.subscribe({
      next: user => {
        this.user = user;
      },
    });
  }

  signOut(): void {
    this.authService.signOut();
    const url = `${environment.cas_url}/logout?service=${encodeURIComponent(environment.app_url)}`;

    window.location.assign(url);
  }
}
