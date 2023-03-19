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
  societyMemberlist: any=[
    {
      "uuid": "string",
      "itsc": "yhlamav",
      "nickname": "Lam Yui Hin",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Society Member"
      ]
    },
    {
      "uuid": "string",
      "itsc": "kclamau",
      "nickname": "Lam Ka Chun",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Society Member"
      ]
    },
    {
      "uuid": "string",
      "itsc": "wlchanau",
      "nickname": "Chan Wing Lam",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	wlpchoi",
      "nickname": " Chan Cheng Lam",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	ylikk",
      "nickname": " Li Yuansheng",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "cyhobi",
      "nickname": "Ho Chun Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	cykwokaq",
      "nickname": "Kwok Ching Yuk",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "syangcb",
      "nickname": "Yang Shuyu",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "cyleebh",
      "nickname": "Lee Chun Yu",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "jlijh",
      "nickname": " Li Junyu",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Society Member"
      ]
    },
    {
      "uuid": "string",
      "itsc": "ychoiag",
      "nickname": "Choi Yuvin",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "wtyungaa",
      "nickname": " Yung Wing Tung",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	yfchoiac",
      "nickname": " Choi Yuen Fong",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "yypangaa",
      "nickname": "Pang Yu Yin",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "kylambd",
      "nickname": "Lam Kit Yung",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },

    {
      "uuid": "string",
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "mail": "string",
      "enrolledSocieties": [
        "string"
      ],
      "roles": [
        "Student"
      ]
    },
  ];
  enrolledSocietiesList: any = [
    {
  
      "itsc": "yhlamav",
      "nickname": "Lam Yui Hin",
      "status": "PENDING",
    },
    {
  
      "itsc": "kclamau",
      "nickname": "Lam Ka Chun",
      "status": "PENDING",
    },
    {
  
      "itsc": "wlchanau",
      "nickname": "Chan Wing Lam",
      "status": "PENDING",
    },
    {
  
      "itsc": "	wlpchoi",
      "nickname": " Chan Cheng Lam",  
      "status": "PENDING",
  
    },
    {
  
      "itsc": "	ylikk",
      "nickname": " Li Yuansheng",      
      "status": "PENDING",
  
    },
    {
  
      "itsc": "cyhobi",
      "nickname": "Ho Chun Yuen",
      "status": "PENDING",
  
    },
    {
      "itsc": "	cykwokaq",
      "nickname": "Kwok Ching Yuk",  
      "status": "PENDING",
  
    },
    {
      "itsc": "syangcb",
      "nickname": "Yang Shuyu",
      "status": "PENDING",
    },
    {
  
      "itsc": "cyleebh",
      "nickname": "Lee Chun Yu",
         
      "status": "PENDING",
  
    },
    {
  
      "itsc": "jlijh",
      "nickname": " Li Junyu",
         
      "status": "PENDING",
  
    },
    {
  
      "itsc": "ychoiag",
      "nickname": "Choi Yuvin",
  
      "status": "PENDING",
  
    },
    {
  
      "itsc": "wtyungaa",
      "nickname": " Yung Wing Tung",
      "status": "PENDING",
    },
    {
  
      "itsc": "	yfchoiac",
      "nickname": " Choi Yuen Fong",
      "status": "PENDING",
  
    },
    {
  
      "itsc": "yypangaa",
      "nickname": "Pang Yu Yin",     
      "status": "PENDING",
    },
    {
  
      "itsc": "kylambd",
      "nickname": "Lam Kit Yung",   
      "status": "PENDING",
    },
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen", 
      "status": "PENDING",
    },
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",    
      "status": "PENDING",
    },
    {
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",   
      "status": "PENDING",
    },
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
      "status": "PENDING",
  
    },
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
         
      "status": "PENDING",
  
    },
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
         
      "status": "PENDING",
  
    },
  
    {
  
      "itsc": "	hyngaz",
      "nickname": " Ng Ho Yuen",
         
      "status": "PENDING",
  
    },
  ];
  societyName: string|null="";
  ngOnInit(): void {
    this.societyName=this.route.snapshot.queryParamMap.get('societyName');
    console.log("societyName");
    console.log(this.societyName);
    this.getSocietyMember();
  }
  
  getSocietyMember():void{
    this.apiService.getAllSocietyMember(this.societyName).subscribe((response)=>{

/*     this.societyMemberlist=response;
    console.log("Society Member List:");
    console.log(this.societyMemberlist); */
    });
  }
  deleteSocietyMember(studentId:string|null):void{
    this.apiService.deleteSocietyMember(this.societyName,studentId);
    console.log("Click the Set As Student button");
  }

  setAsSocietyMember(studentId:string|null,):void{
    this.apiService.setAsSocietyMember(this.societyName,studentId);
    console.log("Click the Set As Society Member button");
  }

  approveSocietyRequest(studentId:string|null):void{
    this.apiService.updateEnrolledSocietyRecord(this.societyName,studentId,"SUCCESS");
    console.log("Click the Approve button");
  }
}
