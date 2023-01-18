import {NgModule} from '@angular/core';

import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzUploadModule} from 'ng-zorro-antd/upload';
import {NzMessageModule} from 'ng-zorro-antd/message';
import {NzSelectModule} from 'ng-zorro-antd/select';

@NgModule({
  imports: [
    NzIconModule,
    NzDatePickerModule,
    NzInputModule,
    NzButtonModule,
    NzFormModule,
    NzUploadModule,
    NzMessageModule,
    NzSelectModule,
  ],
  exports: [
    NzIconModule,
    NzDatePickerModule,
    NzInputModule,
    NzButtonModule,
    NzFormModule,
    NzUploadModule,
    NzMessageModule,
    NzSelectModule,
  ],
})
export class NgZorroAntdModule {}
