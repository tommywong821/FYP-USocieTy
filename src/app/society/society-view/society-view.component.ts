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
  enrolledSocietiesList:any;
  societyName: string|null="";
  societyNameList:any;
  ngOnInit(): void {
    this.societyName=this.route.snapshot.queryParamMap.get('societyName');
    console.log("societyName");
    console.log(this.societyName);
    this.getSocietyMember();
    this.getenrolledSocietyMember();
  }
  
  getSocietyMember():void{
    this.apiService.getAllSocietyMember(this.societyName).subscribe((response)=>{
    this.societyMemberlist=response;
    console.log("Society Member List:");
    console.log(this.societyMemberlist); 
    });
  }

  getenrolledSocietyMember():void{
    this.apiService.getenrolledSocietyRecord(this.societyName).subscribe((response)=>{
    this.enrolledSocietiesList=response;
    console.log("enrolledSocietiesList:");
    console.log(this.enrolledSocietiesList); 
    });
  }
  deleteSocietyMember(studentId:string|null):void{
    this.apiService.deleteSocietyMember(this.societyName,studentId);
    console.log("Click the delete member button");
  }

  setAsSocietyMember(studentId:string|null,):void{
    let temp=studentId?.toString();
    this.apiService.setAsSocietyMember(this.societyName,[temp!]);
    console.log("Click the Set As Society Member button");
    console.log(studentId);
  }

  approveSocietyRequest(societyId:string|null,studentId:string|null):void{
    this.apiService.updateEnrolledSocietyRecord(societyId,studentId,"SUCCESS");
    console.log("Click the Approve button");
  }
  isSocietyMember(roleList:string[]|null):boolean{
    let temp=this.societyName?.toString();
    if(roleList?.includes(temp!))
    {
    return true;
    }
    else 
    return false
  }
}
