import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-society',
  templateUrl: './society.component.html',
  styleUrls: ['./society.component.scss']
})
export class SocietyComponent implements OnInit {

  constructor(private apiService: ApiService) { }
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

}
