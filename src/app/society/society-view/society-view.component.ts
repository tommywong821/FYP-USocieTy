import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import {filter, ReplaySubject, Subject, switchMap, tap} from 'rxjs';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import { EventAction } from 'src/app/model/event';
@Component({
  selector: 'app-society-view',
  templateUrl: './society-view.component.html',
  styleUrls: ['./society-view.component.scss']
})
export class SocietyViewComponent implements OnInit {
   loadingMessage: NzMessageRef | null = null;
  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };
  constructor(private apiService: ApiService,private route: ActivatedRoute,private message: NzMessageService,) { }
  societyMemberlist: any;
  enrolledSocietiesList:any;
  societyName: string|null="";


  refreshSocietyMember$ = new Subject();
  refreshEnrolledSocietyMember$ = new Subject();
  
  ngOnInit(): void {
    this.societyName=this.route.snapshot.queryParamMap.get('societyName');

    this.refreshSocietyMember$.pipe(
      tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching data...'))),
      switchMap(() => this.apiService.getAllSocietyMember(this.societyName)),
      tap(() => this.message.remove(this.messages[EventAction.Fetch]!.messageId))
    ).subscribe((response)=>{
      this.societyMemberlist=response;
      console.log("Society Member List:");
      console.log(this.societyMemberlist); 
      },
     );

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
    this.apiService.deleteSocietyMember(this.societyName,studentId).subscribe((response)=>{
    console.log("Click the delete member button");
    setTimeout(() => {
      this.refreshSocietyMember$.next({});
    }, 500);
    this.message.success('Successfully Remove Administrative Rights')
    },(error)=>{
    this.message.create('error', `Remove Administrative Rights Failed`);
    });
  }

  setAsSocietyMember(studentId:string|null,):void{
    let temp=studentId?.toString();
    this.apiService.setAsSocietyMember(this.societyName,[temp!]).subscribe((response)=>{
    console.log("Click the Set As Society Member button");
    console.log(studentId);
    setTimeout(() => {
      this.refreshSocietyMember$.next({});
    }, 1000);
    this.message.success('Successfully Asign Administrative Rights')
  },(error)=>{
    this.message.create('error', `Asign Administrative Rights Failed`);
  });
  }

  approveSocietyRequest(societyId:string|null,studentId:string|null):void{
    this.apiService.updateEnrolledSocietyRecord(societyId,studentId,"SUCCESS").subscribe((response)=>{
    console.log("Click the Approve button");
    setTimeout(() => {
      this.refreshEnrolledSocietyMember$.next({});
    }, 500);
    setTimeout(() => {
      this.refreshSocietyMember$.next({});
    }, 500);
    this.message.success('Successfully Approve Request')
  },(error)=>{
    this.message.create('error', `Approve Request Failed`);
  });
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
    this.message.success('Successfully Approve ALL Request')
  }
}
