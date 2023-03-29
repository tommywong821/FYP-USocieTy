import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import {filter, ReplaySubject, Subject, switchMap, tap} from 'rxjs';

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


  refreshSocietyMember$ = new Subject();
  refreshEnrolledSocietyMember$ = new Subject();
  
  ngOnInit(): void {
    this.societyName=this.route.snapshot.queryParamMap.get('societyName');

    this.refreshSocietyMember$.pipe(
      switchMap(() => this.apiService.getAllSocietyMember(this.societyName)),
    ).subscribe((response)=>{
      this.societyMemberlist=response;
      console.log("Society Member List:");
      console.log(this.societyMemberlist); 
      });

      this.refreshEnrolledSocietyMember$.pipe(
        switchMap(() => this.apiService.getenrolledSocietyRecord(this.societyName)),
      ).subscribe((response)=>{
        this.enrolledSocietiesList=response;
        console.log("enrolledSocietiesList:");
        console.log(this.enrolledSocietiesList); 
        });

    this.refreshSocietyMember$.next({});
    this.refreshEnrolledSocietyMember$.next({});
  }
  
  deleteSocietyMember(studentId:string|null):void{
    let temp=studentId?.toString();
    this.apiService.deleteSocietyMember(this.societyName,[temp!]);
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
    setTimeout(() => {
      this.refreshEnrolledSocietyMember$.next({});
    }, 500);
    setTimeout(() => {
      this.refreshSocietyMember$.next({});
    }, 500);
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
  approveAllSocietyRequest():void{
    for(let i=0;i<this.enrolledSocietiesList.length;i++){
    this.apiService.updateEnrolledSocietyRecord(this.enrolledSocietiesList[i].societyId,this.enrolledSocietiesList[i].studentId,"SUCCESS");
  }
    console.log("Click the Approve button");
    setTimeout(() => {
      this.refreshEnrolledSocietyMember$.next({});
    }, 2000);
    setTimeout(() => {
      this.refreshSocietyMember$.next({});
    }, 2000);
  }
}
