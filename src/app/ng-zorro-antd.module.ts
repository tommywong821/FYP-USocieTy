import {NgModule} from '@angular/core';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzMessageModule} from 'ng-zorro-antd/message';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzUploadModule} from 'ng-zorro-antd/upload';

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
    NzTableModule,
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
    NzTableModule,
  ],
})
export class NgZorroAntdModule {}
