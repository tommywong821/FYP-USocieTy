import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import {Path} from '../app-routing.module';
@Component({
  selector: 'app-society',
  templateUrl: './society.component.html',
  styleUrls: ['./society.component.scss']
})
export class SocietyComponent implements OnInit {

  constructor(private apiService: ApiService,private router: Router) { }
  societyList: any;
  ngOnInit(): void {
    this.getSociety();
  }
  getSociety():void{
    this.apiService.getAllSociety().subscribe((response)=>{
    this.societyList=response;
    console.log("Society List:");
    console.log(this.societyList);
    });
  }

  toggleViewEvent(societyName: string): void {
    this.router.navigate([Path.Main, Path.Society, Path.ViewEvent], {queryParams: {societyName: societyName}});
  }

}
