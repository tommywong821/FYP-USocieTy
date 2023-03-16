import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-society-view',
  templateUrl: './society-view.component.html',
  styleUrls: ['./society-view.component.scss']
})
export class SocietyViewComponent implements OnInit {
  constructor(private apiService: ApiService,private route: ActivatedRoute) { }
  societyMemberlist: any;
  societyName: string|null="";
  ngOnInit(): void {
    this.societyName=this.route.snapshot.queryParamMap.get('societyName');
    console.log("societyName");
    console.log(this.societyName);
    this.getSocietyMember();
  }
  
  getSocietyMember():void{
    this.apiService.getAllSocietyMember(this.societyName).subscribe((response)=>{

    this.societyMemberlist=response;
    console.log("Society Member List:");
    console.log(this.societyMemberlist);
    });
  }

}
